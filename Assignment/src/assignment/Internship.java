package assignment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Internship {
    private String internshipID;
    private String companyName;
    private String position;        // Internship title
    private String description;
    private String level;           // Basic, Intermediate, Advanced
    private String preferredMajor;
    private String startDate;       // Application opening date
    private String endDate;         // Application closing date
    private String status;          // Pending, Approved, Rejected, Filled
    private List<CompanyRepresentative> representatives; // Assigned company reps
    private int slots;              // Max 10
    private int confirmedStudents;  // Tracks how many students accepted
    private boolean isAvailable;
    private boolean visible;

    //No-argument constructor
    public Internship() {
        this.confirmedStudents = 0;
        this.isAvailable = false;
        this.visible = true;
        this.status = "Pending";
        this.representatives = new ArrayList<>();
    }

    // Parameterized constructor
    public Internship(String internshipID, String companyName, String position, String description,
                      String level, String preferredMajor, String startDate, String endDate,
                      int slots, List<CompanyRepresentative> reps) {
        this.internshipID = internshipID;
        this.companyName = companyName;
        this.position = position;
        this.description = description;
        this.level = level;
        this.preferredMajor = preferredMajor;
        this.startDate = startDate;
        this.endDate = endDate;
        this.slots = Math.min(slots, 10);
        this.confirmedStudents = 0;
        this.isAvailable = false;
        this.visible = true;
        this.status = "Pending";
        this.representatives = reps != null ? reps : new ArrayList<>();
    }
    // Load internships from CSV (optional)
    public static List<Internship> loadInternshipsFromCSV(String filePath) {
        List<Internship> internships = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 11) {
                    Internship i = new Internship();
                    i.internshipID = values[0].trim();
                    i.position = values[1].trim();
                    i.description = values[2].trim();
                    i.level = values[3].trim();
                    i.preferredMajor = values[4].trim();
                    i.companyName = values[5].trim();
                    i.slots = Integer.parseInt(values[6].trim());
                    i.startDate = values[7].trim();
                    i.endDate = values[8].trim();
                    i.confirmedStudents = Integer.parseInt(values[9].trim());
                    i.isAvailable = values[10].trim().equals("1");
                    i.visible = values[11].trim().equals("1");
                    i.status = values[12].trim();
                    i.representatives = new ArrayList<>();
                    internships.add(i);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading internship CSV: " + e.getMessage());
        }
        return internships;
    }
    // Display info
    public void displayDetails() {
        System.out.println("Internship ID: " + internshipID);
        System.out.println("Company: " + companyName);
        System.out.println("Title: " + position);
        System.out.println("Description: " + description);
        System.out.println("Level: " + level);
        System.out.println("Preferred Major: " + preferredMajor);
        System.out.println("Application Start: " + startDate);
        System.out.println("Application End: " + endDate);
        System.out.println("Slots: " + slots);
        System.out.println("Confirmed Students: " + confirmedStudents);
        System.out.println("Available: " + (isAvailable ? "Yes" : "No"));
        System.out.println("Visible: " + (visible ? "Yes" : "No"));

    }

    // Getters
    public String getInternshipID() { return internshipID; }
    public String getCompanyName() { return companyName; }
    public String getPosition() { return position; }
    public String getDescription() { return description; }
    public String getLevel() { return level; }
    public String getPreferredMajor() { return preferredMajor; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public String getStatus() { return status; }
    public List<CompanyRepresentative> getRepresentatives() { return representatives; }
    public int getSlots() { return slots; }
    public int getConfirmedStudents() { return confirmedStudents; }
    public boolean isAvailable() { return isAvailable; }
    public boolean isVisible() { return visible; }

    // Setters
    public void setInternshipID(String ID) { this.internshipID= internshipID; }

    public void setCompanyName(String companyName) { this.companyName= companyName; }
    public void setPosition(String position) { this.position = position; }
    public void setDescription(String description) { this.description = description; }
    public void setLevel(String level) { this.level = level; }
    public void setPreferredMajor(String preferredMajor) { this.preferredMajor = preferredMajor; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public void setStatus(String status) { this.status = status; }
    public void setSlots(int slots) { this.slots = Math.min(slots, 10); }
    public void setConfirmedStudents(int confirmedStudents) { this.confirmedStudents = confirmedStudents; }
    public void setAvailable(boolean isAvailable) { this.isAvailable = isAvailable; }
    public void setVisible(boolean visible) { this.visible = visible; }
    public void setRepresentatives(List<CompanyRepresentative> reps) { this.representatives = reps; }


    


}
