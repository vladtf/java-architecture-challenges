package com.oop.project.repositories;

import com.oop.project.ioc.annotations.Bean;
import com.oop.project.models.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Bean
public class ItemRepository extends FileRepository<Item> {
    private static final Logger LOGGER = LogManager.getLogger(ItemRepository.class);

    public ItemRepository() {
    }

    @Override
    public Class<Item[]> getModelClass() {
        return Item[].class;
    }

}
