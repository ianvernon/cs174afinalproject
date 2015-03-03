package edu.ucsb.cs;

/**
 * Created by ianvernon on 3/3/15.
 */
public class Assigned
{
    private int authorID;
    private int patientID;
    private String participatingRole;

    public Assigned(int authorID, int patientID, String participatingRole)
    {
        this.authorID = authorID;
        this.patientID = patientID;
        this.participatingRole = participatingRole;
    }

}
