package com.scc.ui;

import com.scc.dao.UserDao;
import com.scc.model.User;

import java.util.List;
import java.util.Scanner;

public class AdminMenu {
    private final Scanner scanner;
    private final UserDao userDao = new UserDao();

    public AdminMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    public void show(User admin) {
        int choice;
        do {
            System.out.println("\n========== ADMIN MENU ==========");
            System.out.println("Signed in as: " + admin.getUsername());
            System.out.println("1. View All System Users");
            System.out.println("2. View Pending Registrations");
            System.out.println("3. Approve User Account");
            System.out.println("4. Logout");
            System.out.print("Enter choice: ");

            choice = readIntOrCancel();

            switch (choice) {
                case 1 -> printUsers(userDao.getAllUsers(), "ALL SYSTEM USERS");
                case 2 -> printUsers(userDao.getPendingUsers(), "PENDING REGISTRATIONS");
                case 3 -> approveAccount();
                case 4 -> System.out.println("Logging out...");
                case 0 -> System.out.println("Action cancelled.");
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 4);
    }

    private void approveAccount() {
        List<User> pending = userDao.getPendingUsers();
        if (pending.isEmpty()) {
            System.out.println("There are no pending registrations.");
            return;
        }

        printUsers(pending, "PENDING REGISTRATIONS");
        System.out.print("Enter Target User ID to approve (0 to cancel): ");
        int id = readIntOrCancel();

        if (id == 0) {
            System.out.println("Approval cancelled.");
            return;
        }

        boolean isPending = pending.stream().anyMatch(user -> user.getUserId() == id);
        if (!isPending) {
            System.out.println("That ID is not in the pending registration list.");
            return;
        }

        System.out.print("Approve user ID " + id + "? (Y/N): ");
        String confirm = scanner.nextLine().trim();

        if (!confirm.equalsIgnoreCase("Y")) {
            System.out.println("Approval cancelled.");
            return;
        }

        if (userDao.approveUser(id)) {
            System.out.println("User ID " + id + " approved successfully.");
        } else {
            System.out.println("Approval failed. The user may no longer be pending.");
        }
    }

    private void printUsers(List<User> users, String title) {
        System.out.println("\n========== " + title + " ==========");
        String border = "+--------+-----------------+-----------------+--------------+--------------+";
        System.out.println(border);
        System.out.printf("| %-6s | %-15s | %-15s | %-12s | %-12s |%n",
                "User ID", "Username", "Role", "Status", "Full Name");
        System.out.println(border);

        if (users.isEmpty()) {
            System.out.printf("| %-66s |%n", "No records found.");
        } else {
            for (User user : users) {
                System.out.printf("| %-6s | %-15s | %-15s | %-12s | %-12s |%n",
                        clip(String.valueOf(user.getUserId()), 6),
                        clip(user.getUsername(), 15),
                        clip(user.getRole(), 15),
                        clip(user.getStatus(), 12),
                        clip(user.getFullName(), 12));
            }
        }
        System.out.println(border);
    }

    private String clip(String value, int maxLength) {
        if (value == null) return "";
        return value.length() <= maxLength ? value : value.substring(0, maxLength);
    }

    private int readIntOrCancel() {
        String value = scanner.nextLine().trim();
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
            return -1;
        }
    }
}
