package edu.ucsb.cs;

/**
 * Created by ianvernon on 3/3/15.
 */
public class Author
{
    private String authorID;
    private String authorTitle;
    private String authorFirstName;
    private String authorLastName;

    public Author(String authorID, String authorTitle, String authorFirstName, String authorLastName)
    {
        this.authorID = authorID;
        this.authorTitle = authorTitle;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
    }

    public String getAuthorID() {
        return authorID;
    }

    public String getAuthorTitle() {
        return authorTitle;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }
}
