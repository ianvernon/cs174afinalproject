package edu.ucsb.cs;

/**
 * Created by ianvernon on 3/3/15.
 */
public class PatientRelative
{
    private int relativeID;
    private int age;
    private String diagnosis;
    private int patientID;
    private String relationship;

    public PatientRelative(int relativeID, int age, String diagnosis, int patientID, String relationship)
    {
        this.relativeID = relativeID;
        this.age = age;
        this.diagnosis = diagnosis;
        this.patientID = patientID;
        this.relationship = relationship;
    }
}
