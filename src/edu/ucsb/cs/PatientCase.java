package edu.ucsb.cs;

import java.sql.*;
import java.util.Scanner;

/**
 * Created by ianvernon on 3/12/15.
 */
public class PatientCase
{
    public PatientCase()
    {
        this.run();
    }
    public void run() {
        try {
            //set up JDBC connections
            Class.forName("com.mysql.jdbc.Driver");
            Connection connectHISDB;

            connectHISDB = DriverManager.getConnection("jdbc:mysql://localhost/HealthInformationSystem?"
                    + "user=root&password=");


            Scanner patientScanner = new Scanner(System.in);
            String patientID;
            System.out.println("******* PATIENT ACCESS *********");
            System.out.println("For all cases, if you enter -1, the program will exit.");
            System.out.println("Please enter your ID: ");
            patientID = patientScanner.next();
            if (patientID.equals("-1")) {
                System.out.println("*********EXITING PATIENT ACCESS**********");
                //displayMainMenu();
                return;
            }
            Patient p = getPatient(connectHISDB, patientID);
            if (p == null) {
                System.out.println("Returning to main menu.");
                // displayMainMenu();
                return;
            }
            String patientMenuInput = "-2";
            //view patient record
            while (!patientMenuInput.equals("-1")) {
                System.out.println("Okay " + p.getGivenName() + " " + p.getFamilyName() + ", what would you like to do? Select from amongst the following options. Input -1 to exit");
                displayPatientMenu();
                patientMenuInput = patientScanner.next();
                // view patient record
                if (patientMenuInput.equals("1")) {
                    p.viewPatientRecord(connectHISDB);
                }
                // view authors assigned to this patient / recorded roles
                else if (patientMenuInput.equals("2")) {
                    p.viewPatientAuthors(connectHISDB);
                }
                // view lab test reports
                else if (patientMenuInput.equals("3")) {
                    p.viewPatientLabTestReport(connectHISDB);
                }
                // view allergies
                else if (patientMenuInput.equals("4")) {
                    p.viewPatientAllergies(connectHISDB);
                }
                // view plan
                else if (patientMenuInput.equals("5")) {
                    p.viewPatientPlanInfo(connectHISDB);
                }
                // view relatives
                else if (patientMenuInput.equals("6")) {
                    p.viewPatientRelatives(connectHISDB);
                }
                //view my insurance company
                else if (patientMenuInput.equals("7")) {
                    p.viewPatientInsuranceCompany(connectHISDB);
                }
                // view guardian information
                else if (patientMenuInput.equals("8")) {
                    p.viewPatientGuardian(connectHISDB);
                }
                // edit patient information
                else if (patientMenuInput.equals("9")) {
                    p.editPatientInfo(connectHISDB);
                }
                //edit guardian information
                else if (patientMenuInput.equals("10")) {
                    p.editGuardianInfo(connectHISDB);
                } else if (patientMenuInput.equals("-1")) {
                    //do nothing
                } else {
                    System.out.println("Invalid option. Try again.");
                }
            }
            System.out.println("*************EXITING PATIENT MENU**************");
            //displayMainMenu();

        } catch (ClassNotFoundException ex) {
            System.out.println("Cannot find JDBC class. Exiting.");
            return;
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error executing query. Exiting.");
            return;
        }
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
    //done
    //done
    public static void displayPatientMenu()
    {
        System.out.println("********* PATIENT MENU *********");
        System.out.println("1: View my Patient record.");
        System.out.println("2: View Authors assigned to me and the role they recorded.");
        System.out.println("3: View my previous Lab Test Reports.");
        System.out.println("4: View my allergies.");
        System.out.println("5: View my plan.");
        System.out.println("6: View my relatives.");
        System.out.println("7: View my insurance company.");
        System.out.println("8: View my guardian information.");
        System.out.println("9: Edit my information in Patient table.");
        System.out.println("10: Edit my Guardian information.");


    }
    //done
}
