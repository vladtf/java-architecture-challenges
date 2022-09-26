package com.oop.project.services;

import com.oop.project.ioc.ContainerContext;
import com.oop.project.ioc.annotations.Bean;
import com.oop.project.ioc.annotations.Logged;
import com.oop.project.models.Item;
import com.oop.project.repositories.ItemRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

// Adapter
@Bean(lazy = true)
@Logged
public class ItemService extends ServiceBase<Item> {
    private static final String INPUT_FILE_PATH = "items.json";

    public ItemService() {
        super(ContainerContext.getBean(ItemRepository.class));
    }

    @Override
    public String getInputFilePath() {
        return INPUT_FILE_PATH;
    }

    public Set<Item> getRandomItems(int len) {
        ArrayList<Item> auxList = new ArrayList<>(items);
        Collections.shuffle(auxList);

        Set<Item> items = new HashSet<>();

        for (int i = 0; i < len; i++) {
            items.add(auxList.get(i));
        }

        return items;
    }
}
