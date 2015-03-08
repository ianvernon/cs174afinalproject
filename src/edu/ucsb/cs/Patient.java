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
    private String birthtime; //check
    private String providerId; //check
    private String xmlCreationDate; // check
    private String guardianNo; // check
    private String payerID; // check
    private String policyType; // check
    private String purpose; //chcek
    public Patient(String ID, String patientrole, String givenName, String familyname, String suffix,
               String gender, String birthtime, String providerid, String xmlCreationDate, String guardianNo,
               String payerID, String policyType, String purpose)
    {
        this.patientID = ID;
        this.patientRole = patientrole;
        this.givenName = givenName;
        this.familyName = familyname;
        this.suffix = suffix;
        this.gender = gender;
        this.birthtime = birthtime;
        this.providerId = providerid;
        this.xmlCreationDate = xmlCreationDate;
        this.guardianNo = guardianNo;
        this.payerID = payerID;
        this.policyType = policyType;
        this.purpose = purpose;
    }

    public String getPatientID() {
        return patientID;
    }

    public String getPatientRole() {
        return patientRole;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthtime() {
        return birthtime;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getXmlCreationDate() {
        return xmlCreationDate;
    }

    public String getGuardianNo() {
        return guardianNo;
    }

    public String getPayerID() {
        return payerID;
    }

    public String getPolicyType() {
        return policyType;
    }

    public String getPurpose() {
        return purpose;
    }


    public void setPatientRole(String patientRole) {
        this.patientRole = patientRole;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirthtime(String birthtime) {
        this.birthtime = birthtime;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public void setXmlCreationDate(String xmlCreationDate) {
        this.xmlCreationDate = xmlCreationDate;
    }

    public void setGuardianNo(String guardianNo) {
        this.guardianNo = guardianNo;
    }

    public void setPayerID(String payerID) {
        this.payerID = payerID;
    }

    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}
