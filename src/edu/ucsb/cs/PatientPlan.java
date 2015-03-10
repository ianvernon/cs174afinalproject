package edu.ucsb.cs;

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
}

