package dao;

import java.sql.*;
import db.DBConnection;
import model.User;

public class UserDAO {

    // ================= REGISTER ACCOUNT =================
    // MUST be lowercase 'register' and return a 'boolean' to match AuthController
    public boolean register(User u) throws Exception {
        String sql = "INSERT INTO users(name, email, password, role, matricNo) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getName());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPassword()); 
            ps.setString(4, u.getRole());
            ps.setString(5, u.getMatricNo()); 

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Returns true if insertion succeeded
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Returns false if it fails (like duplicate email)
        }
    }

    // ================= VALIDATE LOGIN =================
    public User login(String email, String password) throws Exception {
        String sql = "SELECT * FROM users WHERE email=? AND password=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setUserId(rs.getInt("userId"));
                    u.setName(rs.getString("name"));
                    u.setEmail(rs.getString("email"));
                    u.setRole(rs.getString("role"));
                    u.setMatricNo(rs.getString("matricNo"));
                    return u;
                }
            }
        }
        return null;
    }
}