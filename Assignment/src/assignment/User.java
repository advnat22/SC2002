package assignment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class User {
    protected String userID;
    protected String name;
    protected String password;

    // List to store all loaded users
    protected static List<User> allUsers = new ArrayList<>();


    // Generic CSV loader for subclasses
    public static <T extends User> List<T> loadUsersFromCSV(String filePath, UserFactory<T> factory) {
        List<T> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 3) {
                    String userID = values[0].trim();
                    String password = values[1].trim();
                    String name = values[2].trim();
                    T user = factory.createUser(userID, password, name, values);
                    users.add(user);
                    allUsers.add(user);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }
        return users;
    }
    // Abstract method for subclasses to implement
    public abstract void displayMenu();

    public String getId() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public boolean login(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    public void logout() {
        System.out.println(name + " logged out.");
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
        System.out.println("Password changed successfully.");
    }


    // Functional interface for creating subclass objects
    public interface UserFactory<T extends User> {
        T createUser(String userID, String password, String name, String[] csvValues);
    }
    public String getUserID() { return userID; }

}
