package assignment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Withdrawal {

    public void viewWithdrawalRequests(List<Student> allStudents, List<Internship> allInternships) {
        boolean hasRequests = false;
        List<String[]> myRequests = new ArrayList<>();
        List<String> allLines = new ArrayList<>();

        Scanner sc = new Scanner(System.in);

        try (BufferedReader br = new BufferedReader(new FileReader("withdrawal_list.csv"))) {
            String header = br.readLine();
            if (header != null) allLines.add(header);

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                allLines.add(line);

                if (parts.length >= 3 && parts[2].trim().equalsIgnoreCase("Pending")) {
                    // Check if student exists in allStudents list
                    for (Student s : allStudents) {
                        if (parts[1].trim().equalsIgnoreCase(s.getId())) {
                            myRequests.add(parts);
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading withdrawal_list.csv: " + e.getMessage());
            return;
        }

        if (myRequests.isEmpty()) {
            System.out.println("No withdrawal requests.");
            return;
        }

        int index = 1;
        for (int i = 0; i < myRequests.size(); i++) {
            String[] req = myRequests.get(i);
            String internshipID = req[0].trim();
            String studentID = req[1].trim();

            System.out.println(index++ + ". Withdrawal request for Internship ID: " + internshipID + ", Student ID: " + studentID);

            Student student = allStudents.stream()
                                .filter(s -> s.getId().equalsIgnoreCase(studentID))
                                .findFirst()
                                .orElse(null);

            Internship internship = allInternships.stream()
                                     .filter(j -> j.getInternshipID().equalsIgnoreCase(internshipID))
                                     .findFirst()
                                     .orElse(null);

            if (student != null && internship != null) {
                System.out.println("Student Name: " + student.getName());
                System.out.println("Internship Position: " + internship.getPosition());
                System.out.println("Company: " + internship.getCompanyName());
            }

            System.out.print("Approve withdrawal? (yes/no): ");
            String input = sc.nextLine();

            String newStatus;
            if (input.equalsIgnoreCase("yes")) {
                newStatus = "Approved";
                // Update application_list.csv status to "Withdrawn"
                updateApplicationStatus(studentID, internshipID, "Withdrawn");

                // Remove internship from student's applied internships list and update hasInternship field
                if (student != null) {
                    student.getAppliedInternships().remove(internship);
                    if (student.getAppliedInternships().isEmpty()) {
                        updateStudentHasInternship(studentID, false);
                    }
                }

                System.out.println("Withdrawal approved.");
            } else {
                newStatus = "Rejected";
                System.out.println("Withdrawal rejected.");
            }

            // Update withdrawal_list.csv status
            for (int j = 1; j < allLines.size(); j++) {
                String[] parts = allLines.get(j).split(",");
                if (parts.length >= 3 && parts[0].equalsIgnoreCase(internshipID) && parts[1].equalsIgnoreCase(studentID)) {
                    parts[2] = newStatus;
                    allLines.set(j, String.join(",", parts));
                    break;
                }
            }
        }

        // Write updated lines back to withdrawal_list.csv
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("withdrawal_list.csv"))) {
            for (String line : allLines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing withdrawal_list.csv: " + e.getMessage());
        }
    }


    private void updateStudentHasInternship(String studentId, boolean hasInternship) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("sample_student_list.csv"));
            for (int i = 1; i < lines.size(); i++) {
                String[] parts = lines.get(i).split(",");
                if (parts.length >= 7 && parts[0].equalsIgnoreCase(studentId)) {
                    parts[6] = hasInternship ? "TRUE" : "FALSE";
                    lines.set(i, String.join(",", parts));
                    break;
                }
            }
            Files.write(Paths.get("sample_student_list.csv"), lines);
        } catch (IOException e) {
            System.out.println("Error updating sample_student_list.csv: " + e.getMessage());
        }
    }

 // In CareerCenterStaff class    // Reuse or add these helpers to update application_list.csv and sample_student_list.csv
    private void updateApplicationStatus(String studentId, String internshipId, String newStatus) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("application_list.csv"));
            for (int i = 1; i < lines.size(); i++) {
                String[] parts = lines.get(i).split(",");
                if (parts.length >= 3 && parts[0].equalsIgnoreCase(internshipId) && parts[1].equalsIgnoreCase(studentId)) {
                    parts[2] = newStatus;
                    lines.set(i, String.join(",", parts));
                    break;
                }
            }
            Files.write(Paths.get("application_list.csv"), lines);
        } catch (IOException e) {
            System.out.println("Error updating application_list.csv: " + e.getMessage());
        }
    }

   
}
