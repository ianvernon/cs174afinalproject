package edu.ucsb.cs;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

/**
 * Created by ianvernon on 3/12/15.
 */
public class DoctorCase
{
    public DoctorCase()
    {
        this.run();
    }
    public void run()
    {
        try {
            //set up JDBC connections
            Class.forName("com.mysql.jdbc.Driver");
            Connection connectHISDB;


            connectHISDB = DriverManager.getConnection("jdbc:mysql://localhost/HealthInformationSystem?"
                    + "user=root&password=");



            Scanner doctorScanner = new Scanner(System.in);
            String authorID;
            System.out.println("******* DOCTOR/AUTHOR ACCESS *********");
            System.out.println("For all cases, if you enter -1, the program will exit.");
            System.out.println("Please enter your ID: ");
            authorID = doctorScanner.next();
            if(authorID.equals("-1"))
            {
                System.out.println("******************EXITING DOCTOR/AUTHOR MENU*************************");
                //displayMainMenu();
                return;
            }
            Author a = getAuthor(connectHISDB, authorID);
            if(a == null)
            {
                System.out.println("Author not valid. Returning to main menu.");
                //displayMainMenu();
                return;
            }
            // display options to Doctor/Author
            String doctorMenuInput = "-2";
            while(!doctorMenuInput.equals("-1"))
            {
                System.out.println("Okay Author " + a.getAuthorFirstName() + " " + a.getAuthorLastName() + ", what would you like to do? Select from amongst the following options. Input -1 to exit");
                displayDoctorMenu();
                doctorMenuInput = doctorScanner.next();
                if(doctorMenuInput.equals("-1"))
                {
                    System.out.println("*******************EXITING AUTHOR/DOCTOR MENU*****************");
                    //displayMainMenu();
                    return;
                }
                System.out.println("Enter Patient's ID");
                String patientID = doctorScanner.next();
                if(patientID.equals("-1"))
                {
                    System.out.println("*******************EXITING AUTHOR/DOCTOR MENU*****************");
                }
                Patient p = getPatient(connectHISDB, patientID);
                //Author will need to enter PatientID after choosing option in order to pull specific Tuple!!
                 if(p == null)
                 {
                     System.out.println("Patient not valid. Returning to main menu.");
                     return;
                 }
                // view patient record
                if(doctorMenuInput.equals("1"))
                {
                    p.viewPatientRecord(connectHISDB);
                }
                // view authors assigned to this patient / recorded roles
                else if(doctorMenuInput.equals("2"))
                {
                    p.viewPatientAuthors(connectHISDB);
                }
                // view lab test reports
                else if(doctorMenuInput.equals("3"))
                {
                    p.viewPatientLabTestReport(connectHISDB);
                }
                // view allergies
                else if(doctorMenuInput.equals("4"))
                {
                    p.viewPatientAllergies(connectHISDB);
                }
                // view plan
                else if(doctorMenuInput.equals("5"))
                {
                    p.viewPatientPlanInfo(connectHISDB);
                }
                // view guardian information
                else if(doctorMenuInput.equals("6"))
                {
                    p.viewPatientGuardian(connectHISDB);
                }
                // edit patient plan
                else if(doctorMenuInput.equals("7"))
                {
                    a.editPatientPlan(connectHISDB, p);
                }
                //edit a Patients allergies information
                else if(doctorMenuInput.equals("8"))
                {
                    a.editAllergiesInformation(connectHISDB, p);
                }
                else if(doctorMenuInput.equals("9"))
                {
                    p.viewPatientRelatives(connectHISDB);
                }
                else if(doctorMenuInput.equals("10"))
                {
                    p.viewPatientInsuranceCompany(connectHISDB);
                }
            }

        }
        catch(ClassNotFoundException ex)
        {
            System.out.println("Cannot find JDBC class. Exiting.");
            return;
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
            System.out.println("Error executing query. Exiting.");
            return;
        }
    }
    public static void displayDoctorMenu(){

        System.out.println("********* DOCTOR/AUTHOR MENU *********");
        System.out.println("1: View a Patient's record.");
        System.out.println("2: View Authors assigned to a patient and the role they recorded.");
        System.out.println("3: View a Patient's previous Lab Test Reports.");
        System.out.println("4: View a Patient's allergies.");
        System.out.println("5: View a Patient's plans.");
        System.out.println("6: View a Patient's guardian information.");
        System.out.println("7: Edit a Patient's plan.");
        System.out.println("8: Edit a Patient's allergies data.");
        System.out.println("9: View a Patient's relatives.");
        System.out.println("10: View a Patient's insurance company.");

    }
    //done
    public static Author getAuthor(Connection connectHISDB, String authorID) throws SQLException
    {
        Statement statement = connectHISDB.createStatement();
        Scanner authorScanner = new Scanner(System.in);
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Author WHERE authorID='" + authorID + "'");
        while(!resultSet.isBeforeFirst() )
        {
            System.out.println("Provided author ID is not valid. Please try again or enter -1 to exit.");
            authorID = authorScanner.next();
            if(authorID.equals("-1"))
            {
                System.out.println("Exiting getting author information.");
                return null;
            }
            resultSet = statement.executeQuery("SELECT * FROM Author WHERE authorID='" + authorID + "'");
        }
        resultSet.next();
        Author a = new Author(resultSet.getString("authorID"), resultSet.getString("authorTitle"),
                resultSet.getString("authorFirstName"), resultSet.getString("authorLastName"));
        return a;

    }
    public static Patient getPatient(Connection connectHISDB, String patientID) throws SQLException
    {
        Statement statement = connectHISDB.createStatement();
        Scanner patientScanner = new Scanner(System.in);
        //get tuple from Patient table that corresponds to input patient ID
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Patient WHERE patientID='" + patientID + "'");
        // check if result set has anything in it, loop until this occurs or exit with -1
        while(!resultSet.isBeforeFirst() )
        {
            System.out.println("Provided patient ID is not valid. Please try again or enter -1 to exit.");
            patientID = patientScanner.next();
            if(patientID.equals("-1"))
            {
                System.out.println("Exiting from getting patient information.");
                return null;
            }
            resultSet = statement.executeQuery("SELECT * FROM Patient WHERE patientID='" + patientID + "'");
        }

        // put information in patient object for easier access throughout query
        resultSet.next();
        Patient p = new Patient(patientID, resultSet.getString("patientRole"), resultSet.getString("givenName"),
                resultSet.getString("familyName"), resultSet.getString("suffix"), resultSet.getString("gender"),
                resultSet.getString("birthTime"),resultSet.getString("providerid"), resultSet.getString("xmlHealthCreationDate"),
                resultSet.getString("guardianNo"), resultSet.getString("payerID"), resultSet.getString("policyType"),
                resultSet.getString("purpose"));
        return p;
    }

}
