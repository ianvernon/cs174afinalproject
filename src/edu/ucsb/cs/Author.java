package edu.ucsb.cs;

/**
 * Created by ianvernon on 3/3/15.
 */
public class Author
{
    private int authorID;
    private String authorTitle;
    private String authorFirstName;
    private String authorLastName;

    public Author(int authorID, String authorTitle, String authorFirstName, String authorLastName)
    {
        this.authorID = authorID;
        this.authorTitle = authorTitle;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
    }
}
