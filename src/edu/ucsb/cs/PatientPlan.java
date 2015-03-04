package edu.ucsb.cs;

import java.util.Date;

/**
 * Created by ianvernon on 3/3/15.
 */
public class PatientPlan
{
    private int planID;
    private Date date;
    private String activity;
    private int patientID;


    public PatientPlan(int planID, Date date, String activity, int patientID)
    {
        this.planID = planID;
        this.date = date;
        this.activity = activity;
        this.patientID = patientID;
    }
}
