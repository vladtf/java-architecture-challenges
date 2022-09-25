package com.oop.project.threads;

import com.oop.project.models.BattlePokemon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BattleArena implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger(BattleArena.class);

    private final BattlePokemon pokemon1;
    private final BattlePokemon pokemon2;
    private final String threadName;

    Lock lock = new ReentrantLock();
    Condition actionCondition1 = lock.newCondition();
    Condition actionCondition2 = lock.newCondition();

    public BattleArena(BattlePokemon pokemon1, BattlePokemon pokemon2, String threadName) {
        this.pokemon1 = pokemon1;
        this.pokemon2 = pokemon2;
        this.threadName = threadName;
    }

    @Override
    public void run() {
        Thread.currentThread().setName(threadName);

        LOGGER.info("Starting battle: {} vs {}", pokemon1.getName(), pokemon2.getName());

        ActionWrapper actionWrapperPokemon1 = new ActionWrapper(pokemon1);
        ActionWrapper actionWrapperPokemon2 = new ActionWrapper(pokemon2);


        PokemonThread pokemonThread1 = new PokemonThread(pokemon1, pokemon2, actionWrapperPokemon1, actionWrapperPokemon2);
        PokemonThread pokemonThread2 = new PokemonThread(pokemon2, pokemon1, actionWrapperPokemon2, actionWrapperPokemon1);

        pokemonThread1.start();
        pokemonThread2.start();

        while (pokemon1.isAlive() && pokemon2.isAlive()) {
            LOGGER.info("{}: {}", pokemon1.getName(), pokemon1.getHealth());
            LOGGER.info("{}: {}", pokemon2.getName(), pokemon2.getHealth());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        pokemonThread1.shutdown();
        pokemonThread2.shutdown();

        try {
            pokemonThread1.join();
            pokemonThread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LOGGER.info("After battle {}: {}", pokemon1.getName(), pokemon1.getStatus());
        LOGGER.info("After battle {}: {}", pokemon2.getName(), pokemon2.getStatus());

        if (pokemon1.isAlive()) {
            LOGGER.info("Winner: {}", pokemon1.getStatus());
            pokemon1.updateWin();
            pokemon2.updateLoose();
        } else if (pokemon2.isAlive()) {
            LOGGER.info("Winner: {}", pokemon2.getStatus());
            pokemon2.updateWin();
            pokemon1.updateLoose();
        } else {
            LOGGER.info("Draw: {} vs {}", pokemon1.getStatus(), pokemon2.getStatus());
            pokemon2.updateLoose();
            pokemon2.updateLoose();
        }
    }
}
