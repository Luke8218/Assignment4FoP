package com.lukecheskin;

import java.util.ArrayList;

import com.lukecheskin.classes.Collection;
import com.lukecheskin.classes.LegoSet;
import com.lukecheskin.utils.FileManager;

public class Main {
    static FileManager fileManager = new FileManager();

    public static void main(String[] args) {

        System.out.println("----- Lego Set Collection Manager -----");
        System.out.println("                                       ");

        System.out.println("Options:");
        System.out.println("1) View collections");
        System.out.println("2) Quick search by set number");
        System.out.println("3) Export to CSV");
        System.out.print("\nSelect an option: ");
        
        String choice = System.console().readLine();
        switch (choice) {
            case "1" -> displayCollections();
            case "2" -> quickSearch();
            case "3" -> exportToCSV();
            default -> {
                System.out.println("Invalid option selected.\n");
                main(args);
            }
        }
    }

    private static void displayCollections() {
        ArrayList<Collection> collections = fileManager.getData();

        if (collections.isEmpty()) {
            startCreateCollectionWorkflow();
        } else {
            System.out.println("Collections found: " + collections.size());
            for (Collection collection : collections) {
                String displayIndex = String.valueOf(collections.indexOf(collection) + 1);
                System.out.println(displayIndex + ") " + collection.name + " (" + collection.sets.size() + " sets)");
            }
            System.out.println("\nOptions:");
            System.out.println("1) View/manage collection");
            System.out.println("2) Add new collection");
            System.out.println("3) Remove collection");
            System.out.println("4) Exit");
            System.out.print("\nSelect an option: ");
            String choice = System.console().readLine();

            switch (choice) {
                case "1" -> selectCollection();
                case "2" -> startCreateCollectionWorkflow();
                case "3" -> removeCollection();
                case "4" -> System.exit(0);
                default -> {
                    System.out.println("Invalid option selected.");
                    displayCollections();
                }
            }
        }
    }

    private static void selectCollection() {
        ArrayList<Collection> collections = fileManager.getData();
        System.out.print("\nEnter collection number: ");
        try {
            int index = Integer.parseInt(System.console().readLine()) - 1;
            if (index >= 0 && index < collections.size()) {
                manageCollection(collections.get(index));
            } else {
                System.out.println("Invalid collection number.");
                displayCollections();
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            displayCollections();
        }
    }

    private static void manageCollection(Collection collection) {
        System.out.println("\nManaging collection: " + collection.name);
        System.out.println("Sets in collection: " + collection.sets.size());

        if (!collection.sets.isEmpty()) {
            System.out.println("\nCurrent sets:");
            for (LegoSet set : collection.sets) {
                String displayIndex = String.valueOf(collection.sets.indexOf(set) + 1);
                System.out.println(displayIndex + ") " + set.name + " (#" + set.setNumber + ")");
            }
        }

        System.out.println("\nOptions:");
        System.out.println("1) Add new set");
        System.out.println("2) Remove set");
        System.out.println("3) View/edit set details");
        System.out.println("4) Filter sets by theme");
        System.out.println("5) Edit collection name");
        System.out.println("6) Back to collections");
        System.out.print("\nSelect an option: ");
        String choice = System.console().readLine();

        switch (choice) {
            case "1" -> addSetToCollection(collection);
            case "2" -> removeSetFromCollection(collection);
            case "3" -> viewSetDetails(collection);
            case "4" -> filterSetsByTheme(collection);
            case "5" -> editCollectionName(collection);
            case "6" -> displayCollections();
            default -> {
                System.out.println("Invalid option selected.");
                manageCollection(collection);
            }
        }
    }

    private static void addSetToCollection(Collection collection) {

    }

    private static void removeSetFromCollection(Collection collection) {

    }

    private static void viewSetDetails(Collection collection) {

    }

    private static void filterSetsByTheme(Collection collection) {

    }

    private static void editCollectionName(Collection collection) {
        
    }

    private static void startCreateCollectionWorkflow() {
        System.out.println("\nCreating a new collection..");
        System.out.print("Collection name: ");

        String collectionName = System.console().readLine();
        Collection collection = new Collection(collectionName);
        ArrayList<Collection> collections = fileManager.getData();

        collections.add(collection);
        fileManager.saveData(collections);
        
        System.out.println("Collection created: " + collection.name);
        System.out.println();

        displayCollections();
    }

    private static void removeCollection() {

    }

    private static void quickSearch() {

    }

    private static void exportToCSV() {
        
    }
}