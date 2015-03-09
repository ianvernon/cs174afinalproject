package edu.ucsb.cs;

/**
 * Created by ianvernon on 3/3/15.
 */
public class PatientRelative
{
    private String relativeID;
    private String age;
    private String diagnosis;
    private String patientID;
    private String relationship;

    public PatientRelative(String relativeID, String age, String diagnosis, String patientID, String relationship)
    {
        this.relativeID = relativeID;
        this.age = age;
        this.diagnosis = diagnosis;
        this.patientID = patientID;
        this.relationship = relationship;
    }

    public String getRelativeID() {
        return relativeID;
    }

    public String getAge() {
        return age;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getPatientID() {
        return patientID;
    }

    public String getRelationship() {
        return relationship;
    }
}
