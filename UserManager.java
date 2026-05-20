import java.io.*;
import java.util.*;

public class UserManager {
    //Saving users in a simple text file 
    private static final String USERS_FILE = "data/users.txt";

    // Format in txt: username|password|email|fullname
    
    public UserManager() {
        initializeFile();
    }

    // Create file + default admin account if file doesn't exist
    private void initializeFile() {
        File file = new File(USERS_FILE);
        if (!file.exists()) {
            file.getParentFile().mkdirs(); // create data/ folder if missing
            saveUser("admin", "uthm123", "admin@uthm.edu.my", "Administrator");
        }
    }

    // Save new user to txt file
    public boolean saveUser(String username, String password, String email, String fullName) {
        // Check duplicate username first
        if (userExists(username)) {
            return false;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            writer.write(username + "|" + password + "|" + email + "|" + fullName);
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.out.println("Error saving user: " + e.getMessage());
            return false;
        }
    }

    // Verify login credentials
    public boolean verifyLogin(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 2) {
                    String savedUsername = parts[0].trim();
                    String savedPassword = parts[1].trim();
                    if (savedUsername.equals(username) && savedPassword.equals(password)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading users file: " + e.getMessage());
        }
        return false;
    }

    // Check if username already taken
    public boolean userExists(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 1 && parts[0].trim().equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading users file: " + e.getMessage());
        }
        return false;
    }

    // Get full name by username (useful to display "Welcome, John!")
    public String getFullName(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 4 && parts[0].trim().equals(username)) {
                    return parts[3].trim();
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading users file: " + e.getMessage());
        }
        return username; // fallback to username if name not found
    }

    // Get all users (for admin view if needed)
    public List<String[]> getAllUsers() {
        List<String[]> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 4) {
                    users.add(parts);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading users file: " + e.getMessage());
        }
        return users;
    }
}