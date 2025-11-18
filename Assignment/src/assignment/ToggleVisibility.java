package assignment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ToggleVisibility {
    // Toggle visibilitypublic void toggleVisibility(String internshipID, String csvFilePath) {
    public void toggleVisibility(String internshipID, String csvFilePath, List<Internship> allInternships) {
    	 Internship target = null;
    	    for (Internship internship : allInternships) {
    	        if (internship.getInternshipID().equals(internshipID)) {
    	            target = internship;
    	            break;
    	        }
    	    }

    	    if (target == null) {
    	        System.out.println("Internship not found.");
    	        return;
    	    }

    	    boolean newVisibility = !target.isVisible();
    	    target.setVisible(newVisibility);

    	    // Update CSV logic ...
        // Flip visibility: if true, make false; if false, make true
    
        target.setVisible(newVisibility);

        System.out.println("Internship " + internshipID + " visibility is now " + (newVisibility ? "ON" : "OFF"));

        // Update CSV visibility
        try {
            List<String> lines = Files.readAllLines(Paths.get(csvFilePath));
            for (int i = 1; i < lines.size(); i++) {
                String[] values = lines.get(i).split(",");
                if (values[0].trim().equals(internshipID)) {
                    values[11] = newVisibility ? "1" : "0"; // Assuming 1 means visible and 0 means hidden
                    lines.set(i, String.join(",", values));
                    break;
                }
            }
            Files.write(Paths.get(csvFilePath), lines);
        } catch (IOException e) {
            System.out.println("Error updating internship visibility in CSV: " + e.getMessage());
        }
    }

}
