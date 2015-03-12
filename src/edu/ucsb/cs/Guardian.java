package edu.ucsb.cs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
    public void insertGuardian(Connection connectHISDB) throws SQLException
    {
        PreparedStatement guardianStmt = connectHISDB.prepareStatement(
                "INSERT INTO Guardian " +
                        "(guardianNo, givenName, familyName, phone, address, city, state, zip) " +
                        "VALUES(?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE guardianNo=guardianNo, givenName=givenName, familyName=familyName,"+
                        " phone=phone, address=address, city=city, state=state, zip=zip");
        guardianStmt.setString(1, this.getGuardianNo());
        guardianStmt.setString(2, this.getGivenName());
        guardianStmt.setString(3, this.getFamilyName());
        guardianStmt.setString(4, this.getPhone());
        guardianStmt.setString(5, this.getAddress());
        guardianStmt.setString(6, this.getCity());
        guardianStmt.setString(7, this.getState());
        guardianStmt.setString(8, this.getZip());
        guardianStmt.executeUpdate();
    }
}
