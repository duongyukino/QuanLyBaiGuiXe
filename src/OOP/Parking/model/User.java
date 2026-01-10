package OOP.Parking.model;

public class User {
    private String username;
    private String password;
    private String role; // "ADMIN", "MANAGER", "STAFF"
    private String fullName;
    private boolean active;
    private String email;

    public User(String username, String password, String role, String fullName, boolean active, String email) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
        this.active = active;
        this.email = email;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean authenticate(String inputPassword) {
        return this.password.equals(inputPassword);
    }
}