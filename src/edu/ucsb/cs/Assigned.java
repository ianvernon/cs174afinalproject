package edu.ucsb.cs;

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
}
