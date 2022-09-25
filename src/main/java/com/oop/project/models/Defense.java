package com.oop.project.models;


import com.fasterxml.jackson.annotation.JsonProperty;

public class Defense implements Model {

    @JsonProperty("normalDefense")
    private Integer normalDefense;

    @JsonProperty("specialDefense")
    private Integer specialDefense;

    private Defense(Integer normalDefense, Integer specialDefense) {
        this.normalDefense = normalDefense;
        this.specialDefense = specialDefense;
    }

    public Defense() {
    }

    public static Defense createDefense(Integer defense, Integer specialDefense) {
        return new Defense(defense, specialDefense);
    }

    public static Defense createDefense() {
        return new Defense(null, null);
    }

    @Override
    public String toString() {
        return "Defense{" +
                "normalDefense=" + normalDefense +
                ", specialDefense=" + specialDefense +
                '}';
    }

    public int getNormalDefense() {
        return normalDefense != null ? normalDefense : 0;
    }


    public int getSpecialDefense() {
        return specialDefense != null ? specialDefense : 0;
    }

    @Override
    public String getName() {
        return null;
    }

    public Defense addDefense(Defense toAdd) {
        int normalDefense = getNormalDefense() + toAdd.getNormalDefense();
        int specialDefense = getSpecialDefense() + toAdd.getSpecialDefense();

        return createDefense(normalDefense, specialDefense);
    }
}
