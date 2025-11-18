package assignment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Student extends User {
    private int year; // Year of study (1-4)
    private String major;
    public boolean hasInternship;

    private List<Internship> appliedInternships;
    private Internship acceptedInternship;

    private static List<Student> allStudents = new ArrayList<>();
    public static List<Student> withdrawalRequests = new ArrayList<>();

    private final int MAX_APPLICATIONS = 3;
    Scanner sc = new Scanner(System.in);
    private final Notification notificationService = new Notification();
    private final ViewInternship internshipViewer = new ViewInternship();
    private final Bookmark bookmarkService = new Bookmark();
    private final ApplicationService applicationService = new ApplicationService();
    // Constructor
    public Student() {
        appliedInternships = new ArrayList<>();
    }

    // Load students from CSV
    public static void loadUsersFromCSV(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 6) {
                    Student s = new Student();
                    s.userID = values[0].trim();
                    s.password = values[1].trim();
                    s.name = values[2].trim();
                    s.major = values[3].trim();
                    s.year = Integer.parseInt(values[4].trim());
                    s.hasInternship = Boolean.parseBoolean(values[6].trim());
                    allStudents.add(s);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }
    }

    public static List<Student> getAllStudents() {
        return allStudents;
    }

    public void showNotifications() {
        notificationService.showNotifications(this);
    }

    @Override
    public void displayMenu() {
        StudentMenuDisplay menu = new StudentMenuDisplay();
        menu.show();
    }

    
     public void loadAppliedInternshipsFromCSV(List<Internship> allInternships) {
        appliedInternships.clear();
        try (BufferedReader br = new BufferedReader(new FileReader("application_list.csv"))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].trim().equals(this.userID)) {
                    String internshipID = parts[1].trim();
                    // Match and add internship object
                    for (Internship internship : allInternships) {
                        if (internship.getInternshipID().equalsIgnoreCase(internshipID)) {
                            appliedInternships.add(internship);
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading applied internships: " + e.getMessage());
        }
    }
     
     public void viewInternships(List<Internship> internships) {
         internshipViewer.viewInternships(this, internships);
     }
     public void viewBookmarks(List<Internship> internships) {
         bookmarkService.viewBookmark(this, internships);
     }
     public void applyForInternship(Internship internship) {
        if(this.getHasInternship()) {
            System.out.println("You have already accepted an internship. Cannot apply for more.");
        }
        else{
            applicationService.applyForInternship(this, internship);
        }
     }
     public void viewAppliedInternships(List<Internship> internships) {
         applicationService.viewAppliedInternships(this, internships);
     }
     public void withdrawFromInternship(Internship internship) {
         applicationService.withdrawFromInternship(this, internship);
         
     }
    public void acceptApprovedInternship(Internship chosen) {
        applicationService.acceptApprovedInternship(this, chosen);
        applicationService.updateStudentHasInternship(this.getUserID(), true);
        applicationService.updateConfirmedStudents(chosen.getInternshipID());

        this.hasInternship = true;  // Update in-mxemory flag


    }

    public void autoWithdrawOtherApplications(Internship acceptedInternship) {
        applicationService.autoWithdrawOtherApplications(this, acceptedInternship);
    }

  // If static in Student:

    // Getters
public String getApplicationStatus(String internshipID) {
    try (BufferedReader br = new BufferedReader(new FileReader("application_list.csv"))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] vals = line.split(",");
            if (vals.length >= 3
                    && vals[0].trim().equalsIgnoreCase(this.getUserID())
                    && vals[1].trim().equalsIgnoreCase(internshipID)) {
                return vals[2].trim();
            }
        }
    } catch (IOException e) {
        System.out.println("Error reading applications CSV: " + e.getMessage());
    }
    return "Not Found";
}

    public int getYear() { return year; }
    public String getMajor() { return major; }
    public boolean getHasInternship() { return hasInternship; }
    public List<Internship> getAppliedInternships() { return appliedInternships; }
    public Internship getAcceptedInternship() { return acceptedInternship; }
    public void setAcceptedInternship(Internship internship) {
        this.acceptedInternship = internship;
    }


}
