package com.lukecheskin.classes;

import java.util.ArrayList;

/**
 * Represents a LEGO set with its associated properties and minifigures.
 * Contains information about the set's number, name, pieces, price, status, and theme.
 */
public class LegoSet {
    public String setNumber;
    public String name;
    public int pieces;
    public float price;
    public Status status;
    public String theme;
    public ArrayList<Minifigure> minifigures;

    /**
     * Creates a new LEGO set with the specified properties.
     * 
     * @param setNumber The unique identifier of the set
     * @param name The display name of the set
     * @param pieces The total number of pieces in the set
     * @param price The price of the set
     * @param status The build status of the set
     * @param theme The theme/category of the set
     */
    public LegoSet(String setNumber, String name, int pieces, float price, Status status, String theme) {
        this.setNumber = setNumber;
        this.name = name;
        this.pieces = pieces;
        this.price = price;
        this.status = status;
        this.theme = theme;
    }

    public ArrayList<Minifigure> getMinifigures() {
        return minifigures;
    }

    public void setMinifigures(ArrayList<Minifigure> minifigures) {
        this.minifigures = minifigures;
    }
}