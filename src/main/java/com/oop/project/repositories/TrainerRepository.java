package com.oop.project.repositories;

import com.oop.project.ioc.annotations.Bean;
import com.oop.project.ioc.annotations.Repository;
import com.oop.project.models.Trainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Repository
public class TrainerRepository extends FileRepository<Trainer> {
    private static final Logger LOGGER = LogManager.getLogger(TrainerRepository.class);

    public TrainerRepository() {
    }

    @Override
    public Class<Trainer[]> getModelClass() {
        return Trainer[].class;
    }
}
