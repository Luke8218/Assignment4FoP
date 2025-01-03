package com.lukecheskin;

import java.util.ArrayList;

import com.lukecheskin.classes.Collection;
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

        // TODO: Complete this
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