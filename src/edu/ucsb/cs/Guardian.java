package edu.ucsb.cs;

/**
 * Created by ianvernon on 3/3/15.
 */
public class Guardian {
    private int guardianNo;
    private String givenName;
    private String familyName;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String zip;

    public Guardian(int guardianNo, String givenName, String familyName, String phone,
                    String address, String city, String state, String zip)
    {
        this.guardianNo = guardianNo;
        this.givenName = givenName;
        this.familyName = familyName;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }
}
