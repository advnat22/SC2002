package assignment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Notification {
	 public void showNotifications(Student student) {

	        List<String[]> rows = new ArrayList<>();

	        // Read CSV
	        try (BufferedReader br = new BufferedReader(new FileReader("application_list.csv"))) {
	            String line;
	            while ((line = br.readLine()) != null) {
	                rows.add(line.split(","));
	            }
	        } catch (IOException e) {
	            System.out.println("Error reading application list: " + e.getMessage());
	            return;
	        }

	        boolean notified = false;

	        // Check all rows
	        for (String[] row : rows) {

	            if (row.length < 4) continue;

	            String studentID     = row[0].trim();
				String internshipID  = row[1].trim();
	            String status        = row[2].trim();
	            String seen          = row[3].trim();

	            // Conditions: belongs to student, approved, not seen
	            if (studentID.equalsIgnoreCase(student.getUserID()) &&
	                status.equalsIgnoreCase("Approved") &&
	                seen.equals("0")) {

	                System.out.println("Your application for Internship "
	                                    + internshipID + " has been Approved!");

	                row[3] = "1";  // mark as seen
	                notified = true;
	            }
	        }

	        // Update CSV file
	        try (BufferedWriter bw = new BufferedWriter(new FileWriter("application_list.csv", false))) {
	            for (String[] row : rows) {
	                bw.write(String.join(",", row));
	                bw.newLine();
	            }
	        } catch (IOException e) {
	            System.out.println("Error writing application list: " + e.getMessage());
	        }

	        if (!notified) {
	        }
	    }

}
