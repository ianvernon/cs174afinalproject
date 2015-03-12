package edu.ucsb.cs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by ianvernon on 3/3/15.
 */
import java.sql.Timestamp;

public class PatientPlan
{
    private String planID;
    private java.sql.Date date;
    private String activity;
    private String patientID;


    public PatientPlan(String planID, java.sql.Date date, String activity, String patientID)
    {

        this.planID = planID;
        this.date = date;
        this.activity = activity;
        this.patientID = patientID;
    }

    public String getPlanID() {
        return planID;
    }

    public java.sql.Date getDate() {
        return date;
    }

    public String getActivity() {
        return activity;
    }

    public String getPatientID() {
        return patientID;
    }
    public void insertPatientPlan(Connection connectHISDB)  throws SQLException
    {
        PreparedStatement planStatement = connectHISDB.prepareStatement(
                "INSERT INTO PatientPlan " +
                        "(planID, date, activity, patientID)  "  +
                        "VALUES(?, ?, ?, ?) ON DUPLICATE KEY UPDATE planID=planID, date=date, activity=activity, patientID=patientID");
        planStatement.setString(1, this.getPlanID());
        planStatement.setDate(2, this.getDate());
        //System.out.println("pp.getDate() = " + pp.getDate());
        planStatement.setString(3, this.getActivity());
        planStatement.setString(4, this.getPatientID());

        planStatement.executeUpdate();
    }
}

