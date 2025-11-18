package assignment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class ViewApplicants {
	 // Create a new internship and write to CSV
    // View applicants
public void viewApplicants(CompanyRepresentative rep, List<Internship> allInternships) {
    if (!rep.isApproved()) {
        System.out.println("You need Career Center approval to access the menu.");
        return;
    }

    System.out.println("Applications for your company " + rep.getCompanyName() + ":");

    boolean hasApplications = false;  // Flag to detect actual applications

    try (BufferedReader br = new BufferedReader(new FileReader("application_list.csv"))) {
        String header = br.readLine(); // read header line
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 3) {
                String studentID = parts[0].trim();
                String internshipID = parts[1].trim();
                String status = parts[2].trim();

                // Find internship matching internshipID
                Internship appInternship = null;
                for (Internship i : allInternships) {
                    if (i.getInternshipID().equals(internshipID)) {
                        appInternship = i;
                        break;
                    }
                }

                if (appInternship != null && appInternship.getCompanyName().equals(rep.getCompanyName())) {
                    // Print header once if we have real records
                    if (!hasApplications) {
                        System.out.printf("%-15s %-15s %-10s\n", "Internship ID", "Student ID", "Status");
                        hasApplications = true;
                    }
                    System.out.printf("%-15s %-15s %-10s\n", internshipID, studentID, status);
                }
            }
        }
    } catch (IOException e) {
        System.out.println("Error reading application list: " + e.getMessage());
        return;
    }

    if (!hasApplications) {
        System.out.println("No applicants yet.");
    }

}
}
