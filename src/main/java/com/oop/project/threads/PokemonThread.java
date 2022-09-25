package com.oop.project.threads;

import com.oop.project.models.Action;
import com.oop.project.models.BattlePokemon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Producer/Consumer
public class PokemonThread extends Thread {
    private static final Logger LOGGER = LogManager.getLogger(PokemonThread.class);


    private final BattlePokemon pokemon;
    private final BattlePokemon pokemon2;
    private final ActionWrapper myActionWrapper;
    private final ActionWrapper enemyActionWrapper;
    private boolean isRunning = true;

    public PokemonThread(BattlePokemon pokemon, BattlePokemon pokemon2, ActionWrapper myActionWrapper, ActionWrapper enemyActionWrapper) {
        this.pokemon = pokemon;
        this.pokemon2 = pokemon2;
        this.myActionWrapper = myActionWrapper;
        this.enemyActionWrapper = enemyActionWrapper;
    }

    @Override
    public void run() {
        Thread.currentThread().setName(pokemon.getName() + "/" + pokemon2.getName());

        while (isRunning && pokemon.isAlive() && pokemon2.isAlive()) {
            Action myAction = myActionWrapper.doRandomAction();
            Action enemyAction = enemyActionWrapper.getActionAndReset();

            LOGGER.debug("myAction: {}; enemyAction: {}", (myAction != null ? myAction.toShortString() : "null"), (enemyAction != null ? enemyAction.toShortString() : "null"));

            pokemon.attackMe(enemyAction, myAction);

            LOGGER.info("Pokemon {} after an event: {}", pokemon.getName(), pokemon.getStatus());
        }
    }

    public void shutdown() {
        isRunning = false;
        myActionWrapper.setAction(null);
        interrupt();
    }
}
