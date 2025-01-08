package com.lukecheskin;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.google.gson.JsonObject;
import com.lukecheskin.classes.Collection;
import com.lukecheskin.classes.LegoSet;
import com.lukecheskin.classes.Minifigure;
import com.lukecheskin.classes.Status;
import com.lukecheskin.utils.FileManager;
import com.lukecheskin.utils.RebrickableClient;

public class Main {
    static FileManager fileManager = new FileManager();

    /**
     * Main entry point for the LEGO Set Collection Manager application.
     * Displays the main menu and handles user input for navigation between different features.
     */
    public static void main(String[] args) {

        System.out.println("----- LEGO Set Collection Manager -----");
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

    /**
     * Displays all available LEGO collections and provides options for managing them.
     * If no collections exist, initiates the collection creation workflow.
     * Allows users to view, add, or remove collections.
     */
    private static void displayCollections() {
        ArrayList<Collection> collections = fileManager.getData();

        if (collections.isEmpty()) {
            System.out.println("The data file is empty. Please add some collections first.");
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

    /**
     * Handles the selection of a specific collection from the list.
     * Validates user input and redirects to collection management if valid.
     */
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

    /**
     * Displays and manages operations for a specific LEGO collection.
     * Provides options to add, remove, view, and filter sets within the collection.
     * 
     * @param collection The Collection object to manage
     */
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

    /**
     * Handles the process of adding a new LEGO set to a collection.
     * Prompts for set details including number, name, pieces, price, theme, and status.
     * Optionally allows adding minifigures to the set.
     * 
     * @param collection The Collection to add the new set to
     */
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

    /**
     * Adds minifigures to a LEGO set through an interactive prompt.
     * Allows multiple minifigures to be added until the user chooses to stop.
     * 
     * @param set The LegoSet to add minifigures to
     */
    private static void addMinifiguresToSet(LegoSet set) {
        ArrayList<Minifigure> minifigures = new ArrayList<>();
        boolean addMore = true;
        
        while (addMore) {
            System.out.print("\nMinifigure name: ");
            String name = System.console().readLine();
            
            System.out.print("Minifigure description: ");
            String description = System.console().readLine();
            
            minifigures.add(new Minifigure(name, description));
            
            System.out.print("Add another minifigure? (y/n): ");
            addMore = System.console().readLine().toLowerCase().equals("y");
        }
        
        set.setMinifigures(minifigures);
    }

    /**
     * Removes a specified LEGO set from a collection.
     * Updates the persistent storage after removal.
     * 
     * @param collection The Collection to remove the set from
     */
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

    /**
     * Displays detailed information about a specific LEGO set in the collection.
     * Shows set number, name, theme, pieces, price, status, and any minifigures.
     * 
     * @param collection The Collection containing the set to view
     */
    private static void viewSetDetails(Collection collection) {
        System.out.print("\nEnter set number (1-" + collection.sets.size() + "): ");
        try {
            int setIndex = Integer.parseInt(System.console().readLine()) - 1;
            if (setIndex >= 0 && setIndex < collection.sets.size()) {
                LegoSet set = collection.sets.get(setIndex);
                System.out.println("\nSet Details:");
                System.out.println("Set Number: " + set.setNumber);
                System.out.println("Name: " + set.name);
                System.out.println("Theme: " + set.theme);
                System.out.println("Pieces: " + set.pieces);
                System.out.println("Price: £" + set.price);
                System.out.println("Status: " + set.status);

                if (set.minifigures != null && !set.minifigures.isEmpty()) {
                    System.out.println("Minifigures:");
                    for (Minifigure fig : set.minifigures) {
                        System.out.println("- " + fig.name + " (" + fig.description + ")");
                    }
                }
                
                System.out.println("\nOptions:");
                System.out.println("1) Edit set details");
                System.out.println("2) Back to collection");
                System.out.print("\nSelect an option: ");
                
                String choice = System.console().readLine();
                switch (choice) {
                    case "1" -> editSetDetails(collection, set);
                    case "2" -> manageCollection(collection);
                    default -> {
                        System.out.println("Invalid option selected.");
                        viewSetDetails(collection);
                    }
                }
            } else {
                System.out.println("Invalid set number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
        
        System.out.print("\nPress Enter to continue...");
        System.console().readLine();
        manageCollection(collection);
    }

    /**
     * Edits the details of a specific LEGO set within a collection.
     * Allows editing name, pieces, price, status, and theme.
     *
     * @param collection The Collection containing the set to edit
     * @param set The LegoSet to edit
     */
    private static void editSetDetails(Collection collection, LegoSet set) {
        while (true) {
            System.out.println("\nEdit Set Details:");
            System.out.println("1) Edit name");
            System.out.println("2) Edit pieces");
            System.out.println("3) Edit price");
            System.out.println("4) Edit status");
            System.out.println("5) Edit theme");
            System.out.println("6) Back to set details");
            System.out.print("\nSelect an option: ");
            
            String choice = System.console().readLine();
            
            switch (choice) {
                case "1" -> {
                    System.out.print("Enter new name: ");
                    String newName = System.console().readLine();
                    set.name = newName;
                }
                case "2" -> {
                    System.out.print("Enter new piece count: ");
                    try {
                        int newPieces = Integer.parseInt(System.console().readLine());
                        set.pieces = newPieces;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number format. Please try again.");
                        continue;
                    }
                }
                case "3" -> {
                    System.out.print("Enter new price: ");
                    try {
                        float newPrice = Float.parseFloat(System.console().readLine());
                        set.price = newPrice;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid price format. Please try again.");
                        continue;
                    }
                }
                case "4" -> {
                    System.out.println("Available statuses:");
                    for (Status status : Status.values()) {
                        System.out.println("- " + status);
                    }
                    System.out.print("Enter new status: ");
                    try {
                        Status newStatus = Status.valueOf(System.console().readLine().toUpperCase());
                        set.status = newStatus;
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid status. Please try again.");
                        continue;
                    }
                }
                case "5" -> {
                    System.out.print("Enter new theme: ");
                    String newTheme = System.console().readLine();
                    set.theme = newTheme;
                }
                case "6" -> {
                    ArrayList<Collection> collections = fileManager.getData();
                    int index = -1;
                    for (int i = 0; i < collections.size(); i++) {
                        if (collections.get(i).name.equals(collection.name)) {
                            index = i;
                            break;
                        }
                    }
                    if (index != -1) {
                        collections.set(index, collection);
                        fileManager.saveData(collections);
                    }
                    viewSetDetails(collection);
                    return;
                }
                default -> {
                    System.out.println("Invalid option selected.");
                    continue;
                }
            }
            
            ArrayList<Collection> collections = fileManager.getData();
            int index = -1;
            for (int i = 0; i < collections.size(); i++) {
                if (collections.get(i).name.equals(collection.name)) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                collections.set(index, collection);
                fileManager.saveData(collections);
            }
            System.out.println("Set updated successfully.");
        }
    }

    /**
     * Filters and displays sets in a collection that match a specified theme.
     * The search is case-insensitive and supports partial matches.
     *
     * @param collection The Collection to filter sets from
     */
    private static void filterSetsByTheme(Collection collection) {
        System.out.print("\nEnter theme to filter by: ");
        String theme = System.console().readLine();
        
        System.out.println("\nSets matching theme '" + theme + "':");
        boolean found = false;
        
        for (LegoSet set : collection.sets) {
            if (set.theme.toLowerCase().contains(theme.toLowerCase())) {
                String displayIndex = String.valueOf(collection.sets.indexOf(set) + 1);
                System.out.println(displayIndex + ") " + set.name + " (#" + set.setNumber + ") (" + set.theme + ")");
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("No sets found with the specified theme.");
        }
        
        System.out.print("\nPress Enter to continue...");
        System.console().readLine();
        manageCollection(collection);
    }

    /**
     * Displays a prompt to the user asking for the new name for a collection.
     * Updates the collection name in the persistent storage.
     *
     * @param collection The Collection to manage
     */
    private static void editCollectionName(Collection collection) {
        System.out.println("\nCurrent collection name: " + collection.name);
        System.out.print("Enter new name: ");
        String newName = System.console().readLine();
        
        
        ArrayList<Collection> collections = fileManager.getData();
        int index = -1;
        for (int i = 0; i < collections.size(); i++) {
            if (collections.get(i).name.equals(collection.name)) {
                collection.name = newName;
                index = i;
                break;
            }
        }
        if (index != -1) {
            collections.set(index, collection);
            fileManager.saveData(collections);
            System.out.println("Collection name updated successfully.");
        }
        manageCollection(collection);
    }

    /**
     * Initiates the workflow for creating a new collection.
     * Prompts for collection name and saves it to persistent storage.
     */
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

    /**
     * Removes a specified collection from the system.
     * Updates the persistent storage after removal.
     */
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

    /**
     * Performs a quick search across all collections for a specific set number.
     * Displays which collection contains the matching set if found.
     */
    private static void quickSearch() {
        ArrayList<Collection> collections = fileManager.getData();
        
        if (collections.isEmpty()) {
            System.out.println("The data file is empty. Please add some collections first.");
            System.out.print("\nPress Enter to continue...");
            System.console().readLine();
            main(null);
            return;
        }
        
        System.out.print("\nEnter set number to search for: ");
        String setNumber = System.console().readLine();
        boolean found = false;
        
        System.out.println("\nSearch results:");
        for (Collection collection : collections) {
            for (LegoSet set : collection.sets) {
                if (set.setNumber.equals(setNumber)) {
                    System.out.println("Found in collection '" + collection.name + "': " + set.name);
                    found = true;
                }
            }
        }
        
        if (!found) {
            System.out.println("No sets found with set number: " + setNumber);
        }
        
        System.out.print("\nPress Enter to continue...");
        System.console().readLine();
        main(null);
    }

    /**
     * Exports all collection data to a CSV file named 'lego_collections.csv'.
     * Includes collection name, set details, and minifigure information if present.
     * Each minifigure creates a new row with the same set details.
     */
    private static void exportToCSV() {
        ArrayList<Collection> collections = fileManager.getData();
        
        if (collections.isEmpty()) {
            System.out.println("The data file is empty. Please add some collections first.");
            main(null);
            return;
        }
        
        String csvContent = "Collection,Set Number,Name,Theme,Pieces,Price,Status,Minifigure Name,Minifigure Description\n";
        
        for (Collection collection : collections) {
            for (LegoSet set : collection.sets) {
                if (set.minifigures != null && !set.minifigures.isEmpty()) {
                    for (Minifigure fig : set.minifigures) {
                        csvContent += String.format("\"%s\",\"%s\",\"%s\",\"%s\",%d,%.2f,\"%s\",\"%s\",\"%s\"\n",
                            collection.name, set.setNumber, set.name, set.theme, set.pieces, 
                            set.price, set.status, fig.name, fig.description);
                    }
                } else {
                    csvContent += String.format("\"%s\",\"%s\",\"%s\",\"%s\",%d,%.2f,\"%s\",,\n",
                        collection.name, set.setNumber, set.name, set.theme, set.pieces, 
                        set.price, set.status);
                }
            }
        }
        
        try {
            Files.write(Paths.get("lego_collections.csv"), csvContent.getBytes());
            System.out.println("\nData successfully exported to lego_collections.csv");
        } catch (Exception e) {
            System.out.println("\nError exporting to CSV: " + e.getLocalizedMessage());
        }
        
        System.out.print("\nPress Enter to continue...");
        System.console().readLine();
        main(null);
    }
}