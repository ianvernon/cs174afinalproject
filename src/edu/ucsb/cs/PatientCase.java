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
    public void run()
    {
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
            if (patientID.equals("-1"))
            {
                System.out.println("*********EXITING PATIENT ACCESS**********");
                //displayMainMenu();
                return;
            }
            Patient p = getPatient(connectHISDB, patientID);
            if(p == null)
            {
                System.out.println("Returning to main menu.");
               // displayMainMenu();
                return;
            }
            String patientMenuInput = "-2";
            //view patient record
            while(!patientMenuInput.equals("-1"))
            {
                System.out.println("Okay " + p.getGivenName() + " " + p.getFamilyName() + ", what would you like to do? Select from amongst the following options. Input -1 to exit");
                displayPatientMenu();
                patientMenuInput = patientScanner.next();
                // view patient record
                if(patientMenuInput.equals("1"))
                {
                    p.viewPatientRecord(connectHISDB);
                }
                // view authors assigned to this patient / recorded roles
                else if(patientMenuInput.equals("2"))
                {
                    p.viewPatientAuthors(connectHISDB);
                }
                // view lab test reports
                else if(patientMenuInput.equals("3"))
                {
                    p.viewPatientLabTestReport(connectHISDB);
                }
                // view allergies
                else if(patientMenuInput.equals("4"))
                {
                    p.viewPatientAllergies(connectHISDB);
                }
                // view plan
                else if(patientMenuInput.equals("5"))
                {
                    p.viewPatientPlanInfo(connectHISDB);
                }
                // view relatives
                else if(patientMenuInput.equals("6"))
                {
                    p.viewPatientRelatives(connectHISDB);
                }
                //view my insurance company
                else if(patientMenuInput.equals("7"))
                {
                    p.viewPatientInsuranceCompany(connectHISDB);
                }
                // view guardian information
                else if(patientMenuInput.equals("8"))
                {
                    p.viewPatientGuardian(connectHISDB);
                }
                // edit patient information
                else if(patientMenuInput.equals("9"))
                {
                    p.editPatientInfo(connectHISDB);
                }
                //edit guardian information
                else if(patientMenuInput.equals("10"))
                {
                    p.editGuardianInfo(connectHISDB);
                }
                else if(patientMenuInput.equals("-1"))
                {
                    //do nothing
                }
                else
                {
                    System.out.println("Invalid option. Try again.");
                }
            }
            System.out.println("*************EXITING PATIENT MENU**************");
            //displayMainMenu();

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
    public static void viewPatientRecord(Connection connectHISDB, Patient p) throws SQLException
    {
        // System.out.println("in 1 menu");
        Statement statement = connectHISDB.createStatement();
        String patientID = p.getPatientID();
        //System.out.println("patientID = " + patientID);
        System.out.println("********** PATIENT INFO *************");
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Patient WHERE patientID='" + patientID + "'");
        resultSet.next();
        System.out.println("ID: " + resultSet.getString("patientID"));
        System.out.println("Name (f, l): " + resultSet.getString("givenName") + " " + resultSet.getString("familyName"));
        System.out.println("Birth Date / Time: " + resultSet.getString("birthTime"));
        System.out.println("ProviderID: " + resultSet.getString("providerID"));
        System.out.println("Guardian ID: " + resultSet.getString("guardianNo"));
        System.out.println("Payer ID: " + resultSet.getString("payerID"));
        System.out.println("Policy Type: " + resultSet.getString("policyType"));
        System.out.println("Purpose: " + resultSet.getString("purpose"));
        System.out.println("************ END PATIENT INFO ***********");
    }
    //DONE
    public static void viewPatientAuthors(Connection connectHISDB, Patient p) throws SQLException
    {
        Statement statement = connectHISDB.createStatement();
        System.out.println("*********** ASSIGNED AUTHORS TO PATIENT ************");
        String patientID = p.getPatientID();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Assigned WHERE patientID='" + patientID + "'");
        int i = 1;
        //print information about all authors assigned to patient
        while(resultSet.next()) {
            System.out.println("******** AUTHOR " + i + " **********");
            String authorID = resultSet.getString("authorID");
            Statement authorInfoStmt = connectHISDB.createStatement();
            ResultSet authorInfoSet = authorInfoStmt.executeQuery("SELECT * FROM Author WHERE authorID='" + authorID + "'");
            authorInfoSet.next();
            String authorTitle = authorInfoSet.getString("authorTitle");
            //account for if authorTitle field is null - which it is for whatever reason in the data they give us
            if (authorTitle != null) {
                System.out.println("Author " + authorInfoSet.getString("authorTitle") + " " + authorInfoSet.getString("authorFirstName") + " " +
                        authorInfoSet.getString("authorLastName") + " recorded information regarding " + resultSet.getString("participatingRole"));

            }
            else
            {
                System.out.println("Author " + authorInfoSet.getString("authorFirstName") + " " +
                        authorInfoSet.getString("authorLastName") + " recorded information regarding " + resultSet.getString("participatingRole"));
            }
            i++;
        }
    }

    public static void viewPatientLabTestReport(Connection connectHISDB, Patient p) throws SQLException
    {
        System.out.println("************** LAB TEST INFO ***************");
        Statement statement = connectHISDB.createStatement();
        String patientID = p.getPatientID();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM LabTestReport WHERE patientID='" + patientID + "'");
        while(resultSet.next())
        {
            System.out.println("TestID: " + resultSet.getString("LabTestResultID"));
            System.out.println("VisitID: " + resultSet.getString("PatientVisitID"));
            System.out.println("Date of test: " + resultSet.getDate("LabTestPerformedDate"));
            System.out.println("Type of test: " + resultSet.getString("LabTestType"));
            System.out.println("Reference Range: " + resultSet.getString("ReferenceRangeLow") + " - " +
                    resultSet.getString("ReferenceRangeHigh"));
            System.out.println("Test results: " + resultSet.getString("TestResultValue") + "\n");
        }
    }
    public static void viewPatientAllergies(Connection connectHISDB, Patient p) throws SQLException
    {
        System.out.println("*************** ALLERGY INFO ************");
        Statement statement = connectHISDB.createStatement();
        String patientID = p.getPatientID();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM PatientAllergy WHERE patientID='" + patientID + "'");
        while(resultSet.next())
        {
            System.out.println("AllergyID " + resultSet.getString("allergyID"));
            System.out.println("Substance: " + resultSet.getString("substance"));
            System.out.println("Reaction: " + resultSet.getString("reaction"));
            System.out.println("Status: " + resultSet.getString("status"));
        }
    }
    public static void viewPatientRelatives(Connection connectHISDB, Patient p) throws SQLException
    {
        System.out.println("***************RELATIVE INFO**************");
        Statement statement = connectHISDB.createStatement();
        String patientID = p.getPatientID();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM PatientRelative WHERE patientID='" + patientID + "'");
        while(resultSet.next())
        {
            System.out.println("Relation: " + resultSet.getString("relationship"));
            System.out.println("Diagnosis: " + resultSet.getString("diagnosis"));
            System.out.println("Age: " + resultSet.getString("age"));
        }
    }
    public static void viewPatientInsuranceCompany(Connection connectHISDB, Patient p) throws SQLException
    {
        System.out.println("********** INSURANCE COMPANY INFORMATION ************");
        Statement statement = connectHISDB.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM InsuranceCompany WHERE payerID='" + p.getPayerID() + "'");
        while(resultSet.next())
        {
            System.out.println("Patient's insurance company is " + resultSet.getString("name"));
        }
        System.out.println("******** FINISH INSURANCE COMPANY INFORMATION************");
    }
    public static void viewPatientPlanInfo(Connection connectHISDB, Patient p) throws SQLException
    {
        System.out.println("*************** PLAN INFO ************");
        String patientID = p.getPatientID();
        Statement statement = connectHISDB.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM PatientPlan WHERE patientID='" + patientID + "'");
        while(resultSet.next())
        {
            System.out.println("PlanID: " + resultSet.getString("planID") + "\n\tActivity: " + resultSet.getString("activity") + " performed on " + resultSet.getDate("date"));
        }
    }
    public static void viewPatientGuardian(Connection connectHISDB, Patient p) throws SQLException
    {
        System.out.println("********** GUARDIAN INFO **********");
        //resultSet = statement.executeQuery("SELECT * FROM Patient WHERE patientID='" + patientID + "'");
        String guardianNo = p.getGuardianNo();
        Statement statement = connectHISDB.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Guardian WHERE guardianNo='" + guardianNo + "'");
        while(resultSet.next())
        {
            System.out.println("Name (f, l): " + resultSet.getString("givenName") + " " + resultSet.getString("familyName"));
            System.out.println("Address: " + resultSet.getString("address") + "\n" + resultSet.getString("city") + " " +
                    resultSet.getString("state") + " " + resultSet.getString("zip"));
            System.out.println("Phone: " + resultSet.getString("phone"));
        }
    }
    public static void editPatientSuffix(Connection connectHISDB, Patient p) throws SQLException
    {
        System.out.println("Please enter suffix: ");
        Scanner suffixScanner = new Scanner(System.in);
        String suffix = suffixScanner.next();
        Statement statement = connectHISDB.createStatement();
        while(suffix.length() > 100)
        {
            System.out.println("Too big of a suffix. Try again.");
            suffix = suffixScanner.next();
            if(suffix.equals("-1"))
            {
                return;
            }
        }
        String updateSuffixQuery = "UPDATE Patient SET suffix='" + suffix + "' WHERE patientID='"
                + p.getPatientID() + "'";
        int numRowsUpdated = statement.executeUpdate(updateSuffixQuery);
        if(numRowsUpdated > 0)
        {
            System.out.println("Update successful.");
            p.setSuffix(suffix);

        }
        else
        {
            System.out.println("Update failed.");
        }
    }
    public static void editPatientGender(Connection connectHISDB, Patient p) throws SQLException
    {
        System.out.println("Please enter gender (Male or Female): ");
        Scanner editGenderScanner = new Scanner(System.in);
        String editGenderStr = editGenderScanner.next();
        Statement statement = connectHISDB.createStatement();
        while(editGenderStr.length() > 100)
        {
            System.out.println("String size too big. Try again.");
            editGenderStr = editGenderScanner.next();
            if(editGenderStr.equals("-1"))
            {
                return;
            }
        }
        String updateGenderQuery = "UPDATE Patient SET gender='" + editGenderStr + "' WHERE patientID='"
                + p.getPatientID() + "'";
        int numRowsUpdated = statement.executeUpdate(updateGenderQuery);
        if(numRowsUpdated > 0)
        {
            System.out.println("Update successful.");
            p.setGender(editGenderStr);
        }
        else
        {
            System.out.println("Update failed.");
        }
    }
    public static void editPatientFamilyName(Connection connectHISDB, Patient p) throws  SQLException
    {
        System.out.println("Please enter family (last) name: ");
        Scanner familyNameScanner = new Scanner(System.in);
        String familyName = familyNameScanner.next();
        Statement statement = connectHISDB.createStatement();
        while(familyName.length() > 100)
        {
            System.out.println("Too long of a string. Try again.");
            familyName= familyNameScanner.next();
            if(familyName.equals("-1"))
            {
                return;
            }
        }
        String updateLastNameQuery = "UPDATE Patient SET familyName='" + familyName + "' WHERE patientID='" + p.getPatientID() + "'";
        int numRowsUpdated = statement.executeUpdate(updateLastNameQuery);
        if(numRowsUpdated > 0)
        {
            System.out.println("Update successful.");
            p.setFamilyName(familyName);
        }
        else
        {
            System.out.println("Update failed.");
        }
    }
    public static void editPatientGivenName(Connection connectHISDB, Patient p) throws  SQLException
    {
        System.out.println("Please enter given (fist) name: ");
        Scanner givenNameScanner = new Scanner(System.in);
        String givenName = givenNameScanner.next();
        Statement statement = connectHISDB.createStatement();
        while(givenName.length() > 100)
        {
            System.out.println("Too long of a string. Try again.");
            givenName = givenNameScanner.next();
            if(givenName.equals("-1"))
            {
                return;
            }

        }
        String firstNameUpdate = "UPDATE Patient SET givenName='" + givenName + "' WHERE patientID='" + p.getPatientID() + "'";
        int numRowsUpdated = statement.executeUpdate(firstNameUpdate);
        if(numRowsUpdated > 0)
        {
            System.out.println("Update successsful.");
            p.setGivenName(givenName);
        }
        else
        {
            System.out.println("Update failed.");
        }
    }
    //DONE
    public static void editPatientBirthtime(Connection connectHISDB, Patient p) throws SQLException
    {
        System.out.println("Enter date of birth: (mm/dd/yyyy)");
        Scanner birthtimeScanner = new Scanner(System.in);
        String date = birthtimeScanner.next();
        Statement statement = connectHISDB.createStatement();
        while(!date.matches("(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/((19|20)\\d\\d)"))
        {
            System.out.println("Incorrect format. Try again.\nEnter date: (mm/dd/yyyy)");
            date = birthtimeScanner.next();
            if(date.equals("-1"))
            {
                return;
            }
        }
        System.out.println("Date entered.");
        System.out.println("Enter time: (hh:mm:ss AM/PM)");
        String eatUpNewline = birthtimeScanner.nextLine();
        String time = birthtimeScanner.nextLine();
        while(!time.matches("(1[012]|0?[1-9]):([0-5][0-9]):([0-5][0-9])(\\s)(am|pm|AM|PM)"))
        {
            System.out.println("Incorrect format. Try again.\nEnter time: (hh:mm:ss AM/PM)");
            time = birthtimeScanner.nextLine();
            if(time.equals("-1"))
            {
                return;
            }
        }
        String combinedDateTime = date +  " " + time;
        String updateStr = "UPDATE Patient SET birthTime='" + combinedDateTime + "' WHERE patientID='" + p.getPatientID() + "'";
        int numRowsUpdated = statement.executeUpdate(updateStr);
        if(numRowsUpdated > 0)
        {
            System.out.println("Update successful.");
        }
        else
        {
            System.out.println("Update failed.");
        }

    }
    //DONE
    public static void editPatientInfo(Connection connectHISDB, Patient p) throws SQLException
    {
        Scanner editPatientScanner = new Scanner(System.in);
        System.out.println("Which of the following would you like to edit? Enter -1 to exit.");
        System.out.println("1: Suffix");
        System.out.println("2: Gender");
        System.out.println("3: Family (Last) Name: ");
        System.out.println("4: Given (First) Name: ");
        System.out.println("5: Birth Date / Time: ");
        String editPatientStr = editPatientScanner.next();
        //edit suffix
        if(editPatientStr.equals("1"))
        {
            editPatientSuffix(connectHISDB, p);
        }
        // edit gender
        else if(editPatientStr.equals("2"))
        {
            editPatientGender(connectHISDB, p);
        }
        // edit family name
        else if(editPatientStr.equals("3"))
        {
            editPatientFamilyName(connectHISDB, p);
        }
        // edit given name
        else if(editPatientStr.equals("4"))
        {
            editPatientGivenName(connectHISDB, p);

        }
        // edit birth time
        else if(editPatientStr.equals("5"))
        {
            editPatientBirthtime(connectHISDB, p);
        }
        else if(editPatientStr.equals("-1"))
        {
            System.out.println("Exiting. Returning to Patient access menu.");
        }
        else
        {
            System.out.println("Invalid input. Returning to Patient access menu.");
        }
    }
    //DONE
    public static void editGuardianInfo(Connection connectHISDB, Patient p) throws SQLException
    {
        // ask what they want to edit - provided number to enter to escape, loop until they do so
        // make sure length of string is not over 100 characters
        //can edit guardian's givenName, familyName, phone, address, city, state, zip
        // should we check for information about strings they enter in?
        Scanner editGuardianScanner = new Scanner(System.in);
        System.out.println("Which of the following would you like to edit? Enter -1 to exit.");
        System.out.println("1: Edit guardian's given (first) name.");
        System.out.println("2: Edit guardian's family (last) name.");
        System.out.println("3: Edit guardian's phone number.");
        System.out.println("4: Edit guardian's address.");
        String editGuardianStr = editGuardianScanner.next();
        //  given name
        if(editGuardianStr.equals("1"))
        {
            editGuardianGivenName(connectHISDB, p);
        }
        // last name
        else if(editGuardianStr.equals("2"))
        {
            editGuardianFamilyName(connectHISDB, p);

        }
        // phone number
        else if(editGuardianStr.equals("3"))
        {
            editGuardianPhone(connectHISDB, p);
        }
        // address
        else if(editGuardianStr.equals("4"))
        {
            editGuardianAddress(connectHISDB, p);
        }
        else if(editGuardianStr.equals("-1"))
        {
            System.out.println("Exiting and returning to Patient access menu.");
        }
    }
    public static void editGuardianGivenName(Connection connectHISDB, Patient p) throws SQLException
    {
        System.out.println("Enter given name: ");
        Scanner editGuardianScanner = new Scanner(System.in);
        Statement statement = connectHISDB.createStatement();
        String givenName = editGuardianScanner.next();
        if(givenName.equals("-1"))
        {
            return;
        }
        String updateGuardianStr = "UPDATE Guardian SET givenName='" + givenName + "' WHERE guardianNo='" + p.getGuardianNo() + "'";
        int numRowsUpdated = statement.executeUpdate(updateGuardianStr);
        if(numRowsUpdated > 0)
        {
            System.out.println("Update successful.");

        }
        else
        {
            System.out.println("Update failed.");
        }
    }
    //DONE
    public static void editGuardianFamilyName(Connection connectHISDB, Patient p) throws SQLException
    {
        System.out.println("Enter family name: ");
        Scanner editGuardianScanner = new Scanner(System.in);
        Statement statement = connectHISDB.createStatement();
        String familyName = editGuardianScanner.next();
        if(familyName.equals("-1"))
        {
            return;
        }
        String updateGuardianStr = "UPDATE Guardian SET familyName='" + familyName + "' WHERE guardianNo='" + p.getGuardianNo() + "'";
        int numRowsUpdated = statement.executeUpdate(updateGuardianStr);
        if(numRowsUpdated > 0)
        {
            System.out.println("Update successful.");
        }
        else
        {
            System.out.println("Update failed.");
        }
    }
    public static void editGuardianPhone(Connection connectHISDB, Patient p) throws SQLException
    {
        System.out.println("Enter phone number (format (123) 456-7890: ");
        Scanner editGuardianScanner = new Scanner(System.in);
        Statement statement = connectHISDB.createStatement();
        //String eatAnyLines = editGuardianScanner.nextLine();
        String phone = editGuardianScanner.nextLine();
        if(phone.equals("-1"))
        {
            return;
        }
        while(!phone.matches("([(]\\d\\d\\d[)]|(\\d\\d\\d))((\\s)(\\d\\d\\d)(\\s)(\\d\\d\\d\\d)|(-)(\\d\\d\\d)(-)(\\d\\d\\d\\d))"))
        {
            System.out.println("Incorrect format, please try again:");
            phone = editGuardianScanner.nextLine();
            if(phone.equals("-1"))
            {
                return;
            }
        }
        String updateGuardianStr = "UPDATE Guardian SET phone='" + phone + "' WHERE guardianNo='" + p.getGuardianNo() + "'";
        int numRowsUpdated = statement.executeUpdate(updateGuardianStr);
        if(numRowsUpdated > 0)
        {
            System.out.println("Update successful.");
        }
        else
        {
            System.out.println("Update failed.");
        }
    }
    public static void editGuardianAddress(Connection connectHISDB, Patient p) throws SQLException
    {
        Statement updateAddress = connectHISDB.createStatement();
        Statement updateCity = connectHISDB.createStatement();
        Statement updateState = connectHISDB.createStatement();
        Statement updateZip = connectHISDB.createStatement();

        Scanner addressScanner = new Scanner(System.in);
        String guardianNo = p.getGuardianNo();
        String address, streetNum, streetName, city, state, zip;
        // get street number
        System.out.println("Enter street number: ");
        streetNum = addressScanner.next();
        if(streetNum.equals("-1"))
        {
            return;
        }
        while(!streetNum.matches("[0-9]+"))
        {
            System.out.println("Please enter a valid number: ");
            streetNum = addressScanner.next();
            if(streetNum.equals("-1"))
            {
                return;
            }
        }
        // get street name
        String newlineEater = addressScanner.nextLine();
        System.out.println("Enter street name: ");
        streetName = addressScanner.nextLine();
        while(!streetName.matches("[\\w\\s]+"))
        {
            System.out.println("Please use letters only in street names: ");
            streetName = addressScanner.nextLine();
            if(streetName.equals("-1"))
            {
                return;
            }
        }

        address = streetNum + " " + streetName;
        System.out.println("Enter city: ");
        city = addressScanner.nextLine();
        if(city.equals("-1"))
        {
            return;
        }
        while(!city.matches("[\\w\\s]+"))
        {
            System.out.println("Please use letters only in city names: ");
            city = addressScanner.nextLine();
            if(city.equals("-1"))
            {
                return;
            }
        }
        System.out.println("Enter state's abbreviation: ");
        state = addressScanner.nextLine();
        if(city.equals("-1"))
        {
            return;
        }
        while(!state.matches("([a-z]|[A-Z])([a-z]|[A-Z])"))
        {
            System.out.println("Please enter state correctly: ");
            state = addressScanner.nextLine();
            if(city.equals("-1"))
            {
                return;
            }
        }
        System.out.println("Enter zip: ");
        zip = addressScanner.nextLine();
        if(zip.equals("-1"))
        {
            return;
        }
        while(!zip.matches("[0-9]{4,5}"))
        {
            System.out.println("Please enter valid zip code: ");
            zip = addressScanner.nextLine();
            if(zip.equals("-1"))
            {
                return;
            }
        }
        updateAddress.executeUpdate("UPDATE Guardian SET address='" + address + "' WHERE guardianNo='" + guardianNo + "'");
        updateCity.executeUpdate("UPDATE Guardian SET city='" + city + "' WHERE guardianNo='" + guardianNo + "'");
        updateState.executeUpdate("UPDATE Guardian SET state='" + state + "' WHERE guardianNo='" + guardianNo + "'");
        updateZip.executeUpdate("UPDATE Guardian SET zip='" + zip + "' WHERE guardianNo='" + guardianNo + "'");
        System.out.println("Address information updated.");
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
