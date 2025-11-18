package assignment;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudentController {
	static Scanner scanner=new Scanner(System.in);
    private List<Student> students;
    private List<Internship> allInternships;
    InternshipManagementApp imt=new InternshipManagementApp();
    public StudentController(List<Student> students, List<Internship> allInternships) {
        this.students = students;
        this.allInternships = allInternships;
    }
    public  void loginStudent() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Password: ");
        String pwd = scanner.nextLine();

        for (Student s : students) {
            if (s.getId().equals(id) && s.getPassword().equals(pwd)) {
                s.loadAppliedInternshipsFromCSV(allInternships);  // Sync applied internships
                System.out.println("Welcome, " + s.getName());
                studentMenu(s);
                return;
            }
        }
        System.out.println("Invalid credentials.");
    }
    
	private void studentMenu(Student s) {
        while (true) {
            s.showNotifications();

            s.displayMenu();
            System.out.print("Choice: ");
            int choice = imt.getIntInput();
            switch (choice) {
                case 1 -> {       
                	s.viewInternships(allInternships);
                }
                case 2 ->
                	s.viewBookmarks(allInternships);
                case 3 -> applyForInternship(s);
                case 4 -> s.viewAppliedInternships(allInternships);
                case 5 -> acceptPlacement(s);
                case 6 -> withdrawApplication(s);
                case 7 -> {
                    System.out.print("Enter new password: ");
                    String newpass = scanner.nextLine();
                    imt.updatePassword(s.getId(), newpass, "sample_student_list.csv");
                    s.changePassword(newpass);
                    }
             
                case 8 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
    private  void applyForInternship(Student s) {
        System.out.print("Enter Internship ID to apply: ");
        String id = scanner.nextLine();
        for (Internship i : allInternships) {
            if (i.getInternshipID().equals(id)) {
                s.applyForInternship(i);
                return;
            }
        }
        System.out.println("Internship not found.");
    }

    private void acceptPlacement(Student s) {
        List<Internship> applied = s.getAppliedInternships(); // get applied internships
        if (applied == null || applied.isEmpty()) {
            System.out.println("You have no applied internships.");
            return;
        }
        List<Internship> approved = new ArrayList<>();

    	// Filter only 'Approved' applications from CSV
    	for (Internship internship : applied) {
    	    String status = s.getApplicationStatus(internship.getInternshipID());
    	    if (status.equalsIgnoreCase("Approved")) {
    	        approved.add(internship);
    	    }
    	}

    	if (approved.isEmpty()) {
    	    System.out.println("None approved yet");
    	    return;
    	}

    	System.out.println("The following internships are approved for acceptance:");
    	for (int i = 0; i < approved.size(); i++) {
    	    System.out.println((i + 1) + ". " + approved.get(i).getInternshipID()
    	        + " - " + approved.get(i).getPosition());
    	}

    	System.out.print("Select to accept (0 to cancel): ");
    	int choice = imt.getIntInput();

        if (choice > 0 && choice <= approved.size()) {
            Internship chosen = approved.get(choice - 1);
            s.acceptApprovedInternship(chosen);
            s.autoWithdrawOtherApplications(chosen);
        }


    }
    private  void withdrawApplication(Student s) {
        List<Internship> applied = s.getAppliedInternships();
        if (applied.isEmpty()) {
            System.out.println("No applications.");
            return;
        }
        for (int i = 0; i < applied.size(); i++)
            System.out.println((i+1) + ". " + applied.get(i).getInternshipID());
        System.out.print("Select to withdraw (0 to cancel): ");
        int choice = imt.getIntInput();
        if (choice > 0 && choice <= applied.size()) s.withdrawFromInternship(applied.get(choice-1));
    }


}
