package com.lukecheskin;

import java.util.ArrayList;

import com.google.gson.JsonObject;
import com.lukecheskin.classes.Collection;
import com.lukecheskin.classes.LegoSet;
import com.lukecheskin.classes.Status;
import com.lukecheskin.utils.FileManager;
import com.lukecheskin.utils.RebrickableClient;

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
        System.out.println("\nAdding new set to " + collection.name);
        
        System.out.print("Set number: ");
        String setNumber = System.console().readLine();
        
        String setName;
        int pieces;
        String theme;
        
        // Try to fetch set info from Rebrickable
        try {
            RebrickableClient client = new RebrickableClient();
            JsonObject setInfo = client.getSetInfo(setNumber);
            
            setName = setInfo.get("name").getAsString();
            pieces = setInfo.get("num_parts").getAsInt();
            int themeId = setInfo.get("theme_id").getAsInt();
            
            // Fetch theme information
            JsonObject themeInfo = client.getThemeInfo(themeId);
            theme = themeInfo.get("name").getAsString();
            
            System.out.println("\nFound set information:");
            System.out.println("Name: " + setName);
            System.out.println("Pieces: " + pieces);
            System.out.println("Theme: " + theme);
            System.out.print("Use this information? (Y/N): ");
            
            String useInfo = System.console().readLine();
            if (!useInfo.equalsIgnoreCase("Y")) {
                System.out.print("Set name: ");
                setName = System.console().readLine();
                
                System.out.print("Number of pieces: ");
                pieces = Integer.parseInt(System.console().readLine());
                
                System.out.print("Theme: ");
                theme = System.console().readLine();
            }
        } catch (Exception e) {
            System.out.println("\nCould not fetch set information. Please enter manually.");
            
            System.out.print("Set name: ");
            setName = System.console().readLine();
            
            System.out.print("Number of pieces: ");
            pieces = Integer.parseInt(System.console().readLine());

            System.out.print("Theme: ");
            theme = System.console().readLine();
        }
        
        System.out.print("Price: ");
        float price = Float.parseFloat(System.console().readLine());
        
        System.out.println("\nSet status:");
        System.out.println("1) NOT STARTED");
        System.out.println("2) IN PROGRESS");
        System.out.println("3) COMPLETED");
        System.out.print("Select status: ");
        String statusChoice = System.console().readLine();
        Status status;
        status = switch (statusChoice) {
            case "1" -> Status.NOT_STARTED;
            case "2" -> Status.IN_PROGRESS;
            case "3" -> Status.COMPLETED;
            default -> Status.NOT_STARTED;
        };

        LegoSet newSet = new LegoSet(setNumber, setName, pieces, price, status, theme);
        
        System.out.print("\nWould you like to add minifigures to this set? (y/n): ");
        String addMinifigs = System.console().readLine().toLowerCase();
        if (addMinifigs.equals("y")) {
            addMinifiguresToSet(newSet);
        }

        collection.sets.add(newSet);
        
        ArrayList<Collection> collections = fileManager.getData();
        for (Collection c : collections) {
            if (c.name.equals(collection.name)) {
                c.sets = collection.sets;
                break;
            }
        }
        fileManager.saveData(collections);
        
        System.out.println("Set added successfully!");
        manageCollection(collection);
    }

    private static void addMinifiguresToSet(LegoSet set) {
        
    }

    private static void removeSetFromCollection(Collection collection) {
        if (collection.sets.isEmpty()) {
            System.out.println("No sets to remove.");
            manageCollection(collection);
            return;
        }

        System.out.print("\nEnter set number to remove: ");
        try {
            int index = Integer.parseInt(System.console().readLine()) - 1;
            if (index >= 0 && index < collection.sets.size()) {
                LegoSet removedSet = collection.sets.remove(index);
                
                ArrayList<Collection> collections = fileManager.getData();
                for (Collection c : collections) {
                    if (c.name.equals(collection.name)) {
                        c.sets = collection.sets;
                        break;
                    }
                }
                fileManager.saveData(collections);
                
                System.out.println("Set '" + removedSet.name + "' removed successfully!");
            } else {
                System.out.println("Invalid set number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
        manageCollection(collection);
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
        ArrayList<Collection> collections = fileManager.getData();
        System.out.print("\nEnter collection number to remove: ");

        try {
            int index = Integer.parseInt(System.console().readLine()) - 1;

            if (index >= 0 && index < collections.size()) {
                String removedName = collections.get(index).name;
                collections.remove(index);
                fileManager.saveData(collections);
                System.out.println("Collection '" + removedName + "' removed successfully.");
            } else {
                System.out.println("Invalid collection number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
        
        System.out.println();
        displayCollections();
    }

    private static void quickSearch() {

    }

    private static void exportToCSV() {
        
    }
}