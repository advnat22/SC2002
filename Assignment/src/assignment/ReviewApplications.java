package assignment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReviewApplications {
public void reviewApplications(CompanyRepresentative rep, List<Student> allStudents, List<Internship> allInternships) {
    List<String> fileLines = new ArrayList<>();
    List<int[]> pendingIndexes = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader("application_list.csv"))) {
        String line = br.readLine();
        fileLines.add(line); // keep header
        int idx = 0;
        while ((line = br.readLine()) != null) {
            idx++;
            String[] parts = line.split(",");
            if (parts.length >= 3 && parts[2].trim().equalsIgnoreCase("Pending")) {
                fileLines.add(line);
                pendingIndexes.add(new int[]{idx, fileLines.size() - 1});
            } else {
                fileLines.add(line);
            }
        }
    } catch (IOException e) {
        System.out.println("Error reading applications: " + e.getMessage());
        return;
    }

    if (pendingIndexes.isEmpty()) {
        System.out.println("No pending applications to review.");
        return;
    }

    Scanner scanner = new Scanner(System.in);

    for (int[] idxPair : pendingIndexes) {
        int fileLineIdx = idxPair[1];
        String[] parts = fileLines.get(fileLineIdx).split(",");
        String studentID = parts[0].trim();
        String internshipID = parts[1].trim();
        String status = parts[2].trim();

        // Lookup student name
        String studentName = "Unknown";
        for (Student s : allStudents) {
            if (s.getId().equals(studentID)) {
                studentName = s.getName();
                break;
            }
        }

        // Check if the internship belongs to the rep's company
        Internship appInternship = null;
        for (Internship i : allInternships) {
            if (i.getInternshipID().equals(internshipID)) {
                appInternship = i;
                break;
            }
        }

        if (appInternship == null) {
            System.out.println("Internship not found for ID: " + internshipID);
            continue; // skip this application
        }

        if (!appInternship.getCompanyName().equals(rep.getCompanyName())) {

            continue; // skip approval for other companies' internships
        }

        System.out.println("\nInternship ID: " + internshipID);
        System.out.println("Student: " + studentID + " (" + studentName + ")");
        System.out.print("Approve? (press Enter for approve, 0 for reject): ");
        String response = scanner.nextLine().trim();

        String newStatus = response.equals("0") ? "Rejected" : "Approved";
        parts[2] = newStatus;
        fileLines.set(fileLineIdx, String.join(",", parts));
        System.out.println("Application is now: " + newStatus);
    }

    // Write changes back
    try (BufferedWriter bw = new BufferedWriter(new FileWriter("application_list.csv"))) {
        for (String l : fileLines) {
            bw.write(l);
            bw.newLine();
        }
    } catch (IOException e) {
        System.out.println("Error writing applications: " + e.getMessage());
        return;
    }

    System.out.println("\nAll pending applications reviewed.");
}
}
