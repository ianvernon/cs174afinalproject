package edu.ucsb.cs;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;


public class Main {

    public static void main(String[] args)
    {

	    //grabData("healthmessagesexchange2", "messages", "HealthInformationSystem");
        mainMenu();

    }
    public static void mainMenu()
    {
        Scanner inFromConsole = new Scanner(System.in);
        System.out.println("************ Welcome to HealthInformationSystem! **************");
        System.out.println("Privilege levels are as follows:");
        System.out.println("\tPatient: 0");
        System.out.println("\tDoctor: 1");
        System.out.println("\tAdministrator: 2");
        System.out.println("Please enter privilege level: ");

        String privilegeLevel = inFromConsole.next();
        boolean isValidPrivillege = validPrivilege(privilegeLevel);
        while(!validPrivilege(privilegeLevel))
        {
            System.out.println("Not a valid privilege. Input -1 to exit. Otherwise, Please try again: ");
            privilegeLevel = inFromConsole.next();
            if(privilegeLevel.equals("-1"))
            {
                return;
            }
        }
        // patient case
        if(privilegeLevel.equals("0"))
        {
            patientCase();
        }
        // doctor case;
        else if(privilegeLevel.equals("1"))
        {
            doctorCase();
        }
        //admin case
        else if(privilegeLevel.equals("2"))
        {

        }
        // should never get here, but just to be safe!
        else
        {
            System.out.println("Invalid privilege. Exiting.");
            return;
        }


    }
    public static boolean validPrivilege(String input)
    {
        if(!(input.equals("0") || input.equals("1") || input.equals("2")))
        {
            return false;
        }

        else
        {
            return true;
        }
    }
    /** returns a formatted string with the current date and time
     *
     * @return  string containing date in Month/Day/Year Hour:Day AM/PM format
     */
    public static String getCurrentDate()
    {
        /*Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH)+1;
        int currentDay = c.get(Calendar.DAY_OF_MONTH);*/
        DateFormat timeFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        String s = timeFormat.format(new java.util.Date());
        return s;
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
            System.out.println("Date of test: " + resultSet.getString("LabTestPerformedDate"));
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
            System.out.println("Substance: " + resultSet.getString("substance"));
            System.out.println("Reaction: " + resultSet.getString("reaction"));
            System.out.println("Status: " + resultSet.getString("status"));
        }
    }
    public static void viewPatientPlanInfo(Connection connectHISDB, Patient p) throws SQLException
    {
        System.out.println("*************** PLAN INFO ************");
        String patientID = p.getPatientID();
        Statement statement = connectHISDB.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM PatientPlan WHERE patientID='" + patientID + "'");
        while(resultSet.next())
        {
            System.out.println("Activity: " + resultSet.getString("activity") + " performed on " + resultSet.getString("date"));
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
        }
        String updateSuffixQuery = "UPDATE Patient SET suffix='" + suffix + "' WHERE patientID='"
                + p.getPatientID() + "'";
        int numRowsUpdated = statement.executeUpdate(updateSuffixQuery);
        if(numRowsUpdated > 0)
        {
            System.out.println("Update successful.");
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
        }
        String updateGenderQuery = "UPDATE Patient SET gender='" + editGenderStr + "' WHERE patientID='"
                + p.getPatientID() + "'";
        int numRowsUpdated = statement.executeUpdate(updateGenderQuery);
        if(numRowsUpdated > 0)
        {
            System.out.println("Update successful.");
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
        }
        String updateLastNameQuery = "UPDATE Patient SET familyName='" + familyName + "' WHERE patientID='" + p.getPatientID() + "'";
        int numRowsUpdated = statement.executeUpdate(updateLastNameQuery);
        if(numRowsUpdated > 0)
        {
            System.out.println("Update successful.");
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
        }
        String firstNameUpdate = "UPDATE Patient SET givenName='" + givenName + "' WHERE patientID='" + p.getPatientID() + "'";
        int numRowsUpdated = statement.executeUpdate(firstNameUpdate);
        if(numRowsUpdated > 0)
        {
            System.out.println("Update successsful.");
        }
        else
        {
            System.out.println("Update failed.");
        }
    }
    public static void editPatientBirthtime(Connection connectHISDB, Patient p) throws SQLException
    {
        System.out.println("Enter date of birth: (mm/dd/yyyy)");
        Scanner birthtimeScanner = new Scanner(System.in);
        String date = birthtimeScanner.next();
        Statement statement = connectHISDB.createStatement();
        while(!date.matches("(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/((19|20)\\d\\d)"))
        {
            System.out.println("Incorrect format. Try again.\nEnter date of birth: (mm/dd/yyyy)");
            date = birthtimeScanner.next();
        }
        System.out.println("Date entered.");
        System.out.println("Enter time of birth: (hh:mm:ss AM/PM)");
        String eatUpNewline = birthtimeScanner.nextLine();
        String time = birthtimeScanner.nextLine();
        while(!time.matches("(1[012]|0?[1-9]):([0-5][0-9]):([0-5][0-9])(\\s)(am|pm|AM|PM)"))
        {
            System.out.println("Incorrect format. Try again.\nEnter time of birth (hh:mm:ss AM/PM)");
            time = birthtimeScanner.nextLine();
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
        else
        {
            System.out.println("Invalid input. Returning to menu.");
        }
    }
    public static void editGuardianGivenName(Connection connectHISDB, Patient p) throws SQLException
    {
        System.out.println("Enter given name: ");
        Scanner editGuardianScanner = new Scanner(System.in);
        Statement statement = connectHISDB.createStatement();
        String givenName = editGuardianScanner.next();
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
    public static void editGuardianFamilyName(Connection connectHISDB, Patient p) throws SQLException
    {
        System.out.println("Enter family name: ");
        Scanner editGuardianScanner = new Scanner(System.in);
        Statement statement = connectHISDB.createStatement();
        String familyName = editGuardianScanner.next();
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
        System.out.println("Enter phone number: ");
        Scanner editGuardianScanner = new Scanner(System.in);
        Statement statement = connectHISDB.createStatement();
        String eatAnyLines = editGuardianScanner.nextLine();
        String phone = editGuardianScanner.nextLine();
        while(!phone.matches("([(]\\d\\d\\d[)]|(\\d\\d\\d))((\\s)(\\d\\d\\d)(\\s)(\\d\\d\\d\\d)|(-)(\\d\\d\\d)(-)(\\d\\d\\d\\d))"))
        {
            System.out.println("Incorrect format, please try again:");
            phone = editGuardianScanner.nextLine();
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
        while(!streetNum.matches("[0-9]+"))
        {
            System.out.println("Please enter a valid number: ");
            streetNum = addressScanner.next();
        }
        // get street name
        String newlineEater = addressScanner.nextLine();
        System.out.println("Enter street name: ");
        streetName = addressScanner.nextLine();
        while(!streetName.matches("[\\w\\s]+"))
        {
            System.out.println("Please use letters only in street names: ");
            streetName = addressScanner.nextLine();
        }

        address = streetNum + " " + streetName;
        System.out.println("Enter city: ");
        city = addressScanner.nextLine();
        while(!city.matches("[\\w\\s]+"))
        {
            System.out.println("Please use letters only in city names: ");
            city = addressScanner.nextLine();
        }
        System.out.println("Enter state's abbreviation: ");
        state = addressScanner.nextLine();
        while(!state.matches("([a-z]|[A-Z])([a-z]|[A-Z])"))
        {
            System.out.println("Please enter state correctly: ");
            state = addressScanner.nextLine();
        }
        System.out.println("Enter zip: ");
        zip = addressScanner.nextLine();
        while(!zip.matches("[0-9]{4,5}"))
        {
            System.out.println("Please enter valid zip code: ");
            zip = addressScanner.nextLine();
        }
        updateAddress.executeUpdate("UPDATE Guardian SET address='" + address + "' WHERE guardianNo='" + guardianNo + "'");
        updateCity.executeUpdate("UPDATE Guardian SET city='" + city + "' WHERE guardianNo='" + guardianNo + "'");
        updateState.executeUpdate("UPDATE Guardian SET state='" + state + "' WHERE guardianNo='" + guardianNo + "'");
        updateZip.executeUpdate("UPDATE Guardian SET zip='" + zip + "' WHERE guardianNo='" + guardianNo + "'");
        System.out.println("Address information updated.");
    }
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
    public static void patientCase()
    {
        try {
            //set up JDBC connections
            Class.forName("com.mysql.jdbc.Driver");
            Connection connectHISDB = null;
            Statement statement = null;

            connectHISDB = DriverManager.getConnection("jdbc:mysql://localhost/HealthInformationSystem?"
                    + "user=root&password=");
            statement = connectHISDB.createStatement();


            Scanner patientScanner = new Scanner(System.in);
            String patientID;
            System.out.println("******* PATIENT ACCESS *********");
            System.out.println("For all cases, if you enter -1, the program will exit.");
            System.out.println("Please enter your ID: ");
            patientID = patientScanner.next();
            if (patientID.equals("-1"))
            {
                return;
            }
            Patient p = getPatient(connectHISDB, patientID);
            if(p == null)
            {
                return;
            }
            String patientMenuInput = "-2";
            //view patient record
            while(!patientMenuInput.equals("-1"))
            {
                System.out.println("Okay patient " + p.getPatientID() + ", what would you like to do? Select from amongst the following options. Input -1 to exit");
                displayPatientMenu();
                patientMenuInput = patientScanner.next();
                // view patient record
                if(patientMenuInput.equals("1"))
                {
                   viewPatientRecord(connectHISDB, p);
                }
                // view authors assigned to this patient / recorded roles
                else if(patientMenuInput.equals("2"))
                {
                    viewPatientAuthors(connectHISDB, p);
                }
                // view lab test reports
                else if(patientMenuInput.equals("3"))
                {
                    viewPatientLabTestReport(connectHISDB, p);
                }
                // view allergies
                else if(patientMenuInput.equals("4"))
                {
                    viewPatientAllergies(connectHISDB, p);
                }
                // view plan
                else if(patientMenuInput.equals("5"))
                {
                   viewPatientPlanInfo(connectHISDB, p);
                }
                // view guardian information
                else if(patientMenuInput.equals("6"))
                {
                    viewPatientGuardian(connectHISDB, p);
                }
                // edit patient information
                else if(patientMenuInput.equals("7"))
                {
                    editPatientInfo(connectHISDB, p);
                }
                //edit guardian information
                else if(patientMenuInput.equals("8"))
                {
                    editGuardianInfo(connectHISDB, p);
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

    public static void displayPatientMenu()
    {
        System.out.println("********* PATIENT MENU *********");
        System.out.println("1: View my Patient record.");
        System.out.println("2: View Authors assigned to me and the role they recorded.");
        System.out.println("3: View my previous Lab Test Reports.");
        System.out.println("4: View my allergies.");
        System.out.println("5: View my plan.");
        System.out.println("6: View my guardian information.");
        System.out.println("7: Edit my information in Patient table.");
        System.out.println("8: Edit my Guardian information.");


    }

    public static void doctorCase(){
        try {
            //set up JDBC connections
            Class.forName("com.mysql.jdbc.Driver");
            ResultSet resultSet = null;
            ResultSet resultSet2 = null;
            Connection connectHISDB = null;
            Statement statement = null;

            connectHISDB = DriverManager.getConnection("jdbc:mysql://localhost/HealthInformationSystem?"
                    + "user=root&password=");
            statement = connectHISDB.createStatement();


            Scanner doctorScanner = new Scanner(System.in);
            String authorID;
            System.out.println("******* DOCTOR/AUTHOR ACCESS *********");
            System.out.println("For all cases, if you enter -1, the program will exit.");
            System.out.println("Please enter your ID: ");
            authorID = doctorScanner.next();
            if (authorID.equals("-1"))
            {
                return;
            }
            //get tuple from Patient table that corresponds to input patient ID
            //Austin: need to validate Author info but access Patients info!!?
            //Austin: Right now it is accessing Authors info as if it was Patient case
            resultSet = statement.executeQuery("SELECT * FROM Author WHERE authorID='" + authorID + "'");
            // check if result set has anything in it, loop until this occurs or exit with -1
            while(!resultSet.isBeforeFirst() )
            {
                System.out.println("Provided author ID is not valid. Please try again or enter -1 to exit.");
                authorID = doctorScanner.next();
                if(authorID.equals("-1"))
                {
                    return;
                }
                resultSet = statement.executeQuery("SELECT * FROM Author WHERE authorID='" + authorID + "'");
            }
            // display options to Doctor/Author
            String doctorMenuInput = "-2";
            while(!doctorMenuInput.equals("-1"))
            {
                System.out.println("Okay Author " + authorID + ", what would you like to do? Select from amongst the following options. Input -1 to exit");
                displayDoctorMenu();
                doctorMenuInput = doctorScanner.next();
                System.out.println("Enter Patients ID");
                String patientID = doctorScanner.next();
                resultSet2 = statement.executeQuery("SELECT * FROM Patient WHERE PatientID='" + patientID + "'");
                //Author will need to enter PatientID after choosing option in order to pull specific Tuple!!

                // view patient record
                if(doctorMenuInput.equals("1"))
                {
                    // System.out.println("in 1 menu");
                    statement = connectHISDB.createStatement();
                    System.out.println("patientID = " + patientID);
                    System.out.println("********** PATIENT INFO *************");
                    resultSet2 = statement.executeQuery("SELECT * FROM Patient WHERE patientID='" + patientID + "'");
                    resultSet2.next();
                    System.out.println("ID: " + resultSet2.getString("patientID"));
                    System.out.println("Name (f, l): " + resultSet2.getString("givenName") + " " + resultSet2.getString("familyName"));
                    System.out.println("Birth Date / Time: " + resultSet2.getString("birthTime"));
                    System.out.println("ProviderID: " + resultSet2.getString("providerID"));
                    System.out.println("Guardian ID: " + resultSet2.getString("guardianNo"));
                    System.out.println("Payer ID: " + resultSet2.getString("payerID"));
                    System.out.println("Policy Type: " + resultSet2.getString("policyType"));
                    System.out.println("Purpose: " + resultSet2.getString("purpose"));
                    System.out.println("************ END PATIENT INFO ***********");
                }
                // view authors assigned to this patient / recorded roles
                else if(doctorMenuInput.equals("2"))
                {
                    statement = connectHISDB.createStatement();
                    System.out.println("*********** ASSIGNED AUTHORS TO PATIENT ************");
                    resultSet = statement.executeQuery("SELECT * FROM Assigned WHERE patientID='" + patientID + "'");
                    int i = 1;
                    //print information about all authors assigned to patient
                    while(resultSet.next()) {
                        System.out.println("******** AUTHOR " + i + " **********");
                        //Austin: Need to create second authorID variable because patients author could be different than the one currently accessing
                        String authorID2 = resultSet.getString("authorID");
                        Statement authorInfoStmt = connectHISDB.createStatement();
                        ResultSet authorInfoSet = authorInfoStmt.executeQuery("SELECT * FROM Author WHERE authorID='" + authorID2 + "'");
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
                    }
                }
                // view lab test reports
                else if(doctorMenuInput.equals("3"))
                {
                    System.out.println("************** LAB TEST INFO ***************");
                    resultSet = statement.executeQuery("SELECT * FROM LabTestReport WHERE patientID='" + patientID + "'");
                    while(resultSet.next())
                    {
                        System.out.println("TestID: " + resultSet.getString("LabTestResultID"));
                        System.out.println("VisitID: " + resultSet.getString("PatientVisitID"));
                        System.out.println("Date of test: " + resultSet.getString("LabTestPerformedDate"));
                        System.out.println("Type of test: " + resultSet.getString("LabTestType"));
                        System.out.println("Reference Range: " + resultSet.getString("ReferenceRangeLow") + " - " +
                                resultSet.getString("ReferenceRangeHigh"));
                        System.out.println("Test results: " + resultSet.getString("TestResultValue") + "\n");
                    }
                }
                // view allergies
                else if(doctorMenuInput.equals("4"))
                {
                    System.out.println("*************** ALLERGY INFO ************");
                    resultSet = statement.executeQuery("SELECT * FROM PatientAllergy WHERE patientID='" + patientID + "'");
                    while(resultSet.next())
                    {
                        System.out.println("Substance: " + resultSet.getString("substance"));
                        System.out.println("Reaction: " + resultSet.getString("reaction"));
                        System.out.println("Status: " + resultSet.getString("status"));
                    }
                }
                // view plan
                else if(doctorMenuInput.equals("5"))
                {
                    System.out.println("*************** PLAN INFO ************");
                    resultSet = statement.executeQuery("SELECT * FROM PatientPlan WHERE patientID='" + patientID + "'");
                    while(resultSet.next())
                    {
                        System.out.println("Activity: " + resultSet.getString("activity") + " performed on " + resultSet.getString("date"));
                    }
                }
                // view guardian information
                else if(doctorMenuInput.equals("6"))
                {
                    System.out.println("********** GUARDIAN INFO **********");
                    //resultSet = statement.executeQuery("SELECT * FROM Patient WHERE patientID='" + patientID + "'");
                    resultSet.next();
                    String guardianNo = resultSet.getString("guardianNo");
                    resultSet = statement.executeQuery("SELECT * FROM Guardian WHERE guardianNo='" + guardianNo + "'");
                    while(resultSet.next())
                    {
                        System.out.println("Name (f, l): " + resultSet.getString("givenName") + " " + resultSet.getString("familyName"));
                        System.out.println("Address: " + resultSet.getString("address") + "\n" + resultSet.getString("city") +
                                resultSet.getString("state") + " " + resultSet.getString("zip"));
                        System.out.println("Phone: " + resultSet.getString("phone"));
                    }
                }
                // edit patient plan
                else if(doctorMenuInput.equals("7"))
                {
                    // ask what they want to edit - provided number to enter to escape, loop until they do so
                    // make sure length of string is not over 100 characters
                    //Austin: Only need to edit a patients plan in this case! (But how?)
                    Scanner editPatientScanner = new Scanner(System.in);
                    //Can delete everything commented below
                   /* System.out.println("Which of the following would you like to edit? Enter -1 to exit.");
                    System.out.println("1: Suffix");
                    System.out.println("2: Gender");
                    System.out.println("3: Family (Last) Name: ");
                    System.out.println("4: Given (First) Name: ");
                    System.out.println("5: Birth Date / Time: ");
                    String editPatientStr = editPatientScanner.next();
                    if(editPatientStr.equals("1"))
                    {
                        System.out.println("Please enter suffix: ");
                        editPatientStr = editPatientScanner.next();
                        while(editPatientStr.length() > 100)
                        {
                            System.out.println("Too big of a suffix. Try again.");
                            editPatientStr = editPatientScanner.next();
                        }
                        String updateSuffixQuery = "UPDATE Patient SET suffix='" + editPatientStr + "' WHERE patientID='"
                                + patientID + "'";
                        int numRowsUpdated = statement.executeUpdate(updateSuffixQuery);
                        if(numRowsUpdated > 0)
                        {
                            System.out.println("Update successful.");
                        }
                        else
                        {
                            System.out.println("Update failed.");
                        }
                    }
                    else if(editPatientStr.equals("2"))
                    {
                        System.out.println("Please enter gender (Male or Female): ");
                        editPatientStr = editPatientScanner.next();
                        while(editPatientStr.length() > 100)
                        {
                            System.out.println("String size too big. Try again.");
                            editPatientStr = editPatientScanner.next();
                        }
                        String updateGenderQuery = "UPDATE Patient SET gender='" + editPatientStr + "' WHERE patientID='"
                                + patientID + "'";
                        int numRowsUpdated = statement.executeUpdate(updateGenderQuery);
                        if(numRowsUpdated > 0)
                        {
                            System.out.println("Update successful.");
                        }
                        else
                        {
                            System.out.println("Update failed.");
                        }
                    }
                    else if(editPatientStr.equals("3"))
                    {
                        System.out.println("Please enter family (last) name: ");
                        editPatientStr = editPatientScanner.next();
                        while(editPatientStr.length() > 100)
                        {
                            System.out.println("Too long of a string. Try again.");
                            editPatientStr = editPatientScanner.next();
                        }
                        String updateLastNameQuery = "UPDATE Patient SET familyName='" + editPatientStr + "' WHERE patientID='" + patientID + "'";
                        int numRowsUpdated = statement.executeUpdate(updateLastNameQuery);
                        if(numRowsUpdated > 0)
                        {
                            System.out.println("Update successful.");
                        }
                        else
                        {
                            System.out.println("Update failed.");
                        }
                    }
                    else if(editPatientStr.equals("4"))
                    {
                        System.out.println("Please enter given (first) name: ");
                        editPatientStr = editPatientScanner.next();
                        while(editPatientStr.length() > 100)
                        {
                            System.out.println("Too long of a string. Try again.");
                            editPatientStr = editPatientScanner.next();
                        }
                        String firstNameUpdate = "UPDATE Patient SET givenName='" + editPatientStr + "' WHERE patientID='" + patientID + "'";
                        int numRowsUpdated = statement.executeUpdate(firstNameUpdate);
                        if(numRowsUpdated > 0)
                        {
                            System.out.println("Update successsful.");
                        }
                        else
                        {
                            System.out.println("Update failed.");
                        }

                    }
                    else if(editPatientStr.equals("5"))
                    {
                        System.out.println("Enter date of birth: (mm/dd/yyyy)");

                        String date = editPatientScanner.next();
                        while(!date.matches("(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\\\d\\\\d)"))
                        {
                            System.out.println("Incorrect format. Try again.\nEnter date of birth: (mm/dd/yyyy)");
                            date = editPatientScanner.next();
                        }
                        String time = editPatientScanner.next();
                        while(!time.matches("(1[012]|[1-9]):[0-5][0-9]:[0-5][0-9](\\s)?(?i)(am|pm)"))
                        {
                            System.out.println("Incorrect format. Try again.\nEnter time of birth (hh:mm:ss AM/PM");
                            time = editPatientScanner.next();
                        }
                        String combinedDateTime = date +  " " + time;
                        String updateStr = "UPDATE Patient SET birthTime='" + combinedDateTime + "' WHERE patientID='" + patientID + "'";
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
                    else
                    {
                        System.out.println("Invalid input. Returning to menu.");
                    } */
                }
                //edit a Patients allergies information
                else if(doctorMenuInput.equals("8"))
                {
                    // ask what they want to edit - provided number to enter to escape, loop until they do so
                    // make sure length of string is not over 100 characters
                    //Austin: only need to edit Patients allergies data in this case

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
        System.out.println("1: View a Patients record.");
        System.out.println("2: View Authors assigned to a patient and the role they recorded.");
        System.out.println("3: View a Patients previous Lab Test Reports.");
        System.out.println("4: View a Patients allergies.");
        System.out.println("5: View a Patients plan.");
        System.out.println("6: View a Patients guardian information.");
        System.out.println("7: Edit a Patients plan.");
        System.out.println("8: Edit a Patients allergies data.");

    }

    /**
     * grabs data from sourceTable located in sourceDb, and inserts it into destDb
     * @param sourceDb - name of source database which we will be grabbing data from
     * @param sourceTable - name of table within source database that we'll be grabbing data from
     * @param destDb - name of the database that we'll be inserting data into
     */
    public static void grabData(String sourceDb, String sourceTable, String destDb)
    {
        //System.out.println("Hello world!!!!!");

        Connection sourceConnect = null;
        Statement statement = null;
        ResultSet resultSet = null;
        Connection connectHISDB = null;

        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            sourceConnect = DriverManager.getConnection(
                    "jdbc:mysql://localhost/" + sourceDb + "?"+
                            "user=root&password="
            );

            statement = sourceConnect.createStatement();

            resultSet = statement.executeQuery("select * from " + sourceDb + "." + sourceTable);

            connectHISDB = DriverManager.getConnection("jdbc:mysql://localhost/" + destDb + "?"
                    + "user=root&password=");

            while(resultSet.next())
            {

                String currentDateAndTime = getCurrentDate();
                //grab all data from table
                String last_accessed = resultSet.getString("Last_Accessed");
                String patientIdStr = resultSet.getString("patientId");
                int patientIdInt = Integer.parseInt(patientIdStr);
                String givenName = resultSet.getString("GivenName");
                String familyName = resultSet.getString("FamilyName");
                String birthTime = resultSet.getString("BirthTime");
                String providerIdStr = resultSet.getString("providerId");
                //int providerIdInt = Integer.parseInt(providerIdStr);
                String guardianNoStr = resultSet.getString("GuardianNo");
                //int guardianNoInt = Integer.parseInt(guardianNoStr);
                String relationship = resultSet.getString("Relationship");
                String firstName = resultSet.getString("FirstName");
                String lastName = resultSet.getString("LastName");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");
                String city = resultSet.getString("city");
                String state = resultSet.getString("state");
                String zip = resultSet.getString("zip");
                String authorIdStr = resultSet.getString("AuthorId");
                //int authorIdInt = Integer.parseInt(authorIdStr);
                String authorTitle = resultSet.getString("AuthorTitle");
                String authorFirstName = resultSet.getString("AuthorFirstName");
                String authorLastName = resultSet.getString("AuthorLastName");
                String participatingRole = resultSet.getString("ParticipatingRole");
                String payerIdStr = resultSet.getString("PayerId");
                //int payerIdInt = Integer.parseInt(payerIdStr);
                String name = resultSet.getString("Name");
                String policyHolder = resultSet.getString("PolicyHolder");
                String policyType = resultSet.getString("PolicyType");
                String purpose = resultSet.getString("Purpose");
                String relativeIdStr = resultSet.getString("RelativeId");
                //int relativeId = Integer.parseInt("RelativeId");
                String relation = resultSet.getString("Relation");
                String ageStr = resultSet.getString("age");
                //  int age = Integer.parseInt(ageStr);
                String diagnosis = resultSet.getString("Diagnosis");
                String idStr = resultSet.getString("Id");
                // int id = Integer.parseInt(idStr);
                String substance = resultSet.getString("Substance");
                String reaction = resultSet.getString("Reaction");
                String status = resultSet.getString("Status");
                String labTestResultIdStr = resultSet.getString("LabTestResultId");
                // int labTestResultId = Integer.parseInt(labTestResultIdStr);
                String labTestPerformDate = resultSet.getString("LabTestPerformedDate");
                String labTestType = resultSet.getString("LabTestType");
                String testResultValue = resultSet.getString("TestResultValue");
                String referenceRangeHigh = resultSet.getString("ReferenceRangeHigh");
                String referenceRangeLow = resultSet.getString("ReferenceRangeLow");
                String planIdStr = resultSet.getString("PlanId");
                //int planId = Integer.parseInt(planIdStr);
                String activity = resultSet.getString("Activity");
                String scheduledDate = resultSet.getString("ScheduledDate");

                String suffix = "";
                String gender = "";
                String patientRole = "";
                // execute queries

                //TODO: do we need this given current setup?
                if(!last_accessed.equals(currentDateAndTime)) {
                }
                /********* INSURANCE COMPANY ************/
                PreparedStatement icStmt = connectHISDB.prepareStatement(
                        "INSERT INTO InsuranceCompany " +
                                "(payerID, name) VALUES(?, ?) ON DUPLICATE KEY UPDATE payerID=payerID, name=name");

                icStmt.setString(1, payerIdStr);
                icStmt.setString(2, name);

                icStmt.executeUpdate();

                /********* GUARDIAN ***********/

                PreparedStatement guardianStmt = connectHISDB.prepareStatement(
                        "INSERT INTO Guardian " +
                                "(guardianNo, givenName, familyName, phone, address, city, state, zip) " +
                                "VALUES(?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE guardianNo=guardianNo, givenName=givenName, familyName=familyName,"+
                                " phone=phone, address=address, city=city, state=state, zip=zip");
                guardianStmt.setString(1, guardianNoStr);
                guardianStmt.setString(2, givenName);
                guardianStmt.setString(3, familyName);
                guardianStmt.setString(4, phone);
                guardianStmt.setString(5, address);
                guardianStmt.setString(6, city);
                guardianStmt.setString(7, state);
                guardianStmt.setString(8, zip);

                guardianStmt.executeUpdate();

                /********* AUTHOR **********/

                PreparedStatement authorStmt = connectHISDB.prepareStatement(
                        "INSERT INTO Author " +
                                "(authorID, authorTitle, authorFirstName, authorLastName) "
                                + "VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE authorID=authorID, authorFirstName=authorFirstName, authorLastName=authorLastName, authorTitle=authorTitle");

                authorStmt.setString(1, authorIdStr);
                authorStmt.setString(2, authorTitle);
                authorStmt.setString(3, authorFirstName);
                authorStmt.setString(4, authorLastName);

                authorStmt.executeUpdate();


                /************* PATIENT ******************/

                PreparedStatement patientStmt = connectHISDB.prepareStatement(
                        "INSERT INTO Patient " +
                                "(patientID, suffix, familyName, givenName, gender, birthTime, providerID, xmlHealthCreationDate, guardianNo, payerID, patientRole, policyType, purpose) " +
                                " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE patientID=patientID, xmlHealthCreationDate=xmlHealthCreationDate,"+
                                "suffix=suffix, familyName=familyName, givenName=givenName, gender=gender, birthTime=birthTime, providerID=providerID, guardianNo=guardianNo," +
                                "payerID=payerID, patientRole=patientRole, policyType=policyType, purpose=purpose");

                patientStmt.setString(1, patientIdStr);
                patientStmt.setString(2, suffix);
                patientStmt.setString(3, familyName);
                patientStmt.setString(4, givenName);
                patientStmt.setString(5, gender);
                patientStmt.setString(6, birthTime);
                patientStmt.setString(7, providerIdStr);
                patientStmt.setString(8, currentDateAndTime);
                patientStmt.setString(9, guardianNoStr);
                patientStmt.setString(10, payerIdStr);
                patientStmt.setString(11, patientRole);
                patientStmt.setString(12, policyType);
                patientStmt.setString(13, purpose);

                patientStmt.executeUpdate();

                /************** ASSIGNED **************/

                PreparedStatement assignedStmt = connectHISDB.prepareStatement(
                        "INSERT INTO Assigned " +
                                "(authorID, patientID, participatingRole)" +
                                " VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE authorID=authorID , patientID=patientID, participatingRole=participatingRole");

                assignedStmt.setString(1, authorIdStr);
                assignedStmt.setString(2, patientIdStr);
                assignedStmt.setString(3, participatingRole);

                assignedStmt.executeUpdate();

                /********* LABTESTREPORT ***********/

                PreparedStatement ltrStmt = connectHISDB.prepareStatement(
                        "INSERT INTO LabTestReport " +
                                "(LabTestResultID, PatientVisitID, LabTestPerformedDate, LabTestType, ReferenceRangeLow, ReferenceRangeHigh, TestResultValue, patientID) "
                                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE LabTestResultID=LabTestResultID, PatientVisitID=PatientVisitID, LabTestType=LabTestType, ReferenceRangeLow=ReferenceRangeLow, ReferenceRangeHigh=ReferenceRangeHigh, TestResultValue=TestResultValue, patientID=patientID");

                ltrStmt.setString(1, labTestResultIdStr);
                ltrStmt.setString(2, patientIdStr);
                ltrStmt.setString(3, labTestPerformDate);
                ltrStmt.setString(4, labTestType);
                ltrStmt.setString(5, referenceRangeLow);
                ltrStmt.setString(6, referenceRangeHigh);
                ltrStmt.setString(7, testResultValue);
                ltrStmt.setString(8, patientIdStr);

                ltrStmt.executeUpdate();

                System.out.println(familyName);

                /*********** PatientRelative ***********/

                PreparedStatement pRStatement = connectHISDB.prepareStatement(
                        "INSERT INTO PatientRelative " +
                                "(relativeID, age, diagnosis, patientID, relationship) " +
                                "VALUES(?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE relativeID=relativeID , patientID=patientID , diagnosis=diagnosis, age=age, relationship=relationship");

                pRStatement.setString(1, relativeIdStr);
                pRStatement.setString(2, ageStr);
                pRStatement.setString(3, diagnosis);
                pRStatement.setString(4, patientIdStr);
                pRStatement.setString(5, relationship);

                pRStatement.executeUpdate();

                /******** PatientAllergy ************/

                PreparedStatement pAStatement = connectHISDB.prepareStatement(
                        "INSERT INTO PatientAllergy " +
                                "(allergyID, substance, reaction, status, patientID) " +
                                "VALUES(?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE allergyID=allergyID, substance=substance, reaction=reaction, status=status, patientID=patientID");

                pAStatement.setString(1, idStr);
                pAStatement.setString(2, substance);
                pAStatement.setString(3, reaction);
                pAStatement.setString(4, status);
                pAStatement.setString(5, patientIdStr);

                pAStatement.executeUpdate();

                /***** PatientPlan *********/

                PreparedStatement planStatement = connectHISDB.prepareStatement(
                        "INSERT INTO PatientPlan " +
                                "(planID, date, activity, patientID)  "  +
                                "VALUES(?, ?, ?, ?) ON DUPLICATE KEY UPDATE planID=planID, date=date, activity=activity, patientID=patientID");

                if(planIdStr == null)
                {
                    planIdStr = "000";
                }
                planStatement.setString(1, planIdStr);
                planStatement.setString(2, scheduledDate);
                planStatement.setString(3, activity);
                planStatement.setString(4, patientIdStr);

                planStatement.executeUpdate();


                /********* UPDATE LAST ACCESSED FOR CURRENT ROW IN RESULTSET *********/

                PreparedStatement updateLastAccessed = sourceConnect.prepareStatement("UPDATE messages SET Last_Accessed='" + currentDateAndTime
                + "' WHERE patientId='" + patientIdStr+ "'");

                updateLastAccessed.executeUpdate();



            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
