package edu.ucsb.cs;

import java.sql.*;
import java.util.Scanner;

/**
 * Created by ianvernon on 3/12/15.
 */
public class AdminCase
{
    public AdminCase()
    {
        this.run();
    }
    public void run()
    {
        try
        {
            //set up JDBC connections
            Class.forName("com.mysql.jdbc.Driver");
            Connection connectHISDB;


            connectHISDB = DriverManager.getConnection("jdbc:mysql://localhost/HealthInformationSystem?"
                    + "user=root&password=");

            Scanner adminScanner = new Scanner(System.in);
            displayAdminMenu();

            String adminInput = adminScanner.next();
            while(!adminInput.equals("-1"))
            {
                // # of patients for each type of allergy
                if(adminInput.equals("1"))
                {
                    numPatientsPerAllergy(connectHISDB);
                }
                // list # of patients who have more than one allergy
                else if(adminInput.equals("2"))
                {
                    numPatientsGT1Allergy(connectHISDB);
                }
                // list patients who have a plan for surgery today
                else if(adminInput.equals("3"))
                {
                    listSurgeryPatientsToday(connectHISDB);
                }
                // identify authors with more than one patient
                else if(adminInput.equals("4"))
                {
                    listAuthorsWithG1Patient(connectHISDB);
                }
                else
                {
                    System.out.println("Invalid option. Try again.");
                }
                displayAdminMenu();
                adminInput = adminScanner.next();
            }
            System.out.println("***************EXITING ADMIN MENU****************");
            //displayMainMenu();
            return;




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
    public static void displayAdminMenu()
    {
        System.out.println("******** ADMIN MENU ************");
        System.out.println("1: View # of patients for each type of allergy.");
        System.out.println("2: List patients who have more than one allergy.");
        System.out.println("3: List patients who have a plan for surgery today.");
        System.out.println("4: Identify authors with more than one patient.");
        System.out.println("Or, to exit, enter -1.");
    }
    public static void numPatientsPerAllergy(Connection connectHISDB) throws SQLException
    {
        Statement statement = connectHISDB.createStatement();
        String query = "SELECT PA.substance, COUNT(*)  FROM PatientAllergy PA WHERE PA.substance IS NOT NULL GROUP BY PA.substance";
        ResultSet result = statement.executeQuery(query);
        while(result.next())
        {
            System.out.println("Substance: " + result.getString("substance"));
            System.out.println("\tAmount: " + result.getString("COUNT(*)"));
        }
    }
    public static  void numPatientsGT1Allergy(Connection connectHISDB) throws SQLException
    {
        Statement statement = connectHISDB.createStatement();
        String query = "SELECT PA.patientID FROM PatientAllergy PA GROUP BY PA.patientID HAVING COUNT(*) > 1";
        ResultSet result = statement.executeQuery(query);
        System.out.println("Patients with more than one allergy:");
        while(result.next())
        {
            System.out.println("Patient: " + result.getString("patientID"));
        }
    }
    public static void listSurgeryPatientsToday(Connection connectHISDB) throws SQLException
    {
        Statement statement = connectHISDB.createStatement();
        String query = "SELECT PP.patientID FROM PatientPlan PP WHERE PP.date=CURDATE() AND PP.activity='surgery'";
        ResultSet result = statement.executeQuery(query);
        System.out.println("Patients who have 'surgery' today: ");
        while(result.next())
        {
            System.out.println("Patient: " + result.getString("patientID"));
        }
    }
    public static void listAuthorsWithG1Patient(Connection connectHISDB) throws SQLException
    {
        Statement statement = connectHISDB.createStatement();
        String query = "SELECT A.authorID FROM Assigned A GROUP BY A.authorID HAVING COUNT(*) > 1";
        ResultSet result = statement.executeQuery(query);
        System.out.println("Authors with more than one patient: ");
        while(result.next())
        {
            System.out.println("Author: " + result.getString("authorID"));
        }
    }
}
