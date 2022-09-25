package com.oop.project.repositories;

import com.oop.project.ioc.annotations.Bean;
import com.oop.project.models.Pokemon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Bean
public class PokemonRepository extends FileRepository<Pokemon> {
    private static final Logger LOGGER = LogManager.getLogger(PokemonRepository.class);

    public PokemonRepository() {
    }

    @Override
    public Class<Pokemon[]> getModelClass() {
        return Pokemon[].class;
    }
}
