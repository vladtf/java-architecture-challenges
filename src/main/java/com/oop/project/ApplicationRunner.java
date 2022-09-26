package com.oop.project;

import com.oop.project.ioc.ApplicationContext;
import com.oop.project.ioc.annotations.Bean;
import com.oop.project.models.BattlePokemon;
import com.oop.project.models.Event;
import com.oop.project.models.Trainer;
import com.oop.project.services.PokemonService;
import com.oop.project.services.TrainerService;
import com.oop.project.threads.BattleArena;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Bean
public class ApplicationRunner {
    private static final Logger LOGGER = LogManager.getLogger(ApplicationRunner.class);

    private final PokemonService pokemonService;

    public ApplicationRunner(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    public void run() throws Exception {
        LOGGER.info("Start application.");

        Trainer trainer1 = ApplicationContext.getBeanStatic(TrainerService.class).getPreparedTrainer("Trainer1");
        Trainer trainer2 = ApplicationContext.getBeanStatic(TrainerService.class).getPreparedTrainer("Trainer2");

        LOGGER.info("Trainer1: {}", trainer1);
        LOGGER.info("Trainer2: {}", trainer2);

        int currentCycle = 0;
        Event currentEvent;

        while (true) {
            ExecutorService pool = Executors.newCachedThreadPool();

            currentCycle++;
            currentEvent = Event.getRandomEvent(currentCycle);

            switch (currentEvent) {
                case AGAINST_NEUT_1:
                    pool.execute(new BattleArena(trainer1.getPokemon(0), pokemonService.getBattleNeutrel1(), "T1_vs_N1"));
                    pool.execute(new BattleArena(trainer2.getPokemon(0), pokemonService.getBattleNeutrel1(), "T2_vs_N1"));
                    break;

                case AGAINST_NEUT_2:
                    pool.execute(new BattleArena(trainer1.getPokemon(0), pokemonService.getBattleNeutrel2(), "T1_vs_N2"));
                    pool.execute(new BattleArena(trainer2.getPokemon(0), pokemonService.getBattleNeutrel2(), "T2_vs_N2"));
                    break;
                case TRAINERS_BATTLE:
                    pool.execute(new BattleArena(trainer1.getPokemon(0), trainer2.getPokemon(0), "T1_vs_T2_1"));
                    pool.execute(new BattleArena(trainer1.getPokemon(1), trainer2.getPokemon(1), "T1_vs_T2_2"));
                    pool.execute(new BattleArena(trainer1.getPokemon(2), trainer2.getPokemon(2), "T1_vs_T2_3"));
                    break;
                default:
                    throw new IllegalArgumentException();
            }

            pool.shutdown();
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);

            if (currentEvent == Event.TRAINERS_BATTLE) { // final battle
                List<BattlePokemon> pokemons1 = new ArrayList<>(trainer1.getBattlePokemons());
                List<BattlePokemon> pokemons2 = new ArrayList<>(trainer2.getBattlePokemons());
                Collections.sort(pokemons1);
                Collections.sort(pokemons1);

                LOGGER.info("Starting final battle: {}={}; {}={}",
                        pokemons1.get(0).getName(), pokemons1.get(0).sumAllStats(),
                        pokemons2.get(0).getName(), pokemons2.get(0).sumAllStats());

                pool = Executors.newCachedThreadPool();

                pool.execute(new BattleArena(pokemons1.get(0), pokemons2.get(0), "T1_vs_T2_1"));
                pool.shutdown();
                pool.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
                break;
            }
        }


        LOGGER.info("End application.");
    }


}
