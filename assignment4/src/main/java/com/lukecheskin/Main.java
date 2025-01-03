package com.lukecheskin;

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

    }

    private static void quickSearch() {

    }

    private static void exportToCSV() {
        
    }
}