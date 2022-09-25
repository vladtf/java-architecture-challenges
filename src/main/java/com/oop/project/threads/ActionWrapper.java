package com.oop.project.threads;

import com.oop.project.models.Action;
import com.oop.project.models.BattlePokemon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ActionWrapper {
    private static final Logger LOGGER = LogManager.getLogger(ActionWrapper.class);

    private final Lock lock = new ReentrantLock();
    private final BattlePokemon pokemon;
    private Action action;

    public ActionWrapper(BattlePokemon pokemon) {
        this.pokemon = pokemon;
    }

    public Action getActionAndReset() {
        synchronized (pokemon) {

            try {
                while (action == null) {
                    pokemon.wait();
                }
            } catch (InterruptedException ignored) {
            }

        }
        Action action = this.action;
        this.action = null;
        return action;
    }

    public void setAction(Action action) {
        synchronized (pokemon) {
            pokemon.notify();
            this.action = action;
        }
    }

    public Action doRandomAction() {
        Action randomAction = pokemon.getRandomAction();
        setAction(randomAction);
        return randomAction;
    }
}
