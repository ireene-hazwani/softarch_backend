package model;

/**
 * Model component representing a User entity in the SMARTLEARN system.
 * This class acts as a JavaBean (POJO) to transport structural account parameters
 * across presentation views, authentication controllers, and data layers.
 */
public class User {

    private int userId;
    private String name;
    private String email;
    private String password;
    private String role;     // admin, lecturer, student
    private String matricNo; // Explicit property for tracking Student metrics

    // ================= CONSTRUCTORS =================

    /**
     * Default No-Args Constructor.
     * Crucial for JSP actions (<jsp:useBean>) and dynamic framework model instantiations.
     */
    public User() {}

    /**
     * Parameterized Constructor (Full Schema Model Injection).
     * Used when fetching existing users out of relational database table result sets.
     */
    public User(int userId, String name, String email, String password, String role, String matricNo) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.matricNo = matricNo;
    }

    // ================= GETTERS & SETTERS =================

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMatricNo() {
        return matricNo;
    }

    public void setMatricNo(String matricNo) {
        this.matricNo = matricNo;
    }
}