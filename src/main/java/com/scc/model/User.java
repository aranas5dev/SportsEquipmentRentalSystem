package com.scc.model;

public class User {

    private int userId;
    private String fullName;
    private String username;
    private String password;
    private String contactNumber;
    private String email;
    private String address;
    private String role;
    private String status;

    public User() {
    }

    public User(String fullName, String username,
                String password, String contactNumber,
                String email, String address) {

        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.contactNumber = contactNumber;
        this.email = email;
        this.address = address;
        this.role = "Customer";
        this.status = "Active";
    }

    public User(int userId, String fullName,
                String username, String password,
                String contactNumber, String email,
                String address, String role, String status) {

        this.userId = userId;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.contactNumber = contactNumber;
        this.email = email;
        this.address = address;
        this.role = role;
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User ID: " + userId
                + "\nFull Name: " + fullName
                + "\nUsername: " + username
                + "\nContact Number: " + contactNumber
                + "\nEmail: " + email
                + "\nAddress: " + address
                + "\nRole: " + role
                + "\nStatus: " + status;
    }
}