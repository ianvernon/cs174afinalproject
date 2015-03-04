package edu.ucsb.cs;

import java.util.Date;
/**
 * Created by ianvernon on 3/3/15.
 */

public class LabTestReport
{
    private int labTestResultID;
    private int patientVisitID;
    private Date labTestPerformedDate;
    private String labTestType;
    private double referenceRangeLow;
    private double referenceRangeHigh;
    private double testResultValue;
    private int patientID;

    public LabTestReport(int labTestResultID, int patientVisitID, Date labTestPerformedDate,
                         String labTestType, double referenceRangeLow, double referenceRangeHigh,
                         double testResultValue, int patientID)
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
}
