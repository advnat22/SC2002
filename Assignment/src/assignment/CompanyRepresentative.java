package assignment;



import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CompanyRepresentative extends User {
    private String companyName;
    private String department;
    private String position;
    private String email;
    private boolean approved;
    private List<Internship> internships = new ArrayList<>();
    private final int MAX_INTERNSHIPS = 5;

    private static List<CompanyRepresentative> allReps = new ArrayList<>();
    private final InternshipController internshipCreator = new InternshipController();
    private final ViewApplicants applicationsViewer = new ViewApplicants();
    private final ReviewApplications applicationReviewer = new ReviewApplications();
    private final ToggleVisibility visibilityToggler = new ToggleVisibility();

    //Constructor
    public CompanyRepresentative() {
    this.approved = false; 
}

    public CompanyRepresentative(String id, String password, String name, String companyName, String department, String position, String email) {
        this.userID = id;
        this.password = password;
        this.name = name;
        this.companyName = companyName;
        this.department = department;
        this.position = position;
        this.email = email;
        this.approved = false; // default to not approved
    
    }

    // Load company representatives from CSV
    public static void loadRepsFromCSV(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 7) {
                    CompanyRepresentative rep = new CompanyRepresentative();
                    rep.userID = values[0].trim();
                    rep.password = values[1].trim();
                    rep.name = values[2].trim();
                    rep.companyName = values[3].trim();
                    rep.department = values[4].trim();
                    rep.position = values[5].trim();
                    rep.email = values[6].trim();
                    rep.approved = values[7].trim().equalsIgnoreCase("Approved");
                    allReps.add(rep);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV: " + e.getMessage());
        }
    }

    public static List<CompanyRepresentative> getAllReps() {
        return allReps;
    }

    @Override
    public void displayMenu() {
            CompanyRepMenuDisplay menu = new CompanyRepMenuDisplay();
            menu.show(this.approved);
    }

    // Methods for menu actions

    public void createInternship(String csvFilePath) {
        internshipCreator.createInternship(this, csvFilePath);
    }
    public void viewApplicants(List<Internship> allInternships) {
    applicationsViewer.viewApplicants(this, allInternships);
}

    public void reviewApplications(List<Student> students, List<Internship> allInternships) {
        applicationReviewer.reviewApplications(this, students, allInternships);
    }

    public void toggleVisibility(String internshipID, String csvFilePath, List<Internship> allInternships) {
        visibilityToggler.toggleVisibility(internshipID, csvFilePath, allInternships);
    }

    public void viewApprovalStatus(List<Internship> allInternships) {
        System.out.println("\n=== Internship Approval Status ===");
        boolean found = false;
        for (Internship i : allInternships) {
            if(i.getCompanyName().equals(this.companyName)) {
                found = true;
                System.out.println(
                        "ID: " + i.getInternshipID() +
                        " | Company Name: " + i.getCompanyName() +
                        " | Title: " + i.getPosition() +
                        " | Status: " + i.getStatus()
                );
            }
        }
    }
    
    // Getters & setters
    public String getCompanyName() { return companyName; }
    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }
    public List<Internship> getInternships() { return internships; }
    public String getEmail() { return email; }
    public void setId(String id) {
        this.userID = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setEmail(String email) {
        this.email = email;
    }



}