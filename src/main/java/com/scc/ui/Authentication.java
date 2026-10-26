package com.scc.ui;

import com.scc.dao.UserDao;
import com.scc.model.User;

import java.util.Scanner;

public class Authentication {
    private final Scanner scanner = new Scanner(System.in);
    private final UserDao userDao = new UserDao();

    public void start() {
        int choice;
        do {
            System.out.println("\n==================================");
            System.out.println("  SPORTS EQUIPMENT RENTAL SYSTEM");
            System.out.println("==================================");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            choice = readInt();

            switch (choice) {
                case 1 -> login();
                case 2 -> register();
                case 3 -> System.out.println("Goodbye!");
                case 0 -> System.out.println("Action cancelled.");
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 3);
    }

    private void login() {
        System.out.println("\n========== LOGIN ==========");

        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = userDao.authenticate(username, password);

        if (user == null) {
            System.out.println("Login unsuccessful.");
            return;
        }

        System.out.println(
                "Login successful. Welcome, " + user.getFullName() + "!"
        );

        String role = user.getRole() == null
                ? ""
                : user.getRole().trim();

        if (role.equalsIgnoreCase("Admin")) {
            new AdminMenu(scanner).show(user);

        } else if (role.equalsIgnoreCase("Customer")) {
            new CustomerMenu(scanner, user).show();

        } else if (role.equalsIgnoreCase("Staff")) {
            System.out.println(
                    "Staff login successful. Staff menu is not implemented yet."
            );

        } else {
            System.out.println("Unknown user role: " + role);
        }
    }

    private void register() {
        System.out.println("\n--- CUSTOMER REGISTRATION ---");
        System.out.print("Full name: ");
        String fullName = scanner.nextLine().trim();
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Contact number: ");
        String contact = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Address: ");
        String address = scanner.nextLine().trim();

        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()
                || contact.isEmpty() || email.isEmpty() || address.isEmpty()) {
            System.out.println("Registration cancelled: all fields are required.");
            return;
        }

        User user = new User(0, fullName, username, password,
                contact, email, address, "Customer", "PENDING");

        if (userDao.registerUser(user)) {
            System.out.println("Registration submitted. Your account is PENDING approval.");
        } else {
            System.out.println("Registration failed. Username may already exist.");
        }
    }

    private int readInt() {
        String value = scanner.nextLine().trim();
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
            return 0;
        }
    }
}
