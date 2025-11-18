package assignment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class CareerCenterStaff extends User {
    private String role;
    private String staffDepartment;
    private String email;

    
    
    private static List<CareerCenterStaff> allStaff = new ArrayList<>();

    
    private CompanyRepApproval companyRepApprovalService = new CompanyRepApproval();
    private InternshipApproval internshipApprovalService = new InternshipApproval();
    private Withdrawal withdrawalRequestService = new Withdrawal();
    private Report reportService = new Report();
    // Constructor
    public CareerCenterStaff(String userID, String password, String name, String role, String staffDepartment, String email) {
        this.userID = userID;
        this.password = password;
        this.name = name;
        this.role = role;
        this.staffDepartment = staffDepartment;
        this.email = email;
    }

    // Load staff automatically from CSV
    public static void loadStaffFromCSV(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 6) {
                    CareerCenterStaff staff = new CareerCenterStaff(
                        values[0].trim(), // StaffID
                        values[1].trim(), // Password
                        values[2].trim(), // Name
                        values[3].trim(), // Role
                        values[4].trim(), // Department
                        values[5].trim()  // Email
                    );
                    allStaff.add(staff);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading staff CSV: " + e.getMessage());
        }
    }

    public static List<CareerCenterStaff> getAllStaff() {
        return allStaff;
    }

    @Override
    public void displayMenu() {
        CareerStaffMenuDisplay menu = new CareerStaffMenuDisplay();
        menu.show();
    }

    public void approveCompanyReps(List<CompanyRepresentative> reps, String csvFilePath) {
    	companyRepApprovalService.approveCompanyRep(reps, csvFilePath);
    }

    public void approveInternships(List<Internship> internships, String csvFilePath) {
    	internshipApprovalService.approveInternships(internships, csvFilePath);
    }

    public void viewWithdrawalRequests(List<Student> students, List<Internship> internships) {
    	withdrawalRequestService.viewWithdrawalRequests(students, internships);
    }

    public void generateReports(List<Internship> internships) {
    	reportService.generateReports(internships);
    }
    // Getter and setter
    public String getStaffDepartment() {
        return staffDepartment;
    }

    public void setStaffDepartment(String staffDepartment) {
        this.staffDepartment = staffDepartment;
    }
}
