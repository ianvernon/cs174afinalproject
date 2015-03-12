package edu.ucsb.cs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by ianvernon on 3/3/15.
 */
public class Assigned
{
    private String authorID;
    private String patientID;
    private String participatingRole;

    public Assigned(String authorID, String patientID, String participatingRole)
    {
        this.authorID = authorID;
        this.patientID = patientID;
        this.participatingRole = participatingRole;
    }

    public String getAuthorID() {

        return authorID;
    }

    public String getPatientID() {
        return patientID;
    }

    public String getParticipatingRole() {
        return participatingRole;
    }
    public void insertAssigned(Connection connectHISDB) throws SQLException
    {
        PreparedStatement assignedStmt = connectHISDB.prepareStatement(
                "INSERT INTO Assigned " +
                        "(authorID, patientID, participatingRole)" +
                        " VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE authorID=authorID , patientID=patientID, participatingRole=participatingRole");

        assignedStmt.setString(1, this.getAuthorID());
        assignedStmt.setString(2, this.getPatientID());
        assignedStmt.setString(3, this.getParticipatingRole());

        assignedStmt.executeUpdate();
    }
}
