package com.oop.project.models;

import com.oop.project.ioc.annotations.Autowired;
import com.oop.project.ioc.annotations.Bean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.MessageFormat;
import java.util.Random;
import java.util.Set;

// Liskov Substitution Principle
@Bean(prototype = true)
public class BattlePokemon implements Comparable<BattlePokemon> {
    private static final Logger LOGGER = LogManager.getLogger(BattlePokemon.class);

    private Pokemon pokemon;
    private Set<Item> items;
    private Attack attack;
    private Defense defense;
    private int health;
    private int wonBattles = 0;


    private boolean isStuned = false;

    @Autowired
    public BattlePokemon(Pokemon pokemon, Set<Item> items) {
        this.pokemon = pokemon;
        this.items = items;
        initCharacteristics(pokemon, items);
    }

    // requires empty constructor for Proxy
    public BattlePokemon() {
    }

    private void initCharacteristics(Pokemon pokemon, Set<Item> items) {
        pokemon.getFirstAbility().cooldownNow(); // refresh abilities instantly
        pokemon.getSecondAbility().cooldownNow();

        this.health = pokemon.getHealth() + Item.getTotalHealth(items) + wonBattles;
        this.attack = pokemon.getAttack().addAttack(Item.getTotalAttack(items)).addAttack(Attack.createAttack(wonBattles, wonBattles));
        this.defense = pokemon.getDefense().addDefense(Item.getTotalDefense(items)).addDefense(Defense.createDefense(wonBattles, wonBattles));
    }


    public boolean attackMe(Action enemyAction, Action myAction) {
        if (enemyAction == null) {
            return true;
        }

        if (myAction.hasDodge()) {
            LOGGER.debug("Pokemon {} dodged.", getName());
            return true;
        }

        if (enemyAction.isStun()) {
            LOGGER.debug("Pokemon {} was stunned.", getName());
            isStuned = true;
        }

        enemyAction.apply(this);

        return isAlive();
    }


    public Action getRandomAction() {
        updateAbilities();

        if (isStuned) {
            LOGGER.debug("Pokemon {} is stunned and cannot do an action", getName());
            isStuned = false;
            return new Action();
        }

        int action = new Random().nextInt() % 3;
        switch (action) {
            case 1:
                if (!pokemon.getFirstAbility().isInCoolDown()) {
                    LOGGER.debug("Pokemon {} used first ability: {}", getName(), pokemon.getFirstAbility());
                    return new Action(pokemon.getFirstAbility().useThis());
                }
            case 2:
                if (!pokemon.getSecondAbility().isInCoolDown()) {
                    LOGGER.debug("Pokemon {} used second ability: {}", getName(), pokemon.getSecondAbility());
                    return new Action(pokemon.getSecondAbility().useThis());
                }
            default:
                LOGGER.debug("Pokemon {} did a simple attack: {}", getName(), pokemon.getAttack());
                return new Action(pokemon.getAttack());
        }
    }

    public BattlePokemon updateWin() {
        wonBattles++;
        initCharacteristics(pokemon, items);
        pokemon.getFirstAbility().cooldownNow();
        pokemon.getSecondAbility().cooldownNow();
        return this;
    }

    public BattlePokemon updateLoose() {
        initCharacteristics(pokemon, items);
        pokemon.getFirstAbility().cooldownNow();
        pokemon.getSecondAbility().cooldownNow();
        return this;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void damageMe(int damage) {
        this.health = health - damage;
    }

    public void updateAbilities() {
        if (pokemon.getFirstAbility() != null) {
            pokemon.getFirstAbility().decrementLastUsed();
        }
        if (pokemon.getSecondAbility() != null) {
            pokemon.getSecondAbility().decrementLastUsed();
        }
    }

    public int getHealth() {
        return health;
    }

    public Attack getAttack() {
        return attack;
    }

    public Defense getDefense() {

        return defense;
    }

    public String getName() {
        return pokemon.getName();
    }


    @Override
    public String toString() {
        return "BattlePokemon{" +
                "name=" + getName() +
                ", health=" + health +
                ", attack=" + attack +
                ", defense=" + defense +
                ", isStuned=" + isStuned +
                ", pokemon=" + pokemon.toShortString() +
                ", items=" + items +
                '}';
    }

    public String getStatus() {
        return MessageFormat.format("{0}: hp={1}, stunned={2}, cd1={3}, cd2={4}",
                getName(), getHealth(), isStuned,
                pokemon.getFirstAbility().getLastUsedCountdown(), pokemon.getSecondAbility().getLastUsedCountdown());
    }

    public int sumAllStats() {
        return getHealth() +
                getAttack().getNormalAttack() + getAttack().getSpecialAttack() +
                getDefense().getNormalDefense() + getDefense().getSpecialDefense();
    }

    @Override
    public int compareTo(BattlePokemon other) {
        if (other == null) {
            return -1;
        }

        int statsDiff = sumAllStats() - other.sumAllStats();

        if (statsDiff == 0) {
            return getName().compareTo(other.getName());
        }

        return statsDiff;
    }
}
