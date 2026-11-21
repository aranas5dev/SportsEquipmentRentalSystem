package com.scc.ui;

import com.scc.dao.UserDao;
import com.scc.dao.ActivityLogDao;
import com.scc.model.User;
import com.scc.model.ActivityLog;

import java.util.List;
import java.util.Scanner;

public class AdminMenu {

    private final Scanner scanner;
    private final UserDao userDao = new UserDao();
    private final ActivityLogDao logDao = new ActivityLogDao();

    private User currentAdmin;

    public AdminMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    public void show(User admin) {

        currentAdmin = admin;

        int choice;

        do {

            System.out.println("\n========== ADMIN MENU ==========");
            System.out.println("Signed in as: " + admin.getUsername());

            System.out.println("1. View All System Users");
            System.out.println("2. View Pending Registrations");
            System.out.println("3. Approve User Account");
            System.out.println("4. View System Audit Logs");
            System.out.println("5. Logout");

            System.out.print("Enter choice: ");

            choice = readIntOrCancel();

            switch (choice) {

                case 1:
                    printUsers(
                            userDao.getAllUsers(),
                            "ALL SYSTEM USERS"
                    );
                    break;

                case 2:
                    printUsers(
                            userDao.getPendingUsers(),
                            "PENDING REGISTRATIONS"
                    );
                    break;

                case 3:
                    approveAccount();
                    break;

                case 4:
                    auditLogMenu();
                    break;

                case 5:
                    System.out.println("Logging out...");
                    break;

                case 0:
                    System.out.println("Action cancelled.");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 5);
    }

    // APPROVE ACCOUNT
    private void approveAccount() {

        List<User> pending = userDao.getPendingUsers();

        if (pending.isEmpty()) {

            System.out.println(
                    "There are no pending registrations."
            );

            return;
        }

        printUsers(pending, "PENDING REGISTRATIONS");

        System.out.print("Enter Target User ID to approve (0 to cancel): ");

        int id = readIntOrCancel();

        if (id == 0) {
            System.out.println("Approval cancelled.");
            return;
        }

        boolean isPending = pending.stream()
                .anyMatch(user -> user.getUserId() == id);

        if (!isPending) {

            System.out.println(
                    "That ID is not in the pending registration list."
            );

            return;
        }

        System.out.print(
                "Approve user ID " + id + "? (Y/N): "
        );

        String confirm = scanner.nextLine().trim();

        if (!confirm.equalsIgnoreCase("Y")) {

            System.out.println("Approval cancelled.");
            return;
        }

        if (userDao.approveUser(id)) {

            logDao.logAction(
                    currentAdmin.getUserId(),
                    "USER_APPROVED",
                    "Admin ID " + currentAdmin.getUserId()
                            + " approved account for User ID " + id
            );

            System.out.println(
                    "User ID " + id + " approved successfully."
            );

        } else {

            System.out.println(
                    "Approval failed. The user may no longer be pending."
            );
        }
    }

    // AUDIT LOG MENU
    private void auditLogMenu() {

        int choice;

        do {

            System.out.println("\n========== SYSTEM AUDIT LOGS ==========");
            System.out.println("1. View All System Logs");
            System.out.println("2. Filter Logs by User ID");
            System.out.println("3. Clear Historical Logs");
            System.out.println("4. Back to Admin Menu");

            System.out.print("Enter choice: ");

            choice = readIntOrCancel();

            switch (choice) {

                case 1:
                    viewAllLogs();
                    break;

                case 2:
                    filterLogsByUser();
                    break;

                case 3:
                    clearHistoricalLogs();
                    break;

                case 4:
                    System.out.println("Returning to admin menu...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 4);
    }

    // VIEW ALL LOGS
    private void viewAllLogs() {

        List<ActivityLog> logs = logDao.getAllLogs();

        printLogs(logs, "ALL SYSTEM ACTIVITY LOGS");
    }

    // FILTER BY USER ID
    private void filterLogsByUser() {

        System.out.print("Enter User ID to filter: ");

        int userId = readIntOrCancel();

        if (userId <= 0) {

            System.out.println("Invalid User ID.");
            return;
        }

        List<ActivityLog> logs =
                logDao.getLogsByUserId(userId);

        printLogs(
                logs,
                "ACTIVITY LOGS FOR USER ID " + userId
        );
    }

    // DISPLAY LOGS IN TABLE FORMAT
    private void printLogs(
            List<ActivityLog> logs,
            String title) {

        System.out.println("\n========== " + title + " ==========");

        System.out.printf(
                "%-5s | %-8s | %-18s | %-35s | %-20s%n",
                "ID",
                "User ID",
                "Action Type",
                "Description",
                "Timestamp"
        );

        System.out.println(
                "---------------------------------------------------------------------------------------------------------------"
        );

        if (logs.isEmpty()) {

            System.out.println("No activity logs found.");

        } else {

            for (ActivityLog log : logs) {

                System.out.printf(
                        "%-5s | %-8s | %-18s | %-35s | %-20s%n",

                        log.getLogId(),

                        log.getUserId() == 0
                                ? "N/A"
                                : log.getUserId(),

                        clip(log.getActionType(), 18),

                        clip(log.getDescription(), 35),

                        clip(log.getTimestamp(), 20)
                );
            }
        }

        System.out.println(
                "---------------------------------------------------------------------------------------------------------------"
        );

        System.out.println("Total Records: " + logs.size());
    }

    // CLEAR LOGS SAFELY
    private void clearHistoricalLogs() {

        List<ActivityLog> logs = logDao.getAllLogs();

        if (logs.isEmpty()) {

            System.out.println("There are no logs to clear.");
            return;
        }

        System.out.println(
                "WARNING: This will permanently delete all historical logs."
        );

        System.out.println("Total logs: " + logs.size());

        System.out.print(
                "Are you sure you want to continue? (Y/N): "
        );

        String confirm = scanner.nextLine().trim();

        if (!confirm.equalsIgnoreCase("Y")) {

            System.out.println("Clear operation cancelled.");
            return;
        }

        System.out.print(
                "Type CLEAR to confirm deletion: "
        );

        String confirmation = scanner.nextLine().trim();

        if (!confirmation.equals("CLEAR")) {

            System.out.println("Clear operation cancelled.");
            return;
        }

        if (logDao.clearAllLogs()) {

            System.out.println(
                    "All historical activity logs have been cleared."
            );

        } else {

            System.out.println(
                    "Unable to clear historical logs."
            );
        }
    }

    // VIEW USERS
    private void printUsers(
            List<User> users,
            String title) {

        System.out.println("\n========== " + title + " ==========");

        String border =
                "+--------+-----------------+-----------------+--------------+--------------+";

        System.out.println(border);

        System.out.printf(
                "| %-6s | %-15s | %-15s | %-12s | %-12s |%n",
                "User ID",
                "Username",
                "Role",
                "Status",
                "Full Name"
        );

        System.out.println(border);

        if (users.isEmpty()) {

            System.out.println("No records found.");

        } else {

            for (User user : users) {

                System.out.printf(
                        "| %-6s | %-15s | %-15s | %-12s | %-12s |%n",

                        user.getUserId(),

                        clip(user.getUsername(), 15),

                        clip(user.getRole(), 15),

                        clip(user.getStatus(), 12),

                        clip(user.getFullName(), 12)
                );
            }
        }

        System.out.println(border);
    }

    private String clip(String value, int maxLength) {

        if (value == null) {
            return "";
        }

        return value.length() <= maxLength
                ? value
                : value.substring(0, maxLength);
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