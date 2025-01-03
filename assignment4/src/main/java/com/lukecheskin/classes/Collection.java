package com.lukecheskin.classes;

import java.util.ArrayList;

/**
 * Represents a collection of LEGO sets.
 * Manages a list of sets and provides basic collection information.
 */
public class Collection {
    public String name;
    public ArrayList<LegoSet> sets;

    /**
     * Creates a new collection with the specified name.
     * Initializes an empty list of LEGO sets.
     * 
     * @param name The name of the collection
     */
    public Collection(String name) {
        this.name = name;
        this.sets = new ArrayList<LegoSet>();
    }
}
