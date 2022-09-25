package com.oop.project.services;

import com.oop.project.models.Model;
import com.oop.project.repositories.FileRepository;
import com.oop.project.threads.PokemonThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public abstract class ServiceBase<T extends Model> {
    private static final Logger LOGGER = LogManager.getLogger(PokemonThread.class);

    protected List<T> items;
    private final FileRepository<T> fileRepository;

    public ServiceBase(FileRepository<T> fileRepository) {
        this.fileRepository = fileRepository;
        updateItems(getInputFilePath());
    }

    public void updateItems(String inputFilePath) {
        LOGGER.info("Reading objects from: {}", inputFilePath);
        items = fileRepository.findAll(inputFilePath);
    }

    public T getByName(String name) {
        return items.stream().filter(t -> t.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public List<T> getAll() {
        return items;
    }

    public abstract String getInputFilePath();
}
