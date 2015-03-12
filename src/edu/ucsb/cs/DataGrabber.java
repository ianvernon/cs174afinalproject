package edu.ucsb.cs;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by ianvernon on 3/12/15.
 */
public class DataGrabber
{
    public DataGrabber(String sourceDb, String sourceTable, String destDb)
    {
        this.grabData(sourceDb, sourceTable, destDb);
    }
    public static String getCurrentDate()
    {
        /*Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH)+1;
        int currentDay = c.get(Calendar.DAY_OF_MONTH);*/
        DateFormat timeFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        String s = timeFormat.format(new java.util.Date());
        return s;
    }
    public static void grabData(String sourceDb, String sourceTable, String destDb)
    {
        Connection sourceConnect = null;
        Statement statement = null;
        ResultSet resultSet = null;
        Connection connectHISDB = null;

        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            sourceConnect = DriverManager.getConnection(
                    "jdbc:mysql://localhost/" + sourceDb + "?" +
                            "user=root&password="
            );

            statement = sourceConnect.createStatement();

            resultSet = statement.executeQuery("select * from " + sourceDb + "." + sourceTable);

            connectHISDB = DriverManager.getConnection("jdbc:mysql://localhost/" + destDb + "?"
                    + "user=root&password=");

            while(resultSet.next())
            {
                String currentDateAndTime = getCurrentDate();
                String last_accessed = resultSet.getString("Last_Accessed");
                if(last_accessed == null)
                {
                    //System.out.println("null last_accessed");
                    last_accessed = currentDateAndTime;
                }
                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
                //java.util.Date currDate = df.parse(currentDateAndTime);
                java.util.Date laDate = df.parse(last_accessed);


                //check if patientID is in Patient table in HIS
                String patientIdStr = resultSet.getString("patientId");
                //grab all data from table
                //patient
                String givenName = resultSet.getString("GivenName");
                String familyName = resultSet.getString("FamilyName");
                String birthTime = resultSet.getString("BirthTime");
                String providerIdStr = resultSet.getString("providerId");
                //guardian
                String guardianNoStr = resultSet.getString("GuardianNo");
                String relationship = resultSet.getString("Relationship");
                String firstName = resultSet.getString("FirstName");
                String lastName = resultSet.getString("LastName");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");
                String city = resultSet.getString("city");
                String state = resultSet.getString("state");
                String zip = resultSet.getString("zip");
                //author
                String authorIdStr = resultSet.getString("AuthorId");
                String authorTitle = resultSet.getString("AuthorTitle");
                String authorFirstName = resultSet.getString("AuthorFirstName");
                String authorLastName = resultSet.getString("AuthorLastName");
                //asigned
                String participatingRole = resultSet.getString("ParticipatingRole");
                //insurance company
                String payerIdStr = resultSet.getString("PayerId");
                String name = resultSet.getString("Name");
                String policyHolder = resultSet.getString("PolicyHolder");
                String policyType = resultSet.getString("PolicyType");
                String purpose = resultSet.getString("Purpose");
                //relative
                String relativeIdStr = resultSet.getString("RelativeId");
                String relation = resultSet.getString("Relation");
                String ageStr = resultSet.getString("age");
                String diagnosis = resultSet.getString("Diagnosis");
                //allergy
                String idStr = resultSet.getString("Id");
                //System.out.println("idStr = " + idStr);
                String substance = resultSet.getString("Substance");
                String reaction = resultSet.getString("Reaction");
                String status = resultSet.getString("Status");
                //lab test report
                String labTestResultIdStr = resultSet.getString("LabTestResultId");
                String patientVisitIdStr = resultSet.getString("PatientVisitId");
                String labTestPerformDate = resultSet.getString("LabTestPerformedDate");
                String labTestType = resultSet.getString("LabTestType");
                String testResultValue = resultSet.getString("TestResultValue");
                String referenceRangeHigh = resultSet.getString("ReferenceRangeHigh");
                String referenceRangeLow = resultSet.getString("ReferenceRangeLow");
                //plan
                String planIdStr = resultSet.getString("PlanId");
                String activity = resultSet.getString("Activity");
                String scheduledDate = resultSet.getString("ScheduledDate");

                String suffix = "";
                String gender = "";
                String patientRole = "";
                //System.out.println("patientId = " + patientIdStr);
                Statement xmlhcdStmt = connectHISDB.createStatement();
                // see if patient is in Patient table
                String hisQuery = "SELECT * FROM Patient WHERE patientID='" + patientIdStr + "'";
                ResultSet patientQuery = xmlhcdStmt.executeQuery(hisQuery);
                //if not, insert
                if(!patientQuery.isBeforeFirst())
                {
                    //create objects out of data that correspond to tables in HIS
                    InsuranceCompany ic = new InsuranceCompany(payerIdStr, name);

                    Guardian g = new Guardian(guardianNoStr, firstName, lastName, phone, address, city, state, zip);
                    Author a = new Author(authorIdStr, authorTitle, authorFirstName, authorLastName);
                    Patient p = new Patient(patientIdStr, patientRole, givenName, familyName, suffix, gender, birthTime, providerIdStr, currentDateAndTime,
                            guardianNoStr, payerIdStr, policyType, purpose);
                    Assigned as = new Assigned(authorIdStr, patientIdStr, participatingRole);

                    //format lab test performed date into timestamp for insertion into object
                    java.sql.Date ltrPd = null;
                    if(labTestPerformDate != null) {
                        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
                        java.util.Date d = format.parse(labTestPerformDate);
                        ltrPd = new java.sql.Date(d.getTime());
                    }
                    LabTestReport ltr = new LabTestReport(labTestResultIdStr, patientVisitIdStr, ltrPd, labTestType, referenceRangeLow,
                            referenceRangeHigh, testResultValue, patientIdStr);
                    PatientRelative pr = new PatientRelative(relativeIdStr, ageStr, diagnosis, patientIdStr, relation);
                    PatientAllergy pa = new PatientAllergy(idStr, substance, reaction, status, patientIdStr);


                    java.sql.Date planDate = null;
                    if(scheduledDate != null) {
                        // System.out.println("scheduled date is " + scheduledDate);
                        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");

                        java.util.Date d = format.parse(scheduledDate);
                        planDate = new java.sql.Date(d.getTime());
                        //System.out.println("planDate.toString() " + planDate.toString());
                    }

                    PatientPlan pp = new PatientPlan(planIdStr, planDate, activity, patientIdStr);

                    //perform insertion of objects on tables

                    /********* INSURANCE COMPANY ************/
                    if(payerIdStr != null) {
                        insertInsuranceCompany(connectHISDB, ic);
                    }
                    /********* GUARDIAN ***********/
                    if(guardianNoStr != null)
                    {
                        insertGuardian(connectHISDB, g);
                    }
                    /********* AUTHOR **********/
                    if(authorIdStr != null) {
                        insertAuthor(connectHISDB, a);
                    }
                    /************* PATIENT ******************/
                    if(patientIdStr != null && guardianNoStr != null && payerIdStr != null)
                    {
                        insertPatient(connectHISDB, p);
                    }
                    /************** ASSIGNED **************/
                    if(authorIdStr != null && patientIdStr != null) {
                        insertAssigned(connectHISDB, as);
                    }
                    /********* LABTESTREPORT ***********/
                    if(labTestResultIdStr != null && patientVisitIdStr != null && patientIdStr != null) {
                        insertLabTestReport(connectHISDB, ltr);
                    }
                    /*********** PatientRelative ***********/
                    if(relativeIdStr != null && patientIdStr != null && diagnosis != null)
                    {
                        insertPatientRelative(connectHISDB, pr);
                    }
                    /******** PatientAllergy ************/
                    if(idStr != null && patientIdStr != null) {
                        //System.out.println("second case idStr access");
                        insertPatientAllergy(connectHISDB, pa);
                    }
                    /***** PatientPlan *********/
                    if(planIdStr != null && patientIdStr != null)
                    {
                        insertPatientPlan(connectHISDB, pp);
                    }
                    /********* UPDATE LAST ACCESSED FOR CURRENT ROW IN RESULTSET *********/
                    PreparedStatement updateLastAccessed = sourceConnect.prepareStatement("UPDATE messages SET Last_Accessed='" + currentDateAndTime
                            + "' WHERE patientId='" + patientIdStr+ "'");

                    updateLastAccessed.executeUpdate();
                }
                //if in patient table, compare xmlCreationDate in Patient and Last_Accessed in healthmessagesexchange2
                else
                {
                    //System.out.println("else!");
                    patientQuery.next();
                    String currPatientXmlDate = patientQuery.getString("xmlHealthCreationDate");
                    java.util.Date currPatientXmlDateObj = df.parse(currPatientXmlDate);
                    //System.out.println("laDate = " + laDate.toString());
                    // System.out.println("currPatientXMLDateObj = " + currPatientXmlDateObj.toString());
                    // if xmlHealthCreationDate < Last_Accessed, then update info in Patient table
                    if(currPatientXmlDateObj.compareTo(laDate) < 0)
                    {
                        //System.out.println("here");
                        //create objects out of data that correspond to tables in HIS
                        InsuranceCompany ic = new InsuranceCompany(payerIdStr, name);

                        Guardian g = new Guardian(guardianNoStr, firstName, lastName, phone, address, city, state, zip);
                        Author a = new Author(authorIdStr, authorTitle, authorFirstName, authorLastName);
                        Patient p = new Patient(patientIdStr, patientRole, givenName, familyName, suffix, gender, birthTime, providerIdStr, currentDateAndTime,
                                guardianNoStr, payerIdStr, policyType, purpose);
                        Assigned as = new Assigned(authorIdStr, patientIdStr, participatingRole);

                        //format lab test performed date into timestamp for insertion into object
                        java.sql.Date ltrPd = null;
                        if(labTestPerformDate != null) {
                            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                            java.util.Date d = format.parse(labTestPerformDate);
                            ltrPd = new Date(d.getTime());
                        }
                        LabTestReport ltr = new LabTestReport(labTestResultIdStr, patientVisitIdStr, ltrPd, labTestType, referenceRangeLow,
                                referenceRangeHigh, testResultValue, patientIdStr);
                        PatientRelative pr = new PatientRelative(relativeIdStr, ageStr, diagnosis, patientIdStr, relation);
                        PatientAllergy pa = new PatientAllergy(idStr, substance, reaction, status, patientIdStr);


                        java.sql.Date planDate = null;
                        if(scheduledDate != null) {
                            //System.out.println("scheduled date is " + scheduledDate);
                            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

                            java.util.Date d = format.parse(scheduledDate);
                            planDate = new java.sql.Date(d.getTime());
                        }
                        // System.out.println("planIdStr = " + planIdStr);
                        //System.out.println("activity = " + activity);
                        //System.out.println("patientIdStr = " + patientIdStr);
                        PatientPlan pp = new PatientPlan(planIdStr, planDate, activity, patientIdStr);

                        //perform insertion of objects on tables

                        /********* INSURANCE COMPANY ************/
                        if(payerIdStr != null) {
                            insertInsuranceCompany(connectHISDB, ic);
                        }
                        /********* GUARDIAN ***********/
                        if(guardianNoStr != null)
                        {
                            insertGuardian(connectHISDB, g);
                        }
                        /********* AUTHOR **********/
                        if(authorIdStr != null) {
                            insertAuthor(connectHISDB, a);
                        }
                        /************* PATIENT ******************/
                        if(patientIdStr != null && guardianNoStr != null && payerIdStr != null)
                        {
                            insertPatient(connectHISDB, p);
                        }
                        /************** ASSIGNED **************/
                        if(authorIdStr != null && patientIdStr != null) {
                            insertAssigned(connectHISDB, as);
                        }
                        /********* LABTESTREPORT ***********/
                        if(labTestResultIdStr != null && patientVisitIdStr != null && patientIdStr != null) {
                            insertLabTestReport(connectHISDB, ltr);
                        }
                        /*********** PatientRelative ***********/
                        if(relativeIdStr != null && patientIdStr != null && diagnosis != null)
                        {
                            insertPatientRelative(connectHISDB, pr);
                        }
                        /******** PatientAllergy ************/
                        if(idStr != null && patientIdStr != null) {
                            //System.out.println("second case idStr access");
                            insertPatientAllergy(connectHISDB, pa);
                        }
                        /***** PatientPlan *********/
                        if(planIdStr != null && patientIdStr != null)
                        {
                            insertPatientPlan(connectHISDB, pp);
                        }
                        /********* UPDATE LAST ACCESSED FOR CURRENT ROW IN RESULTSET *********/
                        PreparedStatement updateLastAccessed = sourceConnect.prepareStatement("UPDATE messages SET Last_Accessed='" + currentDateAndTime
                                + "' WHERE patientId='" + patientIdStr+ "'");

                        updateLastAccessed.executeUpdate();
                    }

                }
                //System.out.println("*************");
            }

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public static void insertInsuranceCompany(Connection connectHISDB, InsuranceCompany ic) throws SQLException
    {

        PreparedStatement icStmt = connectHISDB.prepareStatement(
                "INSERT INTO InsuranceCompany " +
                        "(payerID, name) VALUES(?, ?) ON DUPLICATE KEY UPDATE payerID=payerID, name=name");

        icStmt.setString(1, ic.getPayerID());
        icStmt.setString(2, ic.getName());

        icStmt.executeUpdate();
    }
    public static void insertGuardian(Connection connectHISDB, Guardian g) throws SQLException
    {
        PreparedStatement guardianStmt = connectHISDB.prepareStatement(
                "INSERT INTO Guardian " +
                        "(guardianNo, givenName, familyName, phone, address, city, state, zip) " +
                        "VALUES(?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE guardianNo=guardianNo, givenName=givenName, familyName=familyName,"+
                        " phone=phone, address=address, city=city, state=state, zip=zip");
        guardianStmt.setString(1, g.getGuardianNo());
        guardianStmt.setString(2, g.getGivenName());
        guardianStmt.setString(3, g.getFamilyName());
        guardianStmt.setString(4, g.getPhone());
        guardianStmt.setString(5, g.getAddress());
        guardianStmt.setString(6, g.getCity());
        guardianStmt.setString(7, g.getState());
        guardianStmt.setString(8, g.getZip());
        guardianStmt.executeUpdate();
    }
    public static void insertAuthor(Connection connectHISDB, Author a) throws  SQLException
    {
        PreparedStatement authorStmt = connectHISDB.prepareStatement(
                "INSERT INTO Author " +
                        "(authorID, authorTitle, authorFirstName, authorLastName) "
                        + "VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE authorID=authorID, authorFirstName=authorFirstName, authorLastName=authorLastName, authorTitle=authorTitle");

        authorStmt.setString(1, a.getAuthorID());
        authorStmt.setString(2, a.getAuthorTitle());
        authorStmt.setString(3, a.getAuthorFirstName());
        authorStmt.setString(4, a.getAuthorLastName());

        authorStmt.executeUpdate();
    }
    public static void insertPatient(Connection connectHISDB, Patient p) throws SQLException
    {
        PreparedStatement patientStmt = connectHISDB.prepareStatement(
                "INSERT INTO Patient " +
                        "(patientID, suffix, familyName, givenName, gender, birthTime, providerID, xmlHealthCreationDate, guardianNo, payerID, patientRole, policyType, purpose) " +
                        " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE patientID=patientID, xmlHealthCreationDate=xmlHealthCreationDate,"+
                        "suffix=suffix, familyName=familyName, givenName=givenName, gender=gender, birthTime=birthTime, providerID=providerID, guardianNo=guardianNo," +
                        "payerID=payerID, patientRole=patientRole, policyType=policyType, purpose=purpose");
        //System.out.println("patientID = " + p.getPatientID());
        patientStmt.setString(1, p.getPatientID());
        patientStmt.setString(2, p.getSuffix());
        patientStmt.setString(3, p.getFamilyName());
        patientStmt.setString(4, p.getGivenName());
        patientStmt.setString(5, p.getGender());
        patientStmt.setString(6, p.getBirthtime());
        patientStmt.setString(7, p.getProviderId());
        patientStmt.setString(8, p.getXmlCreationDate());
        patientStmt.setString(9, p.getGuardianNo());
        patientStmt.setString(10, p.getPayerID());
        patientStmt.setString(11, p.getPatientRole());
        patientStmt.setString(12, p.getPolicyType());
        patientStmt.setString(13, p.getPurpose());

        patientStmt.executeUpdate();

    }
    public static void insertAssigned(Connection connectHISDB, Assigned as) throws SQLException
    {
        PreparedStatement assignedStmt = connectHISDB.prepareStatement(
                "INSERT INTO Assigned " +
                        "(authorID, patientID, participatingRole)" +
                        " VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE authorID=authorID , patientID=patientID, participatingRole=participatingRole");

        assignedStmt.setString(1, as.getAuthorID());
        assignedStmt.setString(2, as.getPatientID());
        assignedStmt.setString(3, as.getParticipatingRole());

        assignedStmt.executeUpdate();
    }
    public static void insertLabTestReport(Connection connectHISDB, LabTestReport ltr) throws SQLException
    {
        PreparedStatement ltrStmt = connectHISDB.prepareStatement(
                "INSERT INTO LabTestReport " +
                        "(LabTestResultID, PatientVisitID, LabTestPerformedDate, LabTestType, ReferenceRangeLow, ReferenceRangeHigh, TestResultValue, patientID) "
                        + "VALUES(?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE LabTestResultID=LabTestResultID, PatientVisitID=PatientVisitID, LabTestType=LabTestType, ReferenceRangeLow=ReferenceRangeLow, ReferenceRangeHigh=ReferenceRangeHigh, TestResultValue=TestResultValue, patientID=patientID");



        ltrStmt.setString(1, ltr.getLabTestResultID());
        ltrStmt.setString(2, ltr.getPatientVisitID());
        ltrStmt.setDate(3, ltr.getLabTestPerformedDate());
        ltrStmt.setString(4, ltr.getLabTestType());
        ltrStmt.setString(5, ltr.getReferenceRangeLow());
        ltrStmt.setString(6, ltr.getReferenceRangeHigh());
        ltrStmt.setString(7, ltr.getTestResultValue());
        ltrStmt.setString(8, ltr.getPatientID());

        ltrStmt.executeUpdate();

    }
    public static void insertPatientRelative(Connection connectHISDB, PatientRelative pr) throws SQLException
    {
        PreparedStatement pRStatement = connectHISDB.prepareStatement(
                "INSERT INTO PatientRelative " +
                        "(relativeID, age, diagnosis, patientID, relationship) " +
                        "VALUES(?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE relativeID=relativeID , patientID=patientID , diagnosis=diagnosis, age=age, relationship=relationship");

        pRStatement.setString(1, pr.getRelativeID());
        pRStatement.setString(2, pr.getAge());
        pRStatement.setString(3, pr.getDiagnosis());
        pRStatement.setString(4, pr.getPatientID());
        pRStatement.setString(5, pr.getRelationship());

        pRStatement.executeUpdate();

    }
    public static void insertPatientAllergy(Connection connectHISDB, PatientAllergy pa) throws SQLException
    {
        PreparedStatement pAStatement = connectHISDB.prepareStatement(
                "INSERT INTO PatientAllergy " +
                        "(allergyID, substance, reaction, status, patientID) " +
                        "VALUES(?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE allergyID=allergyID, substance=substance, reaction=reaction, status=status, patientID=patientID");
        //System.out.println("pa.getAllergyID() = " + pa.getAllergyID());
        pAStatement.setString(1, pa.getAllergyID());
        pAStatement.setString(2, pa.getSubstance());
        pAStatement.setString(3, pa.getReaction());
        pAStatement.setString(4, pa.getStatus());
        pAStatement.setString(5, pa.getPatientID());

        pAStatement.executeUpdate();
    }
    public static void insertPatientPlan(Connection connectHISDB, PatientPlan pp)  throws SQLException
    {
        PreparedStatement planStatement = connectHISDB.prepareStatement(
                "INSERT INTO PatientPlan " +
                        "(planID, date, activity, patientID)  "  +
                        "VALUES(?, ?, ?, ?) ON DUPLICATE KEY UPDATE planID=planID, date=date, activity=activity, patientID=patientID");
        planStatement.setString(1, pp.getPlanID());
        planStatement.setDate(2, pp.getDate());
        //System.out.println("pp.getDate() = " + pp.getDate());
        planStatement.setString(3, pp.getActivity());
        planStatement.setString(4, pp.getPatientID());

        planStatement.executeUpdate();
    }
}
