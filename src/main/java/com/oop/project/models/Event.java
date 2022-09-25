package com.oop.project.models;

import java.util.Random;

public enum Event {
    AGAINST_NEUT_1(0), AGAINST_NEUT_2(1), TRAINERS_BATTLE(2);

    private final int value;

    Event(int value) {
        this.value = value;
    }

    public static Event getRandomEvent(int currentCycle) {
        if (currentCycle == 3) {
            return TRAINERS_BATTLE;
        }

        int randomEvent = Math.abs(new Random().nextInt()) % values().length;

        for (Event event : values()) {
            if (randomEvent == event.value) {
                return event;
            }
        }

        throw new IllegalArgumentException();
    }
}
