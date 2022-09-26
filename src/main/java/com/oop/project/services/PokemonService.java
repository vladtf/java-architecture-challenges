package com.oop.project.services;

import com.oop.project.ioc.ContainerContext;
import com.oop.project.ioc.annotations.Bean;
import com.oop.project.ioc.annotations.Logged;
import com.oop.project.models.BattlePokemon;
import com.oop.project.models.Pokemon;
import com.oop.project.repositories.PokemonRepository;

import java.util.Collections;

@Bean(lazy = true)
@Logged
public class PokemonService extends ServiceBase<Pokemon> {
    private static final String INPUT_FILE_PATH = "pokemons.json";

    public PokemonService() {
        super(ContainerContext.getBean(PokemonRepository.class));
    }

    @Override
    public String getInputFilePath() {
        return INPUT_FILE_PATH;
    }

    public BattlePokemon getBattleNeutrel1() {
        return getByName("Neutrel1").prepareToBattle(Collections.emptySet());
    }

    public BattlePokemon getBattleNeutrel2() {
        return getByName("Neutrel2").prepareToBattle(Collections.emptySet());
    }
}
