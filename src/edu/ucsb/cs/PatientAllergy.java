package edu.ucsb.cs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by ianvernon on 3/3/15.
 */
public class PatientAllergy
{
        private String allergyID;
        private String substance;
        private String reaction;
        private String status;
        private String patientID;

        public PatientAllergy(String allergyID, String substance, String reaction, String status, String patientID)
        {
            this.allergyID = allergyID;
            this.substance = substance;
            this.reaction = reaction;
            this.status = status;
            this.patientID = patientID;
        }

    public String getSubstance() {
        return substance;
    }

    public String getReaction() {
        return reaction;
    }

    public String getStatus() {
        return status;
    }

    public String getPatientID() {
        return patientID;
    }

    public String getAllergyID() {
        return allergyID;
    }
    public void insertPatientAllergy(Connection connectHISDB) throws SQLException
    {
        PreparedStatement pAStatement = connectHISDB.prepareStatement(
                "INSERT INTO PatientAllergy " +
                        "(allergyID, substance, reaction, status, patientID) " +
                        "VALUES(?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE allergyID=allergyID, substance=substance, reaction=reaction, status=status, patientID=patientID");
        //System.out.println("pa.getAllergyID() = " + pa.getAllergyID());
        pAStatement.setString(1, this.getAllergyID());
        pAStatement.setString(2, this.getSubstance());
        pAStatement.setString(3, this.getReaction());
        pAStatement.setString(4, this.getStatus());
        pAStatement.setString(5, this.getPatientID());

        pAStatement.executeUpdate();
    }
}
