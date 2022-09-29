package com.oop.project.repositories;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.oop.project.models.Model;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// Repository pattern to encapsulate raw reading from input files
public abstract class FileRepository<T extends Model> {
    private static final Logger LOGGER = LogManager.getLogger(ItemRepository.class);

    public abstract Class<T[]> getModelClass();

    public List<T> findAll(String inputFilePath) {
        if (inputFilePath == null) {
            return Collections.emptyList();
        }
        ObjectMapper mapper = new ObjectMapper();
        try (Reader reader = Files.newBufferedReader(new ClassPathResource(inputFilePath).getFile().toPath())) {
            T[] values = mapper.readValue(reader, getModelClass());
            return Arrays.asList(values);
        } catch (IOException e) {
            LOGGER.error("Exception: ", e);
        }

        return Collections.emptyList();
    }

    // todo may try to implement filter pattern (create an object that will filter and register a filter by name)
    public T findByName(String name, String inputFilePath) {
        return findAll(inputFilePath).stream()
                .filter(item -> name.equals(item.getName()))
                .findFirst().orElse(null);
    }
}
