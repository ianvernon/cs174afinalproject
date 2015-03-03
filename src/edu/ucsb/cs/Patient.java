package edu.ucsb.cs;

import java.util.Date;

public class Patient {
private String patientid;
private String patientrole;
private String givenname;
private String familyname;
private String suffix;
private String gender;
private Date birthtime;
private String providerId;
private Date xmlCreationdate;
public Patient(String ID, String firstName, String givenname, String familyname, String suffix, String gender, Date birthtime, String providerid, Date xmlCreationDate)
{
this.patientid = ID;
this.patientrole = patientrole;
this.givenname = givenname;
this.familyname = familyname;
this.suffix = suffix;
this.gender = gender;
this.birthtime = birthtime;
this.providerId = providerid;
this.xmlCreationdate = xmlCreationDate;
}
}
