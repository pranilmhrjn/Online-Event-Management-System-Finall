package model;

import java.sql.*;
import java.util.*;

public class UserModel {

    public boolean register(String username,
            String email, String password,
            String role) {
        try {
            Connection conn =
                DatabaseConnection.getConnection();
            PreparedStatement pst =
                conn.prepareStatement(
                "INSERT INTO users (username, " +
                "email, password, role) " +
                "VALUES (?, ?, ?, ?)");
            pst.setString(1, username);
            pst.setString(2, email);
            pst.setString(3, password);
            pst.setString(4, role);
            pst.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(
                "Register error: " +
                e.getMessage());
            return false;
        }
    }

    public String login(String username,
            String password, String role) {
        try {
            Connection conn =
                DatabaseConnection.getConnection();
            PreparedStatement pst =
                conn.prepareStatement(
                "SELECT * FROM users WHERE " +
                "username=? AND password=? " +
                "AND role=?");
            pst.setString(1, username);
            pst.setString(2, password);
            pst.setString(3, role);
            ResultSet rs = pst.executeQuery();
            if (rs.next())
                return rs.getString("role");
        } catch (Exception e) {
            System.out.println(
                "Login error: " + e.getMessage());
        }
        return null;
    }

    public int getUserId(String username) {
        try {
            Connection conn =
                DatabaseConnection.getConnection();
            PreparedStatement pst =
                conn.prepareStatement(
                "SELECT id FROM users " +
                "WHERE username=?");
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();
            if (rs.next())
                return rs.getInt("id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<String[]> getAllUsers() {
        List<String[]> list = new ArrayList<>();
        try {
            Connection conn =
                DatabaseConnection.getConnection();
            ResultSet rs = conn.prepareStatement(
                "SELECT id, username, " +
                "email, role FROM users"
            ).executeQuery();
            while (rs.next()) {
                list.add(new String[]{
                    String.valueOf(
                        rs.getInt("id")),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("role")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateProfile(String username,
            String email, String phone,
            String bio) {
        try {
            Connection conn =
                DatabaseConnection.getConnection();
            PreparedStatement pst =
                conn.prepareStatement(
                "UPDATE users SET email=?, " +
                "phone=?, bio=? " +
                "WHERE username=?");
            pst.setString(1, email);
            pst.setString(2, phone);
            pst.setString(3, bio);
            pst.setString(4, username);
            pst.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(
                "Update error: " +
                e.getMessage());
            return false;
        }
    }

    public String[] getUserProfile(
            String username) {
        try {
            Connection conn =
                DatabaseConnection.getConnection();
            PreparedStatement pst =
                conn.prepareStatement(
                "SELECT id, username, email, " +
                "role, phone, bio " +
                "FROM users WHERE username=?");
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new String[]{
                    String.valueOf(
                        rs.getInt("id")),
                    safe(rs, "username"),
                    safe(rs, "email"),
                    safe(rs, "role"),
                    safe(rs, "phone"),
                    safe(rs, "bio")
                };
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean changePassword(
            String username,
            String oldPassword,
            String newPassword) {
        try {
            Connection conn =
                DatabaseConnection.getConnection();
            PreparedStatement check =
                conn.prepareStatement(
                "SELECT id FROM users WHERE " +
                "username=? AND password=?");
            check.setString(1, username);
            check.setString(2, oldPassword);
            ResultSet rs = check.executeQuery();
            if (!rs.next()) return false;

            PreparedStatement pst =
                conn.prepareStatement(
                "UPDATE users SET password=? " +
                "WHERE username=?");
            pst.setString(1, newPassword);
            pst.setString(2, username);
            pst.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(
                "Password error: " +
                e.getMessage());
            return false;
        }
    }

    private String safe(
            ResultSet rs, String col) {
        try {
            String v = rs.getString(col);
            return v != null ? v : "";
        } catch (Exception e) {
            return "";
        }
    }
}