package assignment;

import java.util.List;
import java.util.Scanner;

public class CompanyRepController {
 static Scanner scanner=new Scanner(System.in);
    InternshipManagementApp imt=new InternshipManagementApp();

    private List<CompanyRepresentative> companyReps;
    private List<Internship> allInternships;
    private List<Internship> pendingInternships;
    private List<Student> students;
    private String INTERNSHIP_CSV;

    private InternshipController internshipController=new InternshipController();

    public CompanyRepController(List<CompanyRepresentative> companyReps, List<Internship> allInternships, List<Internship> pendingInternships, List<Student> students, String internshipCsv) {
     this.companyReps = companyReps;
        this.allInternships = allInternships;
        this.pendingInternships = pendingInternships;
        this.students = students;
        this.INTERNSHIP_CSV = internshipCsv;
    }

    public void loginCompanyRep() {
        System.out.print("Enter Rep ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Password: ");
        String pwd = scanner.nextLine();
        for (CompanyRepresentative rep : companyReps) {
            if (rep.getId().equals(id) && rep.getPassword().equals(pwd)&& rep.isApproved()) {
                System.out.println("Welcome, " + rep.getName());
                companyRepMenu(rep);
                return;
            }
            else if (rep.getId().equals(id) && rep.getPassword().equals(pwd)&& !rep.isApproved()) {
                System.out.println("Your account is pending approval by Career Center staff.");
                return;
            }
        }
        System.out.println("Invalid credentials.");
    }

    private void companyRepMenu(CompanyRepresentative rep) {
        

     while (true) {
            rep.displayMenu();
            if (!rep.isApproved()) {
                System.out.println("Waiting for staff approval.");
                return;
            }
            System.out.print("Choice: ");
            int choice = imt.getIntInput();
            switch (choice) {
                // 1. Create Internship
                case 1 -> {
                    int oldSize = rep.getInternships().size();
                    rep.createInternship(INTERNSHIP_CSV);
                    int newSize = rep.getInternships().size();
                    if (newSize > oldSize) {  // internship was actually added
                        Internship latest = rep.getInternships().get(newSize - 1);
                        allInternships.add(latest);
                        pendingInternships.add(latest);
                    } else {
                        // No new internship created, do nothing or print info if needed
                    }
                }
                // 2. Edit Internship
                case 2 -> internshipController.EditInternship(rep, INTERNSHIP_CSV);
                // 3. Delete Internship
                case 3 -> internshipController.DeleteInternship(rep, INTERNSHIP_CSV);
                // 4. Toggle visibility
                case 4 -> {
                    System.out.print("Enter Internship ID to toggle visibility: ");
                    String id = scanner.nextLine();
                    rep.toggleVisibility(id, INTERNSHIP_CSV, allInternships);
                }


                // 5. View Applicants
                case 5 -> rep.viewApplicants(allInternships);

                // 6. Review Apllications
                case 6 -> rep.reviewApplications(students, allInternships);
                // 7. view approval status
                case 7 -> rep.viewApprovalStatus(allInternships);


                case 8 -> {
                    System.out.print("Enter new password: ");
                    String newpass = scanner.nextLine();
                    imt.updatePassword(rep.getId(), newpass, "sample_company_representative_list.csv");
                    rep.changePassword(newpass);
                    System.out.println("Password updated successfully.");
                    }
             
                case 9 -> { System.out.println("Logging out..."); return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

}