package assignment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;



public class ApplicationService {
	static Scanner sc=new Scanner(System.in);
    private final int MAX_APPLICATIONS = 3;
 
	// Apply
	public void applyForInternship(Student student, Internship internship) {
        if (student.getAppliedInternships().size() >= MAX_APPLICATIONS) {
            System.out.println("You have reached the maximum number of applications (" + MAX_APPLICATIONS + ").");
            return;
        }



        if (student.getYear() <= 2 && !internship.getLevel().equalsIgnoreCase("Basic")) {
            System.out.println("Year 1-2 students can only apply for Basic-level internships.");
            return;
        }

        student.getAppliedInternships().add(internship); // Correct if this is a List
        writeApplicationToCSV(internship.getInternshipID(), student.getUserID(), "Pending");
        System.out.println("Application submitted. Status: Pending");

    }
    
    private void writeApplicationToCSV(String internshipID, String studentID, String status) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("application_list.csv", true))) {
            bw.write(studentID + "," + internshipID + "," + status + ",0");   // 👈 added seen=0
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error writing to application CSV: " + e.getMessage());
        }
    }
	   // View applied
 public void viewAppliedInternships(Student student,List<Internship> allInternships) {
     boolean hasApplications = false;

     try (BufferedReader br = new BufferedReader(new FileReader("application_list.csv"))) {
         String line = br.readLine(); // Skip header if present

         while ((line = br.readLine()) != null) {
             String[] parts = line.split(",");

             if (parts.length >= 3) {
                 String studentID= parts[0].trim();
                 String internshipID = parts[1].trim();
                 String status = parts[2].trim();

                 if (studentID.equalsIgnoreCase(student.getUserID())) {
                     // Find corresponding internship object
                     Internship matchedInternship = null;
                     for (Internship internship : allInternships) {
                         if (internship.getInternshipID().equalsIgnoreCase(internshipID)) {
                             matchedInternship = internship;
                             break;
                         }
                     }

                     if (!hasApplications) {
                         hasApplications = true;
                     }

                     if (matchedInternship != null) {
                         matchedInternship.displayDetails();
                         System.out.println("Status: " + status);

                     } else {
                         System.out.println("Details not found for Internship ID: " + internshipID);
                     }
                     System.out.println("------------------------");
                 }
             }
         }
     } catch (IOException e) {
         System.out.println("Error reading applications CSV: " + e.getMessage());
     }

     if (!hasApplications) {
         System.out.println("You have no applications.");
     }
 }
 // Withdraw
 public void withdrawFromInternship(Student student, Internship internship) {
     if (!student.getAppliedInternships().contains(internship)) {
         System.out.println("You have not applied to this internship.");
         return;
     }

     // Record withdrawal request in CSV
     try (BufferedWriter bw = new BufferedWriter(new FileWriter("withdrawal_list.csv", true))) {
         bw.write(internship.getInternshipID() + "," + student.getUserID() + ",Pending");
         bw.newLine();
     } catch (IOException e) {
         System.out.println("Error writing to withdrawal CSV: " + e.getMessage());
     }

     if (!Student.withdrawalRequests.contains(student)) {
    	    Student.withdrawalRequests.add(student);
    	}


     System.out.println("Withdrawal request submitted for " + internship.getInternshipID() + ".");
     student.getAppliedInternships().remove(internship);

     if (internship.equals(student.getAcceptedInternship())) {
    	 student.setAcceptedInternship(null);
         student.hasInternship = false;
     }
 }
     
public void acceptApprovedInternship(Student student, Internship chosen) {
    // implementation that marks 'chosen' internship as accepted for this student
    // update CSV accordingly
    // Example code:
    
    try {
        File inputFile = new File("application_list.csv");
        File tempFile = new File("application_list_temp.csv");
        
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
        
        String header = reader.readLine();
        writer.write(header);
        writer.newLine();
        
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length < 3) {
                // Skip malformed or empty lines
                continue;
            }
            String studentID = parts[0].trim();
            String internshipID = parts[1].trim();
            
            if (studentID.equals(student.getUserID())) {
                if (internshipID.equals(chosen.getInternshipID())) {
                    parts[2] = "Accepted";
                } else {
                    // Optional: you may want to mark other apps as Rejected here or leave as is
                }
                line = String.join(",", parts);
            }
            
            writer.write(line);
            writer.newLine();
        }
        
        reader.close();
        writer.close();
        
        if (!inputFile.delete()) {
            System.out.println("Could not delete original file");
            return;
        }
        if (!tempFile.renameTo(inputFile)) {
            System.out.println("Could not rename temp file");
        }
    } catch (IOException e) {
        System.out.println("Error updating applications: " + e.getMessage());
    }
}

     // Helper: update student.csv hasInternship
     public void updateStudentHasInternship(String studentID, boolean has) {
         try {
             List<String> rows = Files.readAllLines(Paths.get("sample_student_list.csv"));
             for (int i = 1; i < rows.size(); i++) {
                 String[] vals = rows.get(i).split(",");
                 if (vals.length >= 7 && vals[0].trim().equals(studentID)) {
                     vals[6] = has ? "TRUE" : "FALSE";
                     rows.set(i, String.join(",", vals));
                     break;
                 }
             }
             Files.write(Paths.get("sample_student_list.csv"), rows);
         } catch (IOException e) {
             System.out.println("Error updating student CSV: " + e.getMessage());
         }
     

 }
public void updateConfirmedStudents(String internshipID) {
    try {
        List<String> rows = Files.readAllLines(Paths.get("internship_list.csv"));
        for (int i = 1; i < rows.size(); i++) {
            String[] vals = rows.get(i).split(",");
            if (vals.length > 9 && vals[0].trim().equalsIgnoreCase(internshipID)) { 
                int confirmedCount = 0;
                try {
                    confirmedCount = Integer.parseInt(vals[9].trim());  // Index 9 for ConfirmedStudents
                } catch (NumberFormatException e) {
                    confirmedCount = 0;
                }
                confirmedCount++; // Increment confirmed students count
                vals[9] = Integer.toString(confirmedCount);
                rows.set(i, String.join(",", vals));
                System.out.println("Updated confirmed count to " + confirmedCount + " for internship " + internshipID); // Debug
                break;
            }
        }
        Files.write(Paths.get("internship_list.csv"), rows);
    } catch (IOException e) {
        System.out.println("Error updating internship confirmed count: " + e.getMessage());
    }
}


 public void autoWithdrawOtherApplications(Student student, Internship acceptedInternship) {
    try {
        File inputFile = new File("application_list.csv");
        File tempFile = new File("application_list_temp.csv");

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        // Assuming first line is header and should be preserved
        String header = reader.readLine();
        writer.write(header);
        writer.newLine();

        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            // Adjust indices if your CSV has more fields
            String studentID = parts[0].trim();
            String internshipID = parts[1].trim();
            String status = parts[2].trim();

            if (studentID.equals(student.getUserID())
                && !internshipID.equals(acceptedInternship.getInternshipID())
                && (status.equalsIgnoreCase("Approved") || status.equalsIgnoreCase("Pending"))) {

                parts[2] = "Withdrawn"; // Update status

                System.out.println("Auto-withdrawing application for internship: " + internshipID);
                // Rebuild line with possibly updated status
                line = String.join(",", parts);
            }

            writer.write(line);
            writer.newLine();
        }

        reader.close();
        writer.close();

        // Replace old CSV file with updated temp file
        if (!inputFile.delete()) {
            System.out.println("Failed to delete original application_list.csv");
            return;
        }

        if (!tempFile.renameTo(inputFile)) {
            System.out.println("Failed to rename temporary file");
        }
    } catch (IOException e) {
        System.out.println("Error auto-withdrawing applications: " + e.getMessage());
    }
}


}
