package assignment;

public class CompanyRepMenuDisplay {

    public void show(boolean approved) {
        if (!approved) {
            System.out.println("You need Career Center approval to access the menu.");
            return;
        }

        System.out.println("==== Company Representative Menu ====");
        System.out.println("1. Create internship");
        System.out.println("2. Edit internship");
        System.out.println("3. Delete internship");
        System.out.println("4. Toggle internship visibility");
        System.out.println("5. View applicants");
        System.out.println("6. Review Applicants");
        System.out.println("7. View Internship Approval Status");
        System.out.println("8. Change Password");
        System.out.println("9. Logout");
    }
}
