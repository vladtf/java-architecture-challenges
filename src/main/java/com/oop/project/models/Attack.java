package com.oop.project.models;

public class Attack {
    private Integer normalAttack;
    private Integer specialAttack;


    private Attack(Integer normalAttack, Integer specialAttack) {
        this.normalAttack = normalAttack;
        this.specialAttack = specialAttack;
    }

    public Attack() {
    }

    public static Attack createAttack(Integer attack, Integer specialAttack) {
        return new Attack(attack, specialAttack);
    }

    public static Attack createAttack() {
        return new Attack(null, null);
    }


    public int getNormalAttack() {
        return normalAttack != null ? normalAttack : 0;
    }


    public int getSpecialAttack() {
        return specialAttack != null ? specialAttack : 0;
    }

    public Attack addAttack(Attack toAdd) {
        int normalAttack = 0;
        if (getNormalAttack() != 0) {
            normalAttack = getNormalAttack() + toAdd.getNormalAttack();
        }

        int specialAttack = 0;
        if (getSpecialAttack() != 0) {
            specialAttack = getSpecialAttack() + toAdd.getSpecialAttack();
        }

        return createAttack(normalAttack, specialAttack);
    }

    public int getNormalAttackDefended(Defense defense) {
        return getNormalAttack() > defense.getNormalDefense() ? getNormalAttack() - defense.getNormalDefense() : 0;
    }

    public int getSpecialAttackDefended(Defense defense) {
        return getSpecialAttack() > defense.getSpecialDefense() ? getSpecialAttack() - defense.getSpecialDefense() : 0;
    }

    public int getDamage(Defense defense) {
        return getNormalAttackDefended(defense) + getSpecialAttackDefended(defense);
    }

    public String toShortString() {
        return "{" +
                "normal=" + normalAttack +
                ", special=" + specialAttack +
                '}';

    }

    @Override
    public String toString() {
        return "Attack{" +
                "normalAttack=" + normalAttack +
                ", specialAttack=" + specialAttack +
                '}';
    }

}
