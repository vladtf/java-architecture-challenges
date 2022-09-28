package com.oop.project.services;

import com.oop.project.models.Model;
import com.oop.project.repositories.FileRepository;
import com.oop.project.threads.PokemonThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public abstract class ServiceBase<T extends Model> {
    private static final Logger LOGGER = LogManager.getLogger(PokemonThread.class);
    private final FileRepository<T> fileRepository;
    protected List<T> items;

    public ServiceBase(FileRepository<T> fileRepository) {
        this.fileRepository = fileRepository;
        // TODO this method must be called in postConstruct
        updateItems(getInputFilePath());
    }

    public void updateItems(String inputFilePath) {
        LOGGER.info("Reading objects from: {}", inputFilePath);
        items = fileRepository.findAll(inputFilePath);
    }

    public T getByName(String name) {
        return fileRepository.findByName(name, getInputFilePath());
    }

    public List<T> getAll() {
        return items;
    }

    public abstract String getInputFilePath();
}
