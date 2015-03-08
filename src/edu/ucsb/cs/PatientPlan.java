package edu.ucsb.cs;

import java.util.Date;

/**
 * Created by ianvernon on 3/3/15.
 */
public class PatientPlan
{
    private String planID;
    private String date;
    private String activity;
    private String patientID;


    public PatientPlan(String planID, String date, String activity, String patientID)
    {
        this.planID = planID;
        this.date = date;
        this.activity = activity;
        this.patientID = patientID;
    }

    public String getPlanID() {
        return planID;
    }

    public String getDate() {
        return date;
    }

    public String getActivity() {
        return activity;
    }

    public String getPatientID() {
        return patientID;
    }
}

