package assignment;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InternshipController {
    // Existing methods and attributes of InternshipController

	private static final int MAX_INTERNSHIPS = 5;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static Scanner sc = new Scanner(System.in);
    public void createInternship(CompanyRepresentative rep, String csvFilePath) {
        if (!rep.isApproved()) {
            System.out.println("Wait for Career Center approval first.");
            return;
        }
    int existingCount = countCompanyInternships(csvFilePath, rep.getCompanyName());
    if (existingCount >= MAX_INTERNSHIPS) {
        System.out.println("Cannot create new internship. Maximum of " + MAX_INTERNSHIPS + " internships reached for your company.");
        return;
    }

    // Continue with existing limit check on rep.getInternships()
    if (rep.getInternships().size() >= MAX_INTERNSHIPS) {
        System.out.println("Max " + MAX_INTERNSHIPS + " internships allowed.");
        return;
    }
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Internship ID: ");
        String id = sc.nextLine();
        System.out.print("Enter Internship Title: ");
        String title = sc.nextLine();
        System.out.print("Enter Description: ");
        String description = sc.nextLine();
        String level;
        while (true) {
            System.out.print("Enter Level (Basic/Intermediate/Advanced): ");
            level = sc.nextLine();
            if (level.equals("Basic") || level.equals("Intermediate") || level.equals("Advanced")) {
                break; // valid input, exit loop
            } else {
                System.out.println("Invalid input. Please enter Basic, Intermediate, or Advanced (case sensitive).");
            }
      }

        System.out.print("Enter Preferred Major: ");
        String major = sc.nextLine();
        System.out.print("Enter Application Opening Date (YYYY-MM-DD): ");
        String startDate;
        do {
            System.out.print("Enter Application Opening Date (YYYY-MM-DD): ");
            startDate = sc.nextLine();
            if (!isValidDateFormat(startDate)) {
                System.out.println("Invalid format. Please use YYYY-MM-DD.");
            }
        } while (!isValidDateFormat(startDate));        
        String endDate;
        do {
            System.out.print("Enter Application Closing Date (YYYY-MM-DD): ");
            endDate = sc.nextLine();
            if (!isValidDateFormat(endDate)) {
                System.out.println("Invalid format. Please use YYYY-MM-DD.");
            }
        } while (!isValidDateFormat(endDate));



        int slots = 0;
        while (true) {
            System.out.print("Enter Number of Slots (1 to " + 10 + "): ");
            try {
                slots = Integer.parseInt(sc.nextLine());
                if (slots < 1 || slots >10 ) {
                    System.out.println("Number of slots must be between 1 and " + 10 + ".");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a valid integer.");
            }
        }
        List<CompanyRepresentative> reps = new ArrayList<>();
        reps.add(rep);

        Internship newInternship = new Internship(id, rep.getCompanyName(), title, description, level, major, startDate, endDate, slots, reps);

        rep.getInternships().add(newInternship);

        // write new internships to CSV
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFilePath, true))) {
            StringBuilder sb = new StringBuilder();
            sb.append(id).append(",");
            sb.append(title).append(",");
            sb.append(description).append(",");
            sb.append(level).append(",");
            sb.append(major).append(",");
            sb.append(rep.getCompanyName()).append(",");
            sb.append(slots).append(",");
            sb.append(startDate).append(",");
            sb.append(endDate).append(",");
            sb.append(0).append(","); // confirmedStudents
            sb.append("1").append(","); // isAvailable
            sb.append("0").append(","); // visible
            sb.append("Pending"); // status
            bw.write(sb.toString());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error writing internship to CSV: " + e.getMessage());
        }

        System.out.println("Internship created and pending approval.");
    }


    public void EditInternship(CompanyRepresentative rep, String csvFilePath) {

        if (!rep.isApproved()) {
            System.out.println("Wait for Career Center approval first.");
            return;
        }

        System.out.print("Enter Internship ID to edit: ");
        String id = sc.nextLine().trim();

        List<String> updatedLines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line = br.readLine(); // header
            if (line != null) {
                updatedLines.add(line);
            }

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                // 0:id 1:title 2:desc 3:level 4:major 5:company 6:slots 7:start 8:end 9:confirmed 10:isAvail 11:visible 12:status
                if (parts.length >= 13 &&
                        parts[0].equals(id) &&
                        parts[5].trim().equals(rep.getCompanyName())) {

                    found = true;

                    String currentStatus = parts[12].trim();
                    if (currentStatus.equalsIgnoreCase("Approved")) {
                        System.out.println("This internship has been approved Career Center Staff and can no longer be edited.");
                        updatedLines.add(line);
                        continue;
                    }

                    System.out.println("Editing internship: " + parts[0] + " (" + parts[1] + ")");
                    System.out.println("Press ENTER to keep existing value.");

                    // title
                    System.out.println("Current Title: " + parts[1]);
                    System.out.print("New Title: ");
                    String newTitle = sc.nextLine().trim();
                    if (!newTitle.isEmpty()) parts[1] = newTitle;

                    // desc
                    System.out.println("Current Description: " + parts[2]);
                    System.out.print("New Description: ");
                    String newDescription = sc.nextLine().trim();
                    if (!newDescription.isEmpty()) parts[2] = newDescription;

                    // lvl
                    System.out.println("Current Level: " + parts[3] + " (Basic/Intermediate/Advanced)");
                    while (true) {
                        System.out.print("New Level: ");
                        String newLevel = sc.nextLine().trim();
                        if (newLevel.isEmpty()) {
                            break; // keep old
                        }
                        if (newLevel.equals("Basic") || newLevel.equals("Intermediate") || newLevel.equals("Advanced")) {
                            parts[3] = newLevel;
                            break;
                        } else {
                            System.out.println("Invalid input. Please enter Basic, Intermediate, or Advanced (case sensitive).");
                        }
                    }

                    // major
                    System.out.println("Current Preferred Major: " + parts[4]);
                    System.out.print("New Preferred Major: ");
                    String newMajor = sc.nextLine().trim();
                    if (!newMajor.isEmpty()) parts[4] = newMajor;

                    // num of slots
                    System.out.println("Current Slots: " + parts[6]);
                    System.out.print("New Number of Slots (1 to 10): ");
                    while (true) {
                        String slotsStr = sc.nextLine().trim();
                        if (slotsStr.isEmpty()) {
                            break; // keep old
                        }
                        try {
                            int newSlots = Integer.parseInt(slotsStr);
                            if (newSlots < 1 || newSlots > 10) {
                                System.out.println("Number of slots must be between 1 and 10. Try again: ");
                            } else {
                                parts[6] = String.valueOf(newSlots);
                                break;
                            }
                        } catch (NumberFormatException e) {
                            System.out.print("Invalid number. Enter integer 1–10 or press ENTER to keep: ");
                        }
                    }

                    // openign date
                    System.out.println("Current Opening Date: " + parts[7]);
                    while (true) {
                        System.out.print("New Opening Date (YYYY-MM-DD): ");
                        String newStart = sc.nextLine().trim();
                        if (newStart.isEmpty()) {
                            break; // keep old
                        }
                        if (isValidDateFormat(newStart)) {
                            parts[7] = newStart;
                            break;
                        } else {
                            System.out.println("Invalid format. Please use YYYY-MM-DD.");
                        }
                    }

                    // closign date
                    System.out.println("Current Closing Date: " + parts[8]);
                    while (true) {
                        System.out.print("New Closing Date (YYYY-MM-DD): ");
                        String newEnd = sc.nextLine().trim();
                        if (newEnd.isEmpty()) {
                            break; // keep old
                        }
                        if (isValidDateFormat(newEnd)) {
                            parts[8] = newEnd;
                            break;
                        } else {
                            System.out.println("Invalid format. Please use YYYY-MM-DD.");
                        }
                    }

                    // need to rebuild line
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < parts.length; i++) {
                        sb.append(parts[i]);
                        if (i < parts.length - 1) sb.append(",");
                    }
                    updatedLines.add(sb.toString());


                    for (Internship in : rep.getInternships()) {
                        if (in.getInternshipID().equals(id) && in.getCompanyName().equals(rep.getCompanyName())) {
                            in.setPosition(parts[1]);
                            in.setDescription(parts[2]);
                            in.setLevel(parts[3]);
                            in.setPreferredMajor(parts[4]);
                            in.setStartDate(parts[7]);
                            in.setEndDate(parts[8]);
                            in.setSlots(Integer.parseInt(parts[6]));
                            break;
                        }
                    }

                } else {
                    // not the target internship, keep as is
                    updatedLines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading internship CSV: " + e.getMessage());
            return;
        }

        if (!found) {
            System.out.println("No internship found with ID " + id + " for your company.");
            return;
        }

        // write updated content back to CSV
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFilePath))) {
            for (String l : updatedLines) {
                bw.write(l);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing internship CSV: " + e.getMessage());
            return;
        }

        System.out.println("Internship updated successfully.");
    }



    public void DeleteInternship(CompanyRepresentative rep, String csvFilePath) {

        if (!rep.isApproved()) {
            System.out.println("Wait for Career Center approval first.");
            return;
        }

        System.out.print("Enter Internship ID to delete: ");
        String id = sc.nextLine().trim();

        List<String> updatedLines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line = br.readLine(); // header
            if (line != null) {
                updatedLines.add(line);
            }

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                // 0:id 1:title 2:desc 3:level 4:major 5:company 6:slots 7:start 8:end 9:confirmed 10:isAvail 11:visible 12:status
                if (parts.length >= 6 &&
                        parts[0].equals(id) &&
                        parts[5].trim().equals(rep.getCompanyName())) {
                    // Skip this line -> delete
                    found = true;
                    System.out.println("Deleting internship: " + parts[0] + " (" + parts[1] + ")");
                } else {
                    updatedLines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading internship CSV: " + e.getMessage());
            return;
        }

        if (!found) {
            System.out.println("No internship found with ID " + id + " for your company.");
            return;
        }

        // rewrite CSV without the deleted internship
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFilePath))) {
            for (String l : updatedLines) {
                bw.write(l);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing internship CSV: " + e.getMessage());
            return;
        }


        rep.getInternships().removeIf(in ->
                in.getInternshipID().equals(id) && in.getCompanyName().equals(rep.getCompanyName())
        );

        System.out.println("Internship deleted successfully.");
    }


    private int countCompanyInternships(String csvFilePath, String companyName) {
    int count = 0;
    try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
        String line = br.readLine(); // skip header maybe
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length > 5 && parts[5].trim().equals(companyName)) {
                count++;
            }
        }
    } catch (IOException e) {
        System.out.println("Error reading internship CSV: " + e.getMessage());

    }
    return count;
}

    // Helper method to validate date format
    private boolean isValidDateFormat(String dateStr) {
    return dateStr.matches("\\d{4}-\\d{2}-\\d{2}");
    }

}



