package edu.ucsb.cs;

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
}
