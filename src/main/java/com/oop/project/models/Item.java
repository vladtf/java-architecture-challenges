package com.oop.project.models;

import java.util.Objects;
import java.util.Set;

public class Item implements Model {
    private String name;
    private Integer health;
    private Attack attack;
    private Defense defense;

    private Item(String name, Integer health, Integer attack, Integer specialAttack, Integer normalDefense, Integer specialDefense) {
        this.name = name;
        this.health = health;
        this.attack = Attack.createAttack(attack, specialAttack);
        this.defense = Defense.createDefense(normalDefense, specialDefense);
    }

    public Item() {
    }

    public static Item createItem(String name, Integer health, Integer normalAttack, Integer specialAttack, Integer normalDefense, Integer specialDefense) {
        return new Item(name, health, normalAttack, specialAttack, normalDefense, specialDefense);
    }

    public static Item createItem() {
        return new Item(null, null, null, null, null, null);
    }

    public static int getTotalHealth(Set<Item> items) {
        return items.stream().map(Item::getHealth).filter(Objects::nonNull).reduce(0, Integer::sum);
    }

    public static Attack getTotalAttack(Set<Item> items) {
        return items.stream().map(Item::getAttack).filter(Objects::nonNull).reduce(Attack.createAttack(), Attack::addAttack);
    }

    public static Defense getTotalDefense(Set<Item> items) {
        return items.stream().map(Item::getDefense).filter(Objects::nonNull).reduce(Defense.createDefense(), Defense::addDefense);
    }

    @Override
    public String toString() {
        return "Item{" +
                "health=" + health +
                ", attack=" + attack +
                ", defense=" + defense +
                '}';
    }

    @Override
    public String getName() {
        return name;
    }

    public Item setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getHealth() {
        return health;
    }

    public Item setHealth(Integer health) {
        this.health = health;
        return this;
    }

    public Attack getAttack() {
        return attack;
    }

    public Item setAttack(Attack attack) {
        this.attack = attack;
        return this;
    }

    public Defense getDefense() {
        return defense;
    }

    public Item setDefense(Defense defense) {
        this.defense = defense;
        return this;
    }

}
