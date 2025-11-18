package assignment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Bookmark {
    public void viewBookmark(Student student, List<Internship> internships) {
        System.out.println("==== Bookmarked Internships ====");

        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader("bookmark_list.csv"))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length >= 2) {
                    String studentID = parts[0].trim();
                    String internshipID = parts[1].trim();

                    // belongs to this student
                    if (studentID.equalsIgnoreCase(student.getUserID())) {

                        // find matching internship object
                        Internship matched = null;
                        for (Internship it : internships) {
                            if (it.getInternshipID().equalsIgnoreCase(internshipID)) {
                                matched = it;
                                break;
                            }
                        }

                        if (matched != null) {
                            found = true;
                            matched.displayDetails();
                            System.out.println("Application Status: "+ student.getApplicationStatus(internshipID));
                            System.out.println("------------------------");
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading bookmark list: " + e.getMessage());
            return;
        }

        if (!found) {
            System.out.println("No bookmarked internships found.");
        }
    }


}
