package edu.ucsb.cs;

import java.sql.*;
import java.util.Date;
import java.util.Scanner;

public class Patient
{
    private String patientID; //check
    private String patientRole; //check
    private String givenName; //check
    private String familyName; //check
    private String suffix; //check
    private String gender; // check
    private String birthtime; //check
    private String providerId; //check
    private String xmlCreationDate; // check
    private String guardianNo; // check
    private String payerID; // check
    private String policyType; // check
    private String purpose; //chcek
    public Patient(String ID, String patientrole, String givenName, String familyname, String suffix,
               String gender, String birthtime, String providerid, String xmlCreationDate, String guardianNo,
               String payerID, String policyType, String purpose)
    {
        this.patientID = ID;
        this.patientRole = patientrole;
        this.givenName = givenName;
        this.familyName = familyname;
        this.suffix = suffix;
        this.gender = gender;
        this.birthtime = birthtime;
        this.providerId = providerid;
        this.xmlCreationDate = xmlCreationDate;
        this.guardianNo = guardianNo;
        this.payerID = payerID;
        this.policyType = policyType;
        this.purpose = purpose;
    }

    public String getPatientID() {
        return patientID;
    }

    public String getPatientRole() {
        return patientRole;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthtime() {
        return birthtime;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getXmlCreationDate() {
        return xmlCreationDate;
    }

    public String getGuardianNo() {
        return guardianNo;
    }

    public String getPayerID() {
        return payerID;
    }

    public String getPolicyType() {
        return policyType;
    }

    public String getPurpose() {
        return purpose;
    }


    public void setPatientRole(String patientRole) {
        this.patientRole = patientRole;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirthtime(String birthtime) {
        this.birthtime = birthtime;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public void setXmlCreationDate(String xmlCreationDate) {
        this.xmlCreationDate = xmlCreationDate;
    }

    public void setGuardianNo(String guardianNo) {
        this.guardianNo = guardianNo;
    }

    public void setPayerID(String payerID) {
        this.payerID = payerID;
    }

    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    public void viewPatientRecord(Connection connectHISDB) throws SQLException
    {
        // System.out.println("in 1 menu");
        Statement statement = connectHISDB.createStatement();
        String patientID = this.getPatientID();
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
    public void viewPatientAuthors(Connection connectHISDB) throws SQLException
    {
        Statement statement = connectHISDB.createStatement();
        System.out.println("*********** ASSIGNED AUTHORS TO PATIENT ************");
        String patientID = this.getPatientID();
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

    public void viewPatientLabTestReport(Connection connectHISDB) throws SQLException
    {
        System.out.println("************** LAB TEST INFO ***************");
        Statement statement = connectHISDB.createStatement();
        String patientID = this.getPatientID();
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
    public void viewPatientAllergies(Connection connectHISDB) throws SQLException
    {
        System.out.println("*************** ALLERGY INFO ************");
        Statement statement = connectHISDB.createStatement();
        String patientID = this.getPatientID();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM PatientAllergy WHERE patientID='" + patientID + "'");
        while(resultSet.next())
        {
            System.out.println("AllergyID " + resultSet.getString("allergyID"));
            System.out.println("Substance: " + resultSet.getString("substance"));
            System.out.println("Reaction: " + resultSet.getString("reaction"));
            System.out.println("Status: " + resultSet.getString("status"));
        }
    }
    public void viewPatientRelatives(Connection connectHISDB) throws SQLException
    {
        System.out.println("***************RELATIVE INFO**************");
        Statement statement = connectHISDB.createStatement();
        String patientID = this.getPatientID();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM PatientRelative WHERE patientID='" + patientID + "'");
        while(resultSet.next())
        {
            System.out.println("Relation: " + resultSet.getString("relationship"));
            System.out.println("Diagnosis: " + resultSet.getString("diagnosis"));
            System.out.println("Age: " + resultSet.getString("age"));
        }
    }
    public void viewPatientInsuranceCompany(Connection connectHISDB) throws SQLException
    {
        System.out.println("********** INSURANCE COMPANY INFORMATION ************");
        Statement statement = connectHISDB.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM InsuranceCompany WHERE payerID='" + this.getPayerID() + "'");
        while(resultSet.next())
        {
            System.out.println("Patient's insurance company is " + resultSet.getString("name"));
        }
        System.out.println("******** FINISH INSURANCE COMPANY INFORMATION************");
    }
    public void viewPatientPlanInfo(Connection connectHISDB) throws SQLException
    {
        System.out.println("*************** PLAN INFO ************");
        String patientID = this.getPatientID();
        Statement statement = connectHISDB.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM PatientPlan WHERE patientID='" + patientID + "'");
        while(resultSet.next())
        {
            System.out.println("PlanID: " + resultSet.getString("planID") + "\n\tActivity: " + resultSet.getString("activity") + " performed on " + resultSet.getDate("date"));
        }
    }
    public void viewPatientGuardian(Connection connectHISDB) throws SQLException
    {
        System.out.println("********** GUARDIAN INFO **********");
        //resultSet = statement.executeQuery("SELECT * FROM Patient WHERE patientID='" + patientID + "'");
        String guardianNo = this.getGuardianNo();
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
    public void editPatientSuffix(Connection connectHISDB) throws SQLException
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
                + this.getPatientID() + "'";
        int numRowsUpdated = statement.executeUpdate(updateSuffixQuery);
        if(numRowsUpdated > 0)
        {
            System.out.println("Update successful.");
            this.setSuffix(suffix);

        }
        else
        {
            System.out.println("Update failed.");
        }
    }
    public void editPatientGender(Connection connectHISDB) throws SQLException
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
                + this.getPatientID() + "'";
        int numRowsUpdated = statement.executeUpdate(updateGenderQuery);
        if(numRowsUpdated > 0)
        {
            System.out.println("Update successful.");
            this.setGender(editGenderStr);
        }
        else
        {
            System.out.println("Update failed.");
        }
    }
    public void editPatientFamilyName(Connection connectHISDB) throws  SQLException
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
        String updateLastNameQuery = "UPDATE Patient SET familyName='" + familyName + "' WHERE patientID='" + this.getPatientID() + "'";
        int numRowsUpdated = statement.executeUpdate(updateLastNameQuery);
        if(numRowsUpdated > 0)
        {
            System.out.println("Update successful.");
            this.setFamilyName(familyName);
        }
        else
        {
            System.out.println("Update failed.");
        }
    }
    public void editPatientGivenName(Connection connectHISDB) throws  SQLException
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
        String firstNameUpdate = "UPDATE Patient SET givenName='" + givenName + "' WHERE patientID='" + this.getPatientID() + "'";
        int numRowsUpdated = statement.executeUpdate(firstNameUpdate);
        if(numRowsUpdated > 0)
        {
            System.out.println("Update successsful.");
            this.setGivenName(givenName);
        }
        else
        {
            System.out.println("Update failed.");
        }
    }
    //DONE
    public void editPatientBirthtime(Connection connectHISDB) throws SQLException
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
        String updateStr = "UPDATE Patient SET birthTime='" + combinedDateTime + "' WHERE patientID='" + this.getPatientID() + "'";
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
    public void editPatientInfo(Connection connectHISDB) throws SQLException
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
            this.editPatientSuffix(connectHISDB);
        }
        // edit gender
        else if(editPatientStr.equals("2"))
        {
            this.editPatientGender(connectHISDB);
        }
        // edit family name
        else if(editPatientStr.equals("3"))
        {
            this.editPatientFamilyName(connectHISDB);
        }
        // edit given name
        else if(editPatientStr.equals("4"))
        {
            this.editPatientGivenName(connectHISDB);

        }
        // edit birth time
        else if(editPatientStr.equals("5"))
        {
            this.editPatientBirthtime(connectHISDB);
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
    public void editGuardianInfo(Connection connectHISDB) throws SQLException
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
            this.editGuardianGivenName(connectHISDB);
        }
        // last name
        else if(editGuardianStr.equals("2"))
        {
            this.editGuardianFamilyName(connectHISDB);

        }
        // phone number
        else if(editGuardianStr.equals("3"))
        {
            this.editGuardianPhone(connectHISDB);
        }
        // address
        else if(editGuardianStr.equals("4"))
        {
            this.editGuardianAddress(connectHISDB);
        }
        else if(editGuardianStr.equals("-1"))
        {
            System.out.println("Exiting and returning to Patient access menu.");
        }
    }
    public void editGuardianGivenName(Connection connectHISDB) throws SQLException
    {
        System.out.println("Enter given name: ");
        Scanner editGuardianScanner = new Scanner(System.in);
        Statement statement = connectHISDB.createStatement();
        String givenName = editGuardianScanner.next();
        if(givenName.equals("-1"))
        {
            return;
        }
        String updateGuardianStr = "UPDATE Guardian SET givenName='" + givenName + "' WHERE guardianNo='" + this.getGuardianNo() + "'";
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
    public void editGuardianFamilyName(Connection connectHISDB) throws SQLException
    {
        System.out.println("Enter family name: ");
        Scanner editGuardianScanner = new Scanner(System.in);
        Statement statement = connectHISDB.createStatement();
        String familyName = editGuardianScanner.next();
        if(familyName.equals("-1"))
        {
            return;
        }
        String updateGuardianStr = "UPDATE Guardian SET familyName='" + familyName + "' WHERE guardianNo='" + this.getGuardianNo() + "'";
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
    public void editGuardianPhone(Connection connectHISDB) throws SQLException
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
        String updateGuardianStr = "UPDATE Guardian SET phone='" + phone + "' WHERE guardianNo='" + this.getGuardianNo() + "'";
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
    public void editGuardianAddress(Connection connectHISDB) throws SQLException
    {
        Statement updateAddress = connectHISDB.createStatement();
        Statement updateCity = connectHISDB.createStatement();
        Statement updateState = connectHISDB.createStatement();
        Statement updateZip = connectHISDB.createStatement();

        Scanner addressScanner = new Scanner(System.in);
        String guardianNo = this.getGuardianNo();
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
    public void insertPatient(Connection connectHISDB) throws SQLException
    {
        PreparedStatement patientStmt = connectHISDB.prepareStatement(
                "INSERT INTO Patient " +
                        "(patientID, suffix, familyName, givenName, gender, birthTime, providerID, xmlHealthCreationDate, guardianNo, payerID, patientRole, policyType, purpose) " +
                        " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE patientID=patientID, xmlHealthCreationDate=xmlHealthCreationDate,"+
                        "suffix=suffix, familyName=familyName, givenName=givenName, gender=gender, birthTime=birthTime, providerID=providerID, guardianNo=guardianNo," +
                        "payerID=payerID, patientRole=patientRole, policyType=policyType, purpose=purpose");
        //System.out.println("patientID = " + p.getPatientID());
        patientStmt.setString(1, this.getPatientID());
        patientStmt.setString(2, this.getSuffix());
        patientStmt.setString(3, this.getFamilyName());
        patientStmt.setString(4, this.getGivenName());
        patientStmt.setString(5, this.getGender());
        patientStmt.setString(6, this.getBirthtime());
        patientStmt.setString(7, this.getProviderId());
        patientStmt.setString(8, this.getXmlCreationDate());
        patientStmt.setString(9, this.getGuardianNo());
        patientStmt.setString(10, this.getPayerID());
        patientStmt.setString(11, this.getPatientRole());
        patientStmt.setString(12, this.getPolicyType());
        patientStmt.setString(13, this.getPurpose());

        patientStmt.executeUpdate();

    }

}
