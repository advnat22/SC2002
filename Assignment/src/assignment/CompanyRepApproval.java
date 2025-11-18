package assignment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class CompanyRepApproval {
	public void approveCompanyRep(List<CompanyRepresentative> pendingReps, String csvFilePath) {
        Scanner sc = new Scanner(System.in);
        if (pendingReps.isEmpty()) {
            System.out.println("No pending company representatives to approve.");
            return;
        }

        for (CompanyRepresentative rep : pendingReps) {
            System.out.println("Company Rep ID: " + rep.getId() +
                               ", Name: " + rep.getName() +
                               ", Company: " + rep.getCompanyName());
            System.out.print("Approve this representative? (yes/no): ");
            String input = sc.nextLine();
            boolean approved = input.equalsIgnoreCase("yes");
            rep.setApproved(approved);
            System.out.println(approved ? "Representative approved." : "Representative rejected.");

            updateCompanyRepCSVStatus(csvFilePath, rep.getId(), approved ? "Approved" : "Rejected");
            System.out.println("-----------------------------");
        }
    }

    private void updateCompanyRepCSVStatus(String csvFilePath, String repId, String newStatus) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(csvFilePath));
            for (int i = 1; i < lines.size(); i++) { // skip header
                String[] values = lines.get(i).split(",");
                if (values[0].trim().equals(repId)) {
                    values[7] = newStatus; // status column (adjust if needed)
                    lines.set(i, String.join(",", values));
                }
            }
            Files.write(Paths.get(csvFilePath), lines);
        } catch (IOException e) {
            System.out.println("Error updating CSV: " + e.getMessage());
        }
    }


	
}
