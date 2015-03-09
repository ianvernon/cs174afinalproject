package edu.ucsb.cs;

/**
 * Created by ianvernon on 3/3/15.
 */
public class Guardian {
    private String guardianNo;
    private String givenName;
    private String familyName;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String zip;

    public Guardian(String guardianNo, String givenName, String familyName, String phone,
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

    public String getGuardianNo() {
        return guardianNo;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }
}
