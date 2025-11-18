package assignment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class InternshipApproval {
    // Approve/reject company reps with CSV update
    // Approve/reject internships with CSV update
public void approveInternships(List<Internship> pendingInternships, String csvFilePath) {
    Scanner sc = new Scanner(System.in);
    if (pendingInternships.isEmpty()) {
        System.out.println("No pending internships to approve.");
        return;
    }

    for (Internship internship : pendingInternships) {
        System.out.println("Internship ID: " + internship.getInternshipID() +
                           ", Title: " + internship.getPosition() +
                           ", Company: " + internship.getCompanyName());
        System.out.print("Approve this internship? (yes/no): ");
        String input = sc.nextLine();
        boolean approved = input.equalsIgnoreCase("yes");

        internship.setStatus(approved ? "Approved" : "Rejected");
        internship.setAvailable(approved);

        updateInternshipCSVStatus(csvFilePath, internship.getInternshipID(), internship.getStatus());
          if (approved) {
        System.out.println("Internship approved.");
        } else {
            System.out.println("Internship rejected.");
        }
        System.out.println("-----------------------------");
    }
}
private void updateInternshipCSVStatus(String csvFilePath, String internshipId, String newStatus) {
    try {
        List<String> lines = Files.readAllLines(Paths.get(csvFilePath));
        for (int i = 1; i < lines.size(); i++) { // skip header
            String[] values = lines.get(i).split(",");
            if (values[0].trim().equals(internshipId)) {
                values[12] = newStatus; // status column (adjust if needed)
                lines.set(i, String.join(",", values));
            }
        }
        Files.write(Paths.get(csvFilePath), lines);
    } catch (IOException e) {
        System.out.println("Error updating CSV: " + e.getMessage());
    }
}
}
