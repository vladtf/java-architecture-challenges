package com.oop.project.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oop.project.proxy.ProxyBattlePokemon;

import java.util.Objects;
import java.util.Set;

public class Pokemon implements Model {

    private String name;
    private Integer health;
    private Ability firstAbility;
    private Ability secondAbility;

    @JsonProperty("attack")
    private Attack attack;

    @JsonProperty("defense")
    private Defense defense;

    private Pokemon(String name, Integer health, Integer normalAttack, Integer specialAttack, Integer normalDefense, Integer specialDefense, Ability firstAbility, Ability secondAbility) {
        this.name = name;
        this.health = health;
        this.attack = Attack.createAttack(normalAttack, specialAttack);
        this.defense = Defense.createDefense(normalDefense, specialDefense);
        this.firstAbility = firstAbility;
        this.secondAbility = secondAbility;
    }

    public Pokemon() {
    }

    public static Pokemon createPokemon(String name, Integer health, Integer normalAttack, Integer specialAttack, Integer normalDefense, Integer specialDefense, Ability firstAbility, Ability secondAbility) {
        return new Pokemon(name, health, normalAttack, specialAttack, normalDefense, specialDefense, firstAbility, secondAbility);
    }

    public static Pokemon createPokemon() {
        return new Pokemon(null, null, null, null, null, null, null, null);
    }

    public BattlePokemon prepareToBattle(Set<Item> items) {
        return ProxyBattlePokemon.preparePokemonWithLog(this, items);
    }


    @Override
    public String toString() {
        return "Pokemon{" +
                "name='" + name + '\'' +
                ", health=" + health +
                ", attack=" + attack +
                ", defense=" + defense +
                ", firstAbility=" + firstAbility +
                ", secondAbility=" + secondAbility +
                '}';
    }

    public String toShortString() {
        return "Pokemon{" +
                "name='" + name + '\'' +
                ", health=" + health +
                ", attack=" + attack +
                '}';
    }

    public String getName() {
        return name;
    }


    public Integer getHealth() {
        return health;
    }

    public Attack getAttack() {
        return attack;
    }

    public Defense getDefense() {
        return defense;
    }

    public Ability getFirstAbility() {
        if (firstAbility == null) {
            firstAbility = Ability.createAbility();
        }
        return firstAbility;
    }

    public Ability getSecondAbility() {
        if (secondAbility == null) {
            secondAbility = Ability.createAbility();
        }
        return secondAbility;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pokemon pokemon = (Pokemon) o;
        return Objects.equals(name, pokemon.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
