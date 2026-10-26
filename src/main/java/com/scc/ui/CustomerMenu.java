package com.scc.ui;

import com.scc.model.User;
import java.util.Scanner;

public class CustomerMenu {

    private final Scanner scanner;
    private final User user;

    private final String[] equipment = {
            "Basketball",
            "Volleyball",
            "Soccer Ball",
            "Tennis Racket",
            "Badminton Racket",
            "Table Tennis Paddle"
    };

    private final double[] prices = {
            50.00, 50.00, 60.00, 40.00, 35.00, 30.00
    };

    public CustomerMenu(Scanner scanner, User user) {
        this.scanner = scanner;
        this.user = user;
    }

    public void show() {
        int choice;

        do {
            System.out.println("\n==================================");
            System.out.println("       CUSTOMER DASHBOARD");
            System.out.println("==================================");
            System.out.println("Welcome, " + user.getFullName() + "!");
            System.out.println("1. View Sports Equipment");
            System.out.println("2. Rent Equipment");
            System.out.println("3. View My Profile");
            System.out.println("4. Logout");
            System.out.print("Enter choice: ");

            choice = readInt();

            switch (choice) {
                case 1:
                    viewEquipment();
                    break;
                case 2:
                    rentEquipment();
                    break;
                case 3:
                    viewProfile();
                    break;
                case 4:
                    System.out.println("Logged out successfully!");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 4);
    }

    private void viewEquipment() {
        System.out.println("\n========== SPORTS EQUIPMENT ==========");
        System.out.printf("%-5s %-25s %s%n",
                "No.", "Equipment", "Price/Day");

        for (int i = 0; i < equipment.length; i++) {
            System.out.printf("%-5d %-25s PHP %.2f%n",
                    i + 1, equipment[i], prices[i]);
        }
    }

    private void rentEquipment() {
        viewEquipment();

        System.out.print("\nEnter equipment number (0 to cancel): ");
        int selected = readInt();

        if (selected == 0) {
            System.out.println("Rental cancelled.");
            return;
        }

        if (selected < 1 || selected > equipment.length) {
            System.out.println("Invalid equipment number!");
            return;
        }

        System.out.print("Enter number of rental days (0 to cancel): ");
        int days = readInt();

        if (days == 0) {
            System.out.println("Rental cancelled.");
            return;
        }

        if (days < 0) {
            System.out.println("Rental days must be positive!");
            return;
        }

        int index = selected - 1;
        double total = prices[index] * days;

        System.out.println("\n========== RENTAL SUMMARY ==========");
        System.out.println("Customer: " + user.getFullName());
        System.out.println("Equipment: " + equipment[index]);
        System.out.println("Rental days: " + days);
        System.out.printf("Price per day: PHP %.2f%n", prices[index]);
        System.out.printf("Total amount: PHP %.2f%n", total);

        System.out.print("Confirm rental? (Y/N): ");
        String confirm = scanner.nextLine().trim();

        if (confirm.equalsIgnoreCase("Y")) {
            System.out.println("Rental confirmed!");
            System.out.println(
                    "Note: This demonstration does not save rental transactions yet."
            );
        } else {
            System.out.println("Rental cancelled.");
        }
    }

    private void viewProfile() {
        System.out.println("\n========== MY PROFILE ==========");
        System.out.println("User ID: " + user.getUserId());
        System.out.println("Full Name: " + user.getFullName());
        System.out.println("Username: " + user.getUsername());
        System.out.println("Contact: " + user.getContactNumber());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Address: " + user.getAddress());
        System.out.println("Role: " + user.getRole());
        System.out.println("Status: " + user.getStatus());
    }

    private int readInt() {
        while (true) {
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid number: ");
            }
        }
    }
}