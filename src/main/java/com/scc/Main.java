package com.scc;

import com.scc.dao.UserDao;
import com.scc.model.User;

import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final UserDao userDao = new UserDao();

    private static final String[] equipment = {
            "Basketball",
            "Volleyball",
            "Soccer Ball",
            "Tennis Racket",
            "Badminton Racket",
            "Table Tennis Paddle"
    };

    private static final double[] prices = {
            50.00, 50.00, 60.00, 40.00, 35.00, 30.00
    };

    public static void main(String[] args) {

        int choice;

        do {
            System.out.println("\n==================================");
            System.out.println("  SPORTS EQUIPMENT RENTAL SYSTEM");
            System.out.println("==================================");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            choice = readInt();

            switch (choice) {
                case 1:
                    register();
                    break;

                case 2:
                    login();
                    break;

                case 3:
                    System.out.println("Thank you for using the system!");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 3);

        sc.close();
    }

    private static void register() {

        System.out.println("\n--- USER REGISTRATION ---");

        System.out.print("Full Name: ");
        String fullName = sc.nextLine().trim();

        System.out.print("Username: ");
        String username = sc.nextLine().trim();

        System.out.print("Password: ");
        String password = sc.nextLine();

        System.out.print("Contact Number: ");
        String contactNumber = sc.nextLine().trim();

        System.out.print("Email: ");
        String email = sc.nextLine().trim();

        System.out.print("Address: ");
        String address = sc.nextLine().trim();

        if (fullName.isEmpty()
                || username.isEmpty()
                || password.isEmpty()
                || contactNumber.isEmpty()
                || email.isEmpty()
                || address.isEmpty()) {

            System.out.println("All fields are required!");
            return;
        }

        User user = new User(
                fullName,
                username,
                password,
                contactNumber,
                email,
                address
        );

        if (userDao.registerUser(user)) {
            System.out.println("Registration successful!");
        } else {
            System.out.println(
                    "Registration failed. Username may already exist."
            );
        }
    }

    private static void login() {

        System.out.println("\n--- USER LOGIN ---");

        System.out.print("Username: ");
        String username = sc.nextLine().trim();

        System.out.print("Password: ");
        String password = sc.nextLine();

        User user = userDao.loginUser(username, password);

        if (user != null) {
            System.out.println(
                    "\nWelcome, " + user.getFullName() + "!"
            );
            rentalMenu(user);
        } else {
            System.out.println(
                    "Invalid credentials or inactive account!"
            );
        }
    }

    private static void rentalMenu(User user) {

        int choice;

        do {
            System.out.println("\n--- RENTAL MENU ---");
            System.out.println("1. View Equipment");
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
                    rentEquipment(user);
                    break;

                case 3:
                    System.out.println("\n--- MY PROFILE ---");
                    System.out.println(user);
                    break;

                case 4:
                    System.out.println("Logout successful!");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 4);
    }

    private static void viewEquipment() {

        System.out.println("\n--- SPORTS EQUIPMENT ---");
        System.out.printf("%-5s %-25s %s%n",
                "No.", "Equipment", "Price/Day");

        for (int i = 0; i < equipment.length; i++) {
            System.out.printf(
                    "%-5d %-25s PHP %.2f%n",
                    i + 1, equipment[i], prices[i]
            );
        }
    }

    private static void rentEquipment(User user) {

        viewEquipment();

        System.out.print("\nEnter equipment number: ");
        int choice = readInt();

        if (choice < 1 || choice > equipment.length) {
            System.out.println("Invalid equipment selection!");
            return;
        }

        System.out.print("Number of rental days: ");
        int days = readInt();

        if (days <= 0) {
            System.out.println("Rental days must be greater than zero!");
            return;
        }

        int index = choice - 1;
        double total = prices[index] * days;

        System.out.println("\n--- RENTAL SUMMARY ---");
        System.out.println("Customer: " + user.getFullName());
        System.out.println("Equipment: " + equipment[index]);
        System.out.println("Rental Days: " + days);
        System.out.printf("Price per Day: PHP %.2f%n", prices[index]);
        System.out.printf("Total Amount: PHP %.2f%n", total);

        System.out.print("Confirm rental? (Y/N): ");
        String confirmation = sc.nextLine().trim();

        if (confirmation.equalsIgnoreCase("Y")) {
            System.out.println("Rental confirmed!");
            System.out.println("Please proceed to payment.");
        } else {
            System.out.println("Rental cancelled.");
        }
    }

    private static int readInt() {

        while (true) {
            String input = sc.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid number: ");
            }
        }
    }
}