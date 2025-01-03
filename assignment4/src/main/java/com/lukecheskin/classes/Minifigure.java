package com.lukecheskin.classes;

/**
 * Represents a LEGO minifigure with basic descriptive information.
 * Contains the minifigure's name and description.
 */
public class Minifigure {
    public String name;
    public String description;

    /**
     * Creates a new minifigure with the specified details.
     * 
     * @param name The name of the minifigure
     * @param description A description of the minifigure
     */
    public Minifigure(String name, String description) {
        this.name = name;
        this.description = description;
    }
}