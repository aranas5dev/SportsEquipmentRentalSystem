package com.scc.dao;

import com.scc.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    public boolean registerUser(User user) {

        String sql = "INSERT INTO users "
                + "(full_name, username, password, "
                + "contact_number, email, address, role, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn =
                     DatabaseConnection.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getContactNumber());
            stmt.setString(5, user.getEmail());
            stmt.setString(6, user.getAddress());
            stmt.setString(7, user.getRole());
            stmt.setString(8, user.getStatus());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println(
                    "Registration error: " + e.getMessage()
            );
            return false;
        }
    }

    public User loginUser(String username, String password) {

        String sql = "SELECT * FROM users "
                + "WHERE username = ? AND password = ?";

        try (Connection conn =
                     DatabaseConnection.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    User user = new User(
                            rs.getInt("user_id"),
                            rs.getString("full_name"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("contact_number"),
                            rs.getString("email"),
                            rs.getString("address"),
                            rs.getString("role"),
                            rs.getString("status")
                    );

                    if ("Active".equalsIgnoreCase(
                            user.getStatus())) {
                        return user;
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Login error: " + e.getMessage()
            );
        }

        return null;
    }

    public User getUserById(int userId) {

        String sql = "SELECT * FROM users WHERE user_id = ?";

        try (Connection conn =
                     DatabaseConnection.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("full_name"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("contact_number"),
                            rs.getString("email"),
                            rs.getString("address"),
                            rs.getString("role"),
                            rs.getString("status")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Retrieve user error: " + e.getMessage()
            );
        }

        return null;
    }
}