package com.oop.project.services;

import com.oop.project.ioc.ContainerContext;
import com.oop.project.ioc.annotations.Bean;
import com.oop.project.ioc.annotations.Logged;
import com.oop.project.models.BattlePokemon;
import com.oop.project.models.Pokemon;
import com.oop.project.models.Trainer;
import com.oop.project.repositories.TrainerRepository;
import com.oop.project.threads.PokemonThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Adapter
@Bean(lazy = true, prototype = true)
@Logged
public class TrainerService extends ServiceBase<Trainer> {
    private static final Logger LOGGER = LogManager.getLogger(PokemonThread.class);

    private final ItemService itemService;
    private final PokemonService pokemonService;
    private final String inputFilePath = "trainers.json";

    public TrainerService() {
        super(ContainerContext.getBean(TrainerRepository.class));
        this.itemService = ContainerContext.getBean(ItemService.class);
        this.pokemonService = ContainerContext.getBean(PokemonService.class);
        updateItems(inputFilePath);
    }

    public Trainer getPreparedTrainer(String name) {
        Trainer trainer = getByName(name);
        for (String pokemonName : trainer.getPokemonsName()) {
            Pokemon pokemon = pokemonService.getByName(pokemonName);

            BattlePokemon battlePokemon = pokemon.prepareToBattle(itemService.getRandomItems(3));
            trainer.addBattlePokemon(battlePokemon);
        }

        return trainer;
    }

    @Override
    public String getInputFilePath() {
        return inputFilePath;
    }
}
