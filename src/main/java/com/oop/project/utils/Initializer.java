package com.oop.project.utils;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.oop.project.models.Ability;
import com.oop.project.models.Item;
import com.oop.project.models.Pokemon;
import com.oop.project.models.Trainer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Initializer {

    public static void initPokemons() {
        List<Pokemon> items = new ArrayList<>();

        items.add(Pokemon.createPokemon("Neutrel1", 10, 3, null, 1, 1, null, null));
        items.add(Pokemon.createPokemon("Neutrel2", 20, 4, null, 1, 1, null, null));

        items.add(Pokemon.createPokemon("Pikachu", 35, null, 4, 2, 3, Ability.createAbility(6, false, false, 4), Ability.createAbility(4, false, false, 5)));
        items.add(Pokemon.createPokemon("Bulbasaur", 42, null, 5, 3, 1, Ability.createAbility(6, false, false, 4), Ability.createAbility(5, false, false, 3)));
        items.add(Pokemon.createPokemon("Charmander", 50, 4, null, 3, 2, Ability.createAbility(4, true, false, 4), Ability.createAbility(7, false, false, 6)));
        items.add(Pokemon.createPokemon("Squirtle", 60, null, 3, 5, 5, Ability.createAbility(4, false, false, 3), Ability.createAbility(2, true, false, 2)));
        items.add(Pokemon.createPokemon("Snorlax", 62, 3, null, 6, 4, Ability.createAbility(4, true, false, 5), Ability.createAbility(0, false, true, 5)));
        items.add(Pokemon.createPokemon("Vulpix", 36, 5, null, 2, 4, Ability.createAbility(8, true, false, 6), Ability.createAbility(2, false, true, 7)));
        items.add(Pokemon.createPokemon("Eevee", 39, null, 4, 3, 3, Ability.createAbility(5, false, false, 3), Ability.createAbility(3, true, false, 3)));
        items.add(Pokemon.createPokemon("Jigglypuff", 34, 4, null, 2, 3, Ability.createAbility(4, true, false, 4), Ability.createAbility(3, true, false, 4)));
        items.add(Pokemon.createPokemon("Meowth", 41, 3, null, 4, 2, Ability.createAbility(5, false, true, 4), Ability.createAbility(1, false, true, 3)));
        items.add(Pokemon.createPokemon("Psyduck", 43, 3, null, 3, 3, Ability.createAbility(2, null, null, 4), Ability.createAbility(2, true, false, 5)));


        writeJsonToFile(items, "pokemons.json");
    }

    public static void initItems() {
        List<Item> items = new ArrayList<>();

        items.add(Item.createItem("Scut", null, null, null, 2, 2));
        items.add(Item.createItem("Vestă", 10, null, null, null, null));
        items.add(Item.createItem("Săbiuță", null, 3, null, null, null));
        items.add(Item.createItem("Baghetă Magică", null, null, 3, null, null));
        items.add(Item.createItem("Vitamine", 2, 2, 2, null, null));
        items.add(Item.createItem("Brad de Crăciun", null, 3, null, 1, null));
        items.add(Item.createItem("Pelerină", null, null, null, null, 3));

        writeJsonToFile(items, "items.json");
    }

    public static void initTrainers() {
        List<Trainer> trainers = new ArrayList<>();

        Trainer trainer1 = Trainer.createTrainer()
                .setName("Trainer1")
                .setAge(10)
                .setPokemonsName(new HashSet<>(Arrays.asList("Pikachu", "Bulbasaur", "Charmander")));

        Trainer trainer2 = Trainer.createTrainer()
                .setName("Trainer2")
                .setAge(10)
                .setPokemonsName(new HashSet<>(Arrays.asList("Squirtle", "Snorlax", "Vulpix")));

        trainers.add(trainer1);
        trainers.add(trainer2);
        writeJsonToFile(trainers, "trainers.json");
    }

    private static void writeJsonToFile(List<?> objects, String filePath) {
        try (Writer writer = new FileWriter(filePath)) {
            ObjectWriter mapper = new ObjectMapper().writer(new DefaultPrettyPrinter());
            mapper.writeValue(writer, objects);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
