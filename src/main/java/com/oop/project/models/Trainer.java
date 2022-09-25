package com.oop.project.models;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Set;
import java.util.TreeSet;

public class Trainer implements Model {

    private String name;

    private int age;

    Set<String> pokemonsName;

    @JsonIgnore
    Set<Pokemon> pokemons;

    @JsonIgnore
    Set<BattlePokemon> battlePokemons = new TreeSet<>();

    @JsonIgnore
    Set<Item> items;

    private Trainer(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Trainer() {
    }

    public static Trainer createTrainer(String name, int age) {
        return new Trainer(name, age);
    }

    public static Trainer createTrainer() {
        return createTrainer(null, 0);
    }


    public String getName() {
        return name;
    }

    public Trainer setName(String name) {
        this.name = name;
        return this;
    }

    public int getAge() {
        return age;
    }

    public Trainer setAge(int age) {
        this.age = age;
        return this;
    }

    public Set<Pokemon> getPokemons() {
        return pokemons;
    }

    public BattlePokemon getPokemon(int index) {
        return (BattlePokemon) battlePokemons.toArray()[index];
    }

    public Trainer setPokemons(Set<Pokemon> pokemons) {
        this.pokemons = pokemons;
        return this;
    }

    public void preparePokemonsToBattle(Set<Item> items) {
        for (Pokemon pokemon : pokemons) {
            battlePokemons.add(pokemon.prepareToBattle(items));
        }
    }

    public Set<String> getPokemonsName() {
        return pokemonsName;
    }

    public Trainer setPokemonsName(Set<String> pokemonsName) {
        this.pokemonsName = pokemonsName;
        return this;
    }

    public Set<BattlePokemon> getBattlePokemons() {
        return battlePokemons;
    }

    public Trainer setBattlePokemons(Set<BattlePokemon> battlePokemons) {
        this.battlePokemons = battlePokemons;
        return this;
    }

    public Set<Item> getItems() {
        return items;
    }

    public Trainer setItems(Set<Item> items) {
        this.items = items;
        return this;
    }

    public void addBattlePokemon(BattlePokemon pokemon) {
        this.battlePokemons.add(pokemon);
    }

    @Override
    public String toString() {
        return "Trainer{" + "name='" + name + '\'' + ", age=" + age + ", pokemons=" + pokemons + '}';
    }

}
