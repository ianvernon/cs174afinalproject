package edu.ucsb.cs;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.Calendar;


public class Main {

    public static void main(String[] args)
    {
	    grabData();

    }
    public static String getCurrentDate()
    {
        Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH);
        int currentDay = c.get(Calendar.DAY_OF_MONTH);

        String s = currentYear + "-"+ currentMonth + "-" + currentDay;
        return s;
    }
    public void patientRead()
    {

    }
    public void patientWrite()
    {

    }
    public void doctorRead()
    {

    }
    public void doctorWrite()
    {

    }
    public void adminRead()
    {

    }
    public void adminWrite()
    {

    }
    public static void grabData()
    {
        System.out.println("Hello world!!!!!");

        Connection sourceConnect = null;
        Statement statement = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connectHISDB = null;

        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            sourceConnect = DriverManager.getConnection(
                    "jdbc:mysql://localhost/healthmessagesexchange2?"+
                            "user=root&password="
            );

            statement = sourceConnect.createStatement();

            resultSet = statement.executeQuery("select * from healthmessagesexchange2.messages");
            //writeResultSet(resultSet);

            connectHISDB = DriverManager.getConnection("jdbc:mysql://localhost/HealthInformationSystem?"
                    + "user=root&password=");

            while(resultSet.next())
            {
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

                /********* INSURANCE COMPANY ************/
                PreparedStatement icStmt = connectHISDB.prepareStatement(
                        "INSERT INTO InsuranceCompany " +
                                "(payerID, name) VALUES(?, ?) ON DUPLICATE KEY UPDATE payerID=payerID");

                icStmt.setString(1, payerIdStr);
                icStmt.setString(2, name);

                icStmt.executeUpdate();

                /********* GUARDIAN ***********/

                PreparedStatement guardianStmt = connectHISDB.prepareStatement(
                        "INSERT INTO Guardian " +
                                "(guardianNo, givenName, familyName, phone, address, city, state, zip) " +
                                "VALUES(?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE guardianNo=guardianNo");
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
                                + "VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE authorID=authorID");

                authorStmt.setString(1, authorIdStr);
                authorStmt.setString(2, authorTitle);
                authorStmt.setString(3, authorFirstName);
                authorStmt.setString(4, authorLastName);

                authorStmt.executeUpdate();


                /************* PATIENT ******************/

                PreparedStatement patientStmt = connectHISDB.prepareStatement(
                        "INSERT INTO Patient " +
                                "(patientID, suffix, familyName, givenName, gender, birthTime, providerID, xmlHealthCreationDate, guardianNo, payerID, patientRole, policyType, purpose) " +
                                " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE patientID=patientID");

                patientStmt.setString(1, patientIdStr);
                patientStmt.setString(2, suffix);
                patientStmt.setString(3, familyName);
                patientStmt.setString(4, givenName);
                patientStmt.setString(5, gender);
                patientStmt.setString(6, birthTime);
                patientStmt.setString(7, providerIdStr);
                patientStmt.setDate(8, java.sql.Date.valueOf(getCurrentDate()));
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
                                " VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE authorID=authorID , patientID=patientID");

                assignedStmt.setString(1, authorIdStr);
                assignedStmt.setString(2, patientIdStr);
                assignedStmt.setString(3, participatingRole);

                assignedStmt.executeUpdate();

                /********* LABTESTREPORT ***********/

                PreparedStatement ltrStmt = connectHISDB.prepareStatement(
                        "INSERT INTO LabTestReport " +
                                "(LabTestResultID, PatientVisitID, LabTestPerformedDate, LabTestType, ReferenceRangeLow, ReferenceRangeHigh, TestResultValue, patientID) "
                                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE LabTestResultID=LabTestResultID, PatientVisitID=PatientVisitID");

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
                                "VALUES(?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE relativeID=relativeID , patientID=patientID , diagnosis=diagnosis");

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
                                "VALUES(?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE allergyID=allergyID");

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
                                "VALUES(?, ?, ?, ?) ON DUPLICATE KEY UPDATE planID=planID");

                if(planIdStr == null)
                {
                    planIdStr = "000";
                }
                planStatement.setString(1, planIdStr);
                planStatement.setString(2, scheduledDate);
                planStatement.setString(3, activity);
                planStatement.setString(4, patientIdStr);

                planStatement.executeUpdate();



            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
