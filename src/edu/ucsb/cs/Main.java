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
        Scanner inFromConsole = new Scanner(System.in);
	    //grabData("healthmessagesexchange2", "messages", "HealthInformationSystem");
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

        }
        // doctor case;
        else if(privilegeLevel.equals("1"))
        {

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

    /**
     * grabs data from sourceTable located in sourceDb, and inserts it into destDb
     * @param sourceDb - name of source database which we will be grabbing data from
     * @param sourceTable - name of table within source database that we'll be grabbing data from
     * @param destDb - name of the database that we'll be inserting data into
     */
    public static void grabData(String sourceDb, String sourceTable, String destDb)
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
