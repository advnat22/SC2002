package assignment; 

import java.io.*;
import java.util.*;

public class InternshipManagementApp {
    private static List<Student> students = new ArrayList<>();
    private static List<CompanyRepresentative> companyReps = new ArrayList<>();
    private static List<CareerCenterStaff> staffMembers = new ArrayList<>();
    private static List<Internship> allInternships = new ArrayList<>();
    private static List<CompanyRepresentative> pendingReps = new ArrayList<>();
    private static List<Internship> pendingInternships = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    private static final String STUDENT_CSV = "sample_student_list.csv";
    private static final String STAFF_CSV = "sample_staff_list.csv";
    private static final String COMPANY_REP_CSV = "sample_company_representative_list.csv";
    private static final String INTERNSHIP_CSV = "internship_list.csv";

    public static void main(String[] args) {
        System.out.println("Working directory: " + new File(".").getAbsolutePath());
        System.out.println(System.getProperty("user.dir"));

        loadData();
        
        StudentController studentCtrl = new StudentController(students, allInternships);
        CompanyRepController repCtrl = new CompanyRepController(companyReps, allInternships, pendingInternships, students, INTERNSHIP_CSV);
        CareerCenterStaffController staffCtrl = new CareerCenterStaffController(students, staffMembers, companyReps, allInternships, pendingReps, pendingInternships, COMPANY_REP_CSV, INTERNSHIP_CSV);
        MainMenuDisplay mainMenu = new MainMenuDisplay();

        while (true) {
            mainMenu.show();
            int choice = getIntInput();

            switch (choice) {
            case 1 -> studentCtrl.loginStudent();
            case 2 -> repCtrl.loginCompanyRep();
            case 3 -> staffCtrl.loginStaff();
            case 4 -> registerCompanyRep();
            case 5 -> { System.out.println("Goodbye!"); return; }
            default -> System.out.println("Invalid choice.");
            }
        }
    }
    private static void registerCompanyRep() {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Company Representative Registration ===");
        System.out.print("Enter Rep ID: ");
        String id = sc.nextLine().trim();
        System.out.print("Enter Password: ");
        String pwd = sc.nextLine().trim();
        System.out.print("Enter Name: ");
        String name = sc.nextLine().trim();
        System.out.print("Enter Company Name: ");
        String companyName = sc.nextLine().trim();
        System.out.print("Enter Department: ");
        String department = sc.nextLine().trim();
        System.out.print("Enter Position: ");
        String position = sc.nextLine().trim();
        System.out.print("Enter Email: ");
        String email = sc.nextLine().trim();

        // Append new rep to CSV as unapproved
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(COMPANY_REP_CSV, true))) {
            StringBuilder sb = new StringBuilder();
            sb.append(id).append(",");
            sb.append(pwd).append(",");
            sb.append(name).append(",");
            sb.append(companyName).append(",");
            sb.append(department).append(",");
            sb.append(position).append(",");
            sb.append(email).append(",");
            sb.append("Pending"); // Not approved yet
            bw.newLine();
            bw.write(sb.toString());
        } catch (IOException e) {
            System.out.println("Error writing new company rep: " + e.getMessage());
            return;
        }

        // Create new rep object, add to lists accordingly
        CompanyRepresentative newRep = new CompanyRepresentative();
        newRep.setId(id);
        newRep.setPassword(pwd);
        newRep.setName(name);
        newRep.setCompanyName(companyName);
        newRep.setDepartment(department);
        newRep.setPosition(position);
        newRep.setEmail(email);
        newRep.setApproved(false);
        companyReps.add(newRep);
        pendingReps.add(newRep);

        System.out.println("Registration complete. Awaiting staff approval.");
    }

    private static void loadData() {
        // Load students
        Student.loadUsersFromCSV(STUDENT_CSV);
        students.addAll(Student.getAllStudents());

        // Load staff
        CareerCenterStaff.loadStaffFromCSV(STAFF_CSV);
        staffMembers.addAll(CareerCenterStaff.getAllStaff());

        // Load company representatives
        CompanyRepresentative.loadRepsFromCSV(COMPANY_REP_CSV);
        companyReps.addAll(CompanyRepresentative.getAllReps());

        // Pending reps are unapproved
        for (CompanyRepresentative rep : companyReps) {
            if (!rep.isApproved()) pendingReps.add(rep);
        }

        // Load internships
        allInternships.addAll(Internship.loadInternshipsFromCSV(INTERNSHIP_CSV));

        // Pending internships are "Pending" status
        for (Internship i : allInternships) {
            if (i.getStatus().equalsIgnoreCase("Pending")) pendingInternships.add(i);
        }

        System.out.println("Data loaded successfully.");
    }


    public static void updatePassword(String ID, String newPassword, String filename) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].trim().equals(ID)) {
                    parts[1] = newPassword; // Update password - column 2
                    // Join columns back into a CSV row
                    line = String.join(",", parts);
                }
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading student list: " + e.getMessage());
            return;
        }

        // Write back to file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error updating student list: " + e.getMessage());
        }
    }


    public static int getIntInput() {
        while (true) {
            try { return Integer.parseInt(scanner.nextLine()); }
            catch (NumberFormatException e) { System.out.print("Enter a number: "); }
        }
    }
}
