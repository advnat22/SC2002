<h1 align="center"> SC2002 Internship Management System</h1>

<div align="center">
<p>SC2002 Object-Oriented Design & Programming | AY2025/26 Semester 1</p>

[![Javadoc Badge](https://img.shields.io/badge/Javadoc-F8981D?style=for-the-badge&logo=readthedocs&logoColor=FFFFFF&logoSize=auto&labelColor=222222)](https://github.com/advnat22/SC2002/tree/main/SC2002_javadoc)
&nbsp;

[![Class Diagrams Badge](https://img.shields.io/badge/Class%20Diagrams-C2F0C0?style=for-the-badge&logo=diagramsdotnet&logoColor=FFFFFF&logoSize=auto&labelColor=222222)](https://github.com/advnat22/SC2002/tree/main/Diagrams)
&nbsp;
[![Sequence Diagrams Badge](https://img.shields.io/badge/Sequence%20Diagrams-FFF6B6?style=for-the-badge&logo=miro&logoSize=auto&labelColor=222222)](https://github.com/advnat22/SC2002/tree/main/Diagrams/Sequence%20Diagrams)


=== Click to view our respective documentation and diagrams!====

<p align="center">
<a href="#introduction">Introduction</a> &nbsp;&bull;&nbsp;
<a href="#team-members">Team Members</a> &nbsp;&bull;&nbsp;
<a href="#main features">Features</a> &nbsp;&bull;&nbsp;
<a href="#extra features">Extra Features</a> &nbsp;&bull;&nbsp;


</p>
</div>

---

## Introduction
The **Internship Management System (IMS)** is a Java-based Command Line Interface (CLI) application designed to allow students, company representatives and career center staff to manage internship placements, applications and approvals.

The system was developed as part of **SC2002 Object-Oriented Design & Programming** module, demonstrating key Object-Oriented Programming (OOP) principles such as encapsulation, abstraction, inheritance, and polymorphism.

---

## Project Structure
```
SC2002-IMS
|
├── Assignment/
│   ├── src/assignment/
│   │   ├── ApplicationService.java
│   │   ├── Bookmark.java
│   │   ├── CareerCenterStaff.java
│   │   ├── CareerCenterStaffController.java
│   │   ├── CareerStaffMenuDisplay.java
│   │   ├── CompanyRepApproval.java
│   │   ├── CompanyRepController.java
│   │   ├── CompanyRepMenuDisplay.java
│   │   ├── CompanyRepresentative.java
│   │   ├── Internship.java
│   │   ├── InternshipApproval.java
│   │   ├── InternshipController.java
│   │   ├── InternshipManagementApp.java    # Main entry point
│   │   ├── MainMenuDisplay.java
│   │   ├── Notification.java
│   │   ├── Report.java
│   │   ├── ReviewApplications.java
│   │   ├── Student.java
│   │   ├── StudentController.java
│   │   ├── StudentMenuDisplay.java
│   │   ├── ToggleVisibility.java
│   │   ├── User.java
│   │   ├── ViewApplicants.java
│   │   ├── ViewInternship.java
│   │   └── Withdrawal.java
│   ├── *.csv     # Data files
│   └── module-info.java
|
├── Diagrams/
│   ├── Class Diagram/
│   └── Sequence Diagrams/
│      
|
├── SC2002_javadoc/
│   └── index.html
|
├── Final Report/
│   └── Final Report.pdf
|
├── SC2002_SCEC_GROUP4.pdf
├── README.md
```
---

## Team Members

| **Name**             | **Email Address**       |
|----------------------|-------------------------|
| Jolie Loke Zhi Xuan  | jloke010@e.ntu.edu.sg   |
| Natarajan Advaith    | advaith003@e.ntu.edu.sg |
| Sahana Anandhan      | sahana008@e.ntu.edu.sg  |
| Sivakumar Yasuvanthi | yasuvant001@e.ntu.edu.sg|

---

## Features

### **Main System**
- Login authentication for multiple User profiles
- View and update User profiles
- Password update function

### **Student**
- View available internships
- Apply for internships
- Track internship application status
- Request application withdrawal

### **Company Representative**
- Register new internship postings
- Review student applications
- Approve or reject applicants
- Manage internship applications (Edit/Create/Delete)

### **Career Center Staff**
- Manage Company Representative registrations and Student applications
- Approve or reject internship withdrawal requests
- Oversee system data and generate filtered internship reports

---
## Additional Features

### **Notification**
- Automatically alerts students when their application status changes (e.g., from “Pending” to “Approved”).
- Keeps Students informed in real-time without requiring them to constantly check status
- Enhances engagement and reduces the likelihood of missed internship opportunities

### **Bookmark**
- Allows students to save interested internships for easy access
- Students can view bookmarked internships



