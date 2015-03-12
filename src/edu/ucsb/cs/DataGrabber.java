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
                        ic.insertInsuranceCompany(connectHISDB);
                    }
                    /********* GUARDIAN ***********/
                    if(guardianNoStr != null)
                    {
                        g.insertGuardian(connectHISDB);
                    }
                    /********* AUTHOR **********/
                    if(authorIdStr != null) {
                        a.insertAuthor(connectHISDB);
                    }
                    /************* PATIENT ******************/
                    if(patientIdStr != null && guardianNoStr != null && payerIdStr != null)
                    {
                        p.insertPatient(connectHISDB);
                    }
                    /************** ASSIGNED **************/
                    if(authorIdStr != null && patientIdStr != null) {
                        as.insertAssigned(connectHISDB);
                    }
                    /********* LABTESTREPORT ***********/
                    if(labTestResultIdStr != null && patientVisitIdStr != null && patientIdStr != null) {
                        ltr.insertLabTestReport(connectHISDB);
                    }
                    /*********** PatientRelative ***********/
                    if(relativeIdStr != null && patientIdStr != null && diagnosis != null)
                    {
                        pr.insertPatientRelative(connectHISDB);
                    }
                    /******** PatientAllergy ************/
                    if(idStr != null && patientIdStr != null) {
                        //System.out.println("second case idStr access");
                        pa.insertPatientAllergy(connectHISDB);
                    }
                    /***** PatientPlan *********/
                    if(planIdStr != null && patientIdStr != null)
                    {
                        pp.insertPatientPlan(connectHISDB);
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
                            ic.insertInsuranceCompany(connectHISDB);
                        }
                        /********* GUARDIAN ***********/
                        if(guardianNoStr != null)
                        {
                            g.insertGuardian(connectHISDB);
                        }
                        /********* AUTHOR **********/
                        if(authorIdStr != null) {
                            a.insertAuthor(connectHISDB);
                        }
                        /************* PATIENT ******************/
                        if(patientIdStr != null && guardianNoStr != null && payerIdStr != null)
                        {
                            p.insertPatient(connectHISDB);
                        }
                        /************** ASSIGNED **************/
                        if(authorIdStr != null && patientIdStr != null) {
                            as.insertAssigned(connectHISDB);
                        }
                        /********* LABTESTREPORT ***********/
                        if(labTestResultIdStr != null && patientVisitIdStr != null && patientIdStr != null) {
                            ltr.insertLabTestReport(connectHISDB);
                        }
                        /*********** PatientRelative ***********/
                        if(relativeIdStr != null && patientIdStr != null && diagnosis != null)
                        {
                            pr.insertPatientRelative(connectHISDB);
                        }
                        /******** PatientAllergy ************/
                        if(idStr != null && patientIdStr != null) {
                            //System.out.println("second case idStr access");
                            pa.insertPatientAllergy(connectHISDB);
                        }
                        /***** PatientPlan *********/
                        if(planIdStr != null && patientIdStr != null)
                        {
                            pp.insertPatientPlan(connectHISDB);
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


}
