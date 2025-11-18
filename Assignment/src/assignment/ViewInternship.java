package assignment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class ViewInternship {
	 // View internships
    public void viewInternships(Student student, List<Internship> allInternships) {
        System.out.println("==== Available Internships ====");

        int count=0;
        for (Internship internship : allInternships) {
            if (internship.isVisible() &&
                    internship.getPreferredMajor().equalsIgnoreCase(student.getMajor()) &&
                    ((student.getYear() <= 2 && internship.getLevel().equalsIgnoreCase("Basic")) || student.getYear() >= 3)&& internship.getStatus().equalsIgnoreCase("Approved")&& internship.isVisible()==true) {
            		count++;
                    internship.displayDetails();

                    String internshipID = internship.getInternshipID();

                    // Step 1 — check if already bookmarked
                    boolean alreadyBookmarked = false;

                    File bookmarkFile = new File("bookmark_list.csv");

                    try (BufferedReader br = new BufferedReader(new FileReader("bookmark_list.csv"))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            String[] values = line.split(",");
                            if (values.length >= 2) {
                                String id1 = values[0].trim();
                                String id2 = values[1].trim();

                                if (id1.equalsIgnoreCase(student.getUserID()) && id2.equalsIgnoreCase(internshipID)) {
                                    alreadyBookmarked = true;
                                    break;
                                }
                            }
                        }
                    } catch (IOException e) {
                        System.out.println("Error reading bookmark_list.csv: " + e.getMessage());
                    }

                    //  Step 2 — skip if already bookmarked
                    if (alreadyBookmarked) {
                    } 
                    else {
                        //  Step 3 — only ask if not bookmarked
                        System.out.print("Bookmark this internship? (yes/no): ");
                        Scanner sc = new Scanner(System.in);
                        String ans = sc.nextLine();

                        if (ans.equalsIgnoreCase("yes")) {
                            try (BufferedWriter bw = new BufferedWriter(new FileWriter("bookmark_list.csv", true))) {
                                bw.write(student.getUserID() + "," + internshipID);
                                bw.newLine();
                                System.out.println("Internship bookmarked successfully.");
                            } catch (IOException e) {
                                System.out.println("Error writing to bookmark_list.csv: " + e.getMessage());
                            }
                        } 
                        else {
                            System.out.println("Bookmark skipped.");
                        }
                    }


                    System.out.println("------------------------");
                }
        }
        if(count==0) {
        	System.out.println("No Internships to display");
        }
    }
public void viewInternshipsComRep(CompanyRepresentative rep, List<Internship> allInternships) {
    Scanner sc = new Scanner(System.in);

    System.out.print("Enter Internship ID: ");
    String id = sc.nextLine().trim();

    boolean found = false;
    for (Internship i : allInternships) {
        if (i.getCompanyName().equalsIgnoreCase(rep.getCompanyName()) &&
            i.getInternshipID().equalsIgnoreCase(id)) {
            found = true;
            System.out.println("\n=== Internship Details ( Company Representative View ) ===");
            i.displayDetails();
            break;
        }
    }

    if (!found) {
        System.out.println("No internship with ID " + id + " found for your company.");
    }
}

}
