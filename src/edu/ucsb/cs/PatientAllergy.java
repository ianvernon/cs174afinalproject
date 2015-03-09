package edu.ucsb.cs;

/**
 * Created by ianvernon on 3/3/15.
 */
public class PatientAllergy
{
        private String allergyID;
        private String substance;
        private String reaction;
        private String status;
        private String patientID;

        public PatientAllergy(String allergyID, String substance, String reaction, String status, String patientID)
        {
            this.allergyID = allergyID;
            this.substance = substance;
            this.reaction = reaction;
            this.status = status;
            this.patientID = patientID;
        }

    public String getSubstance() {
        return substance;
    }

    public String getReaction() {
        return reaction;
    }

    public String getStatus() {
        return status;
    }

    public String getPatientID() {
        return patientID;
    }

    public String getAllergyID() {
        return allergyID;
    }
}
