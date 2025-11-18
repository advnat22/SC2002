package assignment;

import java.util.List;

public class Report {

    // Generate reports
    public void generateReports(List<Internship> allInternships) {
        if (allInternships.isEmpty()) {
            System.out.println("No internships available to generate reports.");
            return;
        }

        for (Internship internship : allInternships) {
            internship.displayDetails();
            System.out.println("-----------------------------");
        }
    }

}
