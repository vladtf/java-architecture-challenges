package com.oop.project.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Ability implements Model {
    private int damage;

    private int cooldown;

    @JsonProperty("isStun")
    private Boolean isStun;

    @JsonProperty("isDodge")
    private Boolean isDodge;

    @JsonIgnore
    private int lastUsedCountdown = 0;

    private Ability(int damage, Boolean isStun, Boolean isDodge, int cooldown) {
        this.damage = damage;
        this.isStun = isStun;
        this.isDodge = isDodge;
        this.cooldown = cooldown;
    }

    public Ability() {
    }

    public static Ability createAbility(Integer damage, Boolean isStun, Boolean isDodge, Integer cooldown) {
        return new Ability(damage, isStun, isDodge, cooldown);
    }

    public static Ability createAbility() {
        return new Ability(0, null, null, 0);
    }

    @Override
    public String toString() {
        return "Ability{" +
                "damage=" + damage +
                ", isStun=" + isStun +
                ", isDodge=" + isDodge +
                ", cooldown=" + cooldown +
                '}';
    }

    public boolean isInCoolDown() {
        return lastUsedCountdown > 0;
    }

    public Ability useThis() {
        this.lastUsedCountdown = cooldown + 1; // to consider current cycle
        return this;
    }

    public void decrementLastUsed() {
        if (lastUsedCountdown > 0) {
            this.lastUsedCountdown--;
        } else {
            this.lastUsedCountdown = 0;
        }
    }

    public void cooldownNow() {
        this.lastUsedCountdown = 0;
    }

    public Integer getDamage() {
        return damage;
    }

    public Ability setDamage(Integer damage) {
        this.damage = damage;
        return this;
    }

    public boolean getStun() {
        return isStun != null ? isStun : false;
    }

    public Ability setStun(boolean stun) {
        isStun = stun;
        return this;
    }

    public boolean getDodge() {
        return isDodge != null ? isDodge : false;
    }

    public Ability setDodge(boolean dodge) {
        isDodge = dodge;
        return this;
    }

    public Integer getCooldown() {
        return cooldown;
    }

    public Ability setCooldown(Integer cooldown) {
        this.cooldown = cooldown;
        return this;
    }

    @Override
    public String getName() {
        return null;
    }

    public String toShortString() {
        return "{" +
                "damage=" + damage +
                ", stun=" + isStun +
                ", dodge=" + isDodge +
                ", cooldown=" + cooldown +
                '}';

    }

    public int getLastUsedCountdown() {
        return lastUsedCountdown;
    }
}
