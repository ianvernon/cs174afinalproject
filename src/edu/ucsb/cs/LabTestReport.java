package edu.ucsb.cs;

import java.sql.Timestamp;
import java.util.Date;
/**
 * Created by ianvernon on 3/3/15.
 */

public class LabTestReport
{
    private String labTestResultID;
    private String patientVisitID;
    private java.sql.Date labTestPerformedDate;
    private String labTestType;
    private String referenceRangeLow;
    private String referenceRangeHigh;
    private String testResultValue;
    private String patientID;

    public LabTestReport(String labTestResultID, String patientVisitID, java.sql.Date labTestPerformedDate,
                         String labTestType, String referenceRangeLow, String referenceRangeHigh,
                         String testResultValue, String patientID)
    {
        this.labTestResultID = labTestResultID;
        this.patientVisitID = patientVisitID;
        this.labTestPerformedDate = labTestPerformedDate;
        this.labTestType = labTestType;
        this.referenceRangeLow = referenceRangeLow;
        this.referenceRangeHigh = referenceRangeHigh;
        this.testResultValue = testResultValue;
        this.patientID = patientID;
    }

    public String getLabTestResultID() {
        return labTestResultID;
    }

    public String getPatientVisitID() {
        return patientVisitID;
    }

    public java.sql.Date getLabTestPerformedDate() {
        return labTestPerformedDate;
    }

    public String getLabTestType() {
        return labTestType;
    }

    public String getReferenceRangeLow() {
        return referenceRangeLow;
    }

    public String getReferenceRangeHigh() {
        return referenceRangeHigh;
    }

    public String getTestResultValue() {
        return testResultValue;
    }

    public String getPatientID() {
        return patientID;
    }
}
