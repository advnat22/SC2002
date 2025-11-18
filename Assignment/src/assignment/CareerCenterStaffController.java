package assignment;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CareerCenterStaffController {
	static Scanner scanner=new Scanner(System.in);
    InternshipManagementApp imt=new InternshipManagementApp();

	private List<CareerCenterStaff> staffMembers;
    private List<CompanyRepresentative> companyReps;
    private List<Internship> allInternships;
    private List<CompanyRepresentative> pendingReps;
    private List<Internship> pendingInternships;
    private List<Student> students;

    private String COMPANY_REP_CSV, INTERNSHIP_CSV;

    
    public CareerCenterStaffController(List<Student> students, List<CareerCenterStaff> staffMembers, List<CompanyRepresentative> companyReps, List<Internship> allInternships, List<CompanyRepresentative> pendingReps, List<Internship> pendingInternships, String companyRepCsv, String internshipCsv) {
        this.staffMembers = staffMembers;
        this.companyReps = companyReps;
        this.allInternships = allInternships;
        this.pendingReps = pendingReps;
        this.pendingInternships = pendingInternships;
        this.COMPANY_REP_CSV = companyRepCsv;
        this.INTERNSHIP_CSV = internshipCsv;
        this.students = students;

    }
    public void loginStaff() {
        System.out.print("Enter Staff ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Password: ");
        String pwd = scanner.nextLine();
        for (CareerCenterStaff staff : staffMembers) {
            if (staff.getId().equals(id) && staff.getPassword().equals(pwd)) {
                System.out.println("Welcome, " + staff.getName());
                staffMenu(staff);
                return;
            }
        }
        System.out.println("Invalid credentials.");
    }

    private void staffMenu(CareerCenterStaff staff) {
        while (true) {
            staff.displayMenu();
            System.out.print("Choice: ");
            int choice = imt.getIntInput();
            
            switch (choice) {
                case 1 -> {
                	staff.approveCompanyReps(pendingReps, COMPANY_REP_CSV);
                    pendingReps.removeIf(CompanyRepresentative::isApproved);
                }
                case 2 -> {
                    staff.approveInternships(pendingInternships, INTERNSHIP_CSV);
                    pendingInternships.removeIf(i -> !i.getStatus().equalsIgnoreCase("Pending"));
                }
                case 3 -> {
                    List<Student> withdrawalRequests = new ArrayList<>();
                   
                    for (Student s : students) if (!s.getAppliedInternships().isEmpty()) withdrawalRequests.add(s);
                    staff.viewWithdrawalRequests(students, allInternships);
                }
                case 4 -> staff.generateReports(allInternships);
                case 5 -> {
                    System.out.print("Enter new password: ");
                    String newpass = scanner.nextLine();
                    imt.updatePassword(staff.getId(), newpass, "sample_staff_list.csv");
                    staff.changePassword(newpass);
                    System.out.println("Password updated successfully.");
                    }
             
                case 6 -> { System.out.println("Logging out..."); return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

}
