package edu.ucsb.cs;

import java.util.Date;

public class Patient
{
    private String patientID; //check
    private String patientRole; //check
    private String givenName; //check
    private String familyName; //check
    private String suffix; //check
    private String gender; // check
    private Date birthtime; //check
    private String providerId; //check
    private Date xmlCreationdate; // check
    private int guardianNo; // check
    private int payerID; // check
    private String policyType; // check
    private String purpose; //chcek
    public Patient(String ID, String patientrole, String firstName, String givenname, String familyname, String suffix,
               String gender, Date birthtime, String providerid, Date xmlCreationDate, int guardianNo,
               int payerID, String policyType, String purpose)
    {
        this.patientID = ID;
        this.patientRole = patientrole;
        this.givenName = givenname;
        this.familyName = familyname;
        this.suffix = suffix;
        this.gender = gender;
        this.birthtime = birthtime;
        this.providerId = providerid;
        this.xmlCreationdate = xmlCreationDate;
        this.guardianNo = guardianNo;
            this.payerID = payerID;
            this.policyType = policyType;
            this.purpose = purpose;
    }
}
