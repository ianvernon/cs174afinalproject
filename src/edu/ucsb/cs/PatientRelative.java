package edu.ucsb.cs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
    public void insertPatientRelative(Connection connectHISDB) throws SQLException
    {
        PreparedStatement pRStatement = connectHISDB.prepareStatement(
                "INSERT INTO PatientRelative " +
                        "(relativeID, age, diagnosis, patientID, relationship) " +
                        "VALUES(?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE relativeID=relativeID , patientID=patientID , diagnosis=diagnosis, age=age, relationship=relationship");

        pRStatement.setString(1, this.getRelativeID());
        pRStatement.setString(2, this.getAge());
        pRStatement.setString(3, this.getDiagnosis());
        pRStatement.setString(4, this.getPatientID());
        pRStatement.setString(5, this.getRelationship());

        pRStatement.executeUpdate();

    }
}
