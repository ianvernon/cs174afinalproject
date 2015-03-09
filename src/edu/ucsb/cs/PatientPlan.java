package edu.ucsb.cs;

import java.util.Date;

/**
 * Created by ianvernon on 3/3/15.
 */
import java.sql.Timestamp;

public class PatientPlan
{
    private String planID;
    private Timestamp date;
    private String activity;
    private String patientID;


    public PatientPlan(String planID, Timestamp date, String activity, String patientID)
    {
        this.planID = planID;
        this.date = date;
        this.activity = activity;
        this.patientID = patientID;
    }

    public String getPlanID() {
        return planID;
    }

    public Timestamp getDate() {
        return date;
    }

    public String getActivity() {
        return activity;
    }

    public String getPatientID() {
        return patientID;
    }
}

