package edu.ucsb.cs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by ianvernon on 3/3/15.
 */
public class InsuranceCompany
{
    private String payerID;
    private String name;

    public InsuranceCompany(String payerID, String name)
    {
        this.payerID = payerID;
        this.name = name;
    }

    public String getPayerID() {
        return payerID;
    }

    public String getName() {
        return name;
    }
    public void insertInsuranceCompany(Connection connectHISDB) throws SQLException
    {

        PreparedStatement icStmt = connectHISDB.prepareStatement(
                "INSERT INTO InsuranceCompany " +
                        "(payerID, name) VALUES(?, ?) ON DUPLICATE KEY UPDATE payerID=payerID, name=name");

        icStmt.setString(1, this.getPayerID());
        icStmt.setString(2, this.getName());

        icStmt.executeUpdate();
    }
}
