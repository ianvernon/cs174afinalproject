package edu.ucsb.cs;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

/**
 * Created by ianvernon on 3/3/15.
 */
public class Author
{
    private String authorID;
    private String authorTitle;
    private String authorFirstName;
    private String authorLastName;

    public Author(String authorID, String authorTitle, String authorFirstName, String authorLastName)
    {
        this.authorID = authorID;
        this.authorTitle = authorTitle;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
    }

    public String getAuthorID() {
        return authorID;
    }

    public String getAuthorTitle() {
        return authorTitle;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }
    public void editPatientPlan(Connection connectHISDB, Patient p) throws SQLException
    {
        System.out.println("Enter the plan ID you would like to edit from the following plan(s). Enter -1 to exit: ");
        p.viewPatientPlanInfo(connectHISDB);
        Scanner planScanner = new Scanner(System.in);
        String planIdStr = planScanner.next();
        if(planIdStr.equals("-1"))
        {
            System.out.println("*************EXITING EDIT PATIENT PLAN****************");
            return;
        }
        String query = "SELECT * FROM PatientPlan WHERE planID='" + planIdStr + "'";
        Statement statement = connectHISDB.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        // make sure planID entered is valid
        while(!resultSet.isBeforeFirst())
        {
            System.out.println("Inputted planID is not valid.");
            System.out.println("Enter valid planID");
            planIdStr = planScanner.next();
            if(planIdStr.equals("-1"))
            {
                System.out.println("*************EXITING EDIT PATIENT PLAN********************");
                return;
            }
            query = "SELECT * FROM PatientPlan WHERE planID='" + planIdStr + "'";
            resultSet = statement.executeQuery(query);
        }
        resultSet.next();
        PatientPlan patientPlan = new PatientPlan(resultSet.getString("planID"), resultSet.getDate("date"), resultSet.getString("activity"),
                resultSet.getString("patientID"));
        System.out.println("Which attributes of the plan would you like to edit? Select from the following. Exit with -1: ");
        System.out.println("1: date");
        System.out.println("2: activity");
        String attributeToEdit = planScanner.next();
        while(!attributeToEdit.equals("-1"))
        {
            //editing date / time
            if(attributeToEdit.equals("1"))
            {
                Scanner dateScanner = new Scanner(System.in);
                System.out.println("Enter date: (yyyy-mm-dd): ");
                String date =  dateScanner.next();
                if(date.equals("-1"))
                {
                    //displayDoctorMenu();
                    return;
                }
                while(!date.matches("((19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])"))
                {
                    System.out.println("Incorrect format. Try again.\nEnter date: (mm/dd/yyyy)");
                    date = dateScanner.next();
                    if(date.equals("-1"))
                    {
                        //displayDoctorMenu();
                        return;
                    }
                }
                System.out.println("Date entered.");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                ;               try
            {
                java.util.Date date1 = format.parse(date);
                java.sql.Date date2 = new java.sql.Date(date1.getTime());
                String update = "UPDATE PatientPlan SET date= ? WHERE planID= ?";
                PreparedStatement preparedStatement = connectHISDB.prepareStatement(update);
                preparedStatement.setDate(1, date2);
                preparedStatement.setString(2, patientPlan.getPlanID());
                preparedStatement.executeUpdate();
                System.out.println("Updated plan.");
                this.updateAssignedData(connectHISDB, p, "Plan");
            }
            catch(ParseException ex)
            {
                ex.printStackTrace();
            }

            }
            //editing activity
            else if(attributeToEdit.equals("2"))
            {
                Scanner activityScanner = new Scanner(System.in);
                System.out.println("Enter activity: ");
                String activity = activityScanner.nextLine();
                if(activity.equals("-1"))
                {
                    //displayDoctorMenu();
                    return;
                }
                String update = "UPDATE PatientPlan SET activity='" + activity + "' WHERE planID = '" + patientPlan.getPlanID() + "'";
                statement.executeUpdate(update);
                System.out.println("Updated plan.");
                this.updateAssignedData(connectHISDB, p, "Plan");
            }
            else
            {
                System.out.println("Invalid input.");
            }
            System.out.println("Which attributes of the plan would you like to edit? Select from the following. Exit with -1: ");
            System.out.println("1: date");
            System.out.println("2: activity");
            attributeToEdit = planScanner.next();
        }
        System.out.println("******************EXITING EDIT PATIENT PLAN************************");
        return;
    }
    public void editAllergiesInformation(Connection connectHISDB, Patient p) throws SQLException
    {
        System.out.println("Enter the allergy ID from the following for this patient that you'd like to edit: ");
        p.viewPatientAllergies(connectHISDB);
        Scanner allergyScanner = new Scanner(System.in);
        String allergyIdStr = allergyScanner.next();
        if(allergyIdStr.equals("-1"))
        {
            return;
        }
        String query = "SELECT * FROM PatientAllergy WHERE allergyID='" + allergyIdStr + "' AND patientID='" + p.getPatientID() + "'";
        Statement statement = connectHISDB.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        // make sure planID entered is valid
        while(!resultSet.isBeforeFirst())
        {
            System.out.println("Inputted allergyID is not valid.");
            System.out.println("Enter valid allergyID");
            allergyIdStr = allergyScanner.next();
            if(allergyIdStr.equals("-1"))
            {
                return;
            }
            query = "SELECT * FROM PatientAllergy WHERE allergyID='" + allergyIdStr + "'";
            resultSet = statement.executeQuery(query);
        }
        resultSet.next();
        PatientAllergy patientAllergy = new PatientAllergy(resultSet.getString("allergyID"), resultSet.getString("substance"), resultSet.getString("reaction"),
                resultSet.getString("status"), resultSet.getString("patientID"));
        System.out.println("What attributes of this allergy would you like to edit? Select from the following. Exit with -1: ");
        System.out.println("1: Substance");
        System.out.println("2: Reaction");
        System.out.println("3: Status");

        String attributeToEdit = allergyScanner.next();
        while(!attributeToEdit.equals("-1"))
        {
            //editing substance
            if(attributeToEdit.equals("1"))
            {
                Scanner subScanner = new Scanner(System.in);
                System.out.println("Enter substance:");
                String substance = subScanner.nextLine();
                String update = "UPDATE PatientAllergy SET substance='" + substance + "' WHERE allergyID='" + patientAllergy.getAllergyID() + "'";
                statement.executeUpdate(update);
                System.out.println("Updated allergy information.");
                this.updateAssignedData(connectHISDB, p, "Allergy Information");
            }
            //editing reaction
            else if(attributeToEdit.equals("2"))
            {
                Scanner rxnScanner = new Scanner(System.in);
                System.out.println("Enter reaction: ");
                String rxn = rxnScanner.nextLine();
                String update = "UPDATE PatientAllergy SET reaction='" + rxn + "' WHERE allergyID='" + patientAllergy.getAllergyID() + "'";
                statement.executeUpdate(update);
                System.out.println("Updated allergy information.");
                this.updateAssignedData(connectHISDB, p, "Allergy Information");
            }
            //editing status
            else if(attributeToEdit.equals("3"))
            {
                Scanner statusScanner = new Scanner(System.in);
                System.out.println("Enter status: ");
                String status =  statusScanner.nextLine();
                String update = "UPDATE PatientAllergy SET status='" + status + "' WHERE allergyID='" + patientAllergy.getAllergyID() + "'";
                statement.executeUpdate(update);
                System.out.println("Updated allergy information.");
                this.updateAssignedData(connectHISDB, p, "Allergy Information");
            }
            else
            {
                System.out.println("Invalid input.");
            }
            System.out.println("What attributes of this allergy would you like to edit? Select from the following. Exit with -1: ");
            System.out.println("1: Substance");
            System.out.println("2: Reaction");
            System.out.println("3: Status");
            attributeToEdit = allergyScanner.next();
        }
    }
    public void updateAssignedData(Connection connectHISDB, Patient p, String role) throws SQLException
    {
        String patientID = p.getPatientID();
        String authorID = this.getAuthorID();
        PreparedStatement prepStatement = connectHISDB.prepareStatement("INSERT INTO Assigned (authorID, patientID, participatingRole) VALUES(?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE participatingRole=participatingRole");
        prepStatement.setString(1, authorID);
        prepStatement.setString(2, patientID);
        prepStatement.setString(3, role);
        prepStatement.executeUpdate();
    }
    public void insertAuthor(Connection connectHISDB) throws  SQLException
    {
        PreparedStatement authorStmt = connectHISDB.prepareStatement(
                "INSERT INTO Author " +
                        "(authorID, authorTitle, authorFirstName, authorLastName) "
                        + "VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE authorID=authorID, authorFirstName=authorFirstName, authorLastName=authorLastName, authorTitle=authorTitle");

        authorStmt.setString(1, this.getAuthorID());
        authorStmt.setString(2, this.getAuthorTitle());
        authorStmt.setString(3, this.getAuthorFirstName());
        authorStmt.setString(4, this.getAuthorLastName());

        authorStmt.executeUpdate();
    }
}
