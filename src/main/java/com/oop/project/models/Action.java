package com.oop.project.models;

public class Action {
    private Attack attack;
    private Ability ability;

    public Action(Attack attack) {
        this.attack = attack;
    }

    public Action(Ability ability) {
        this.ability = ability;
    }

    public Action() {
    }

    public void apply(BattlePokemon pokemon) {
        int damage = 0;

        if (attack != null) {
            damage = attack.getDamage(pokemon.getDefense());
        } else if (ability != null) {
            damage = ability.getDamage();
        }

        pokemon.damageMe(damage);
    }

    public boolean hasDodge() {
        return ability != null && ability.getDodge();
    }

    public boolean isStun() {
        return ability != null && ability.getStun();
    }

    @Override
    public String toString() {
        return "Action{" +
                "attack=" + attack +
                ", ability=" + ability +
                '}';
    }

    public String toShortString() {
        return "{" +
                "attack=" + (attack != null ? attack.toShortString() : "null") +
                ", ability=" + (ability != null ? ability.toShortString() : "null") +
                '}';
    }
}
