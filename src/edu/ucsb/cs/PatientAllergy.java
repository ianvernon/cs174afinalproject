package edu.ucsb.cs;

/**
 * Created by ianvernon on 3/3/15.
 */
public class PatientAllergy
{
        private String substance;
        private String reaction;
        private String status;
        private int patientID;

        public PatientAllergy(String substance, String reaction, String status, int patientID)
        {
            this.substance = substance;
            this.reaction = reaction;
            this.status = status;
            this.patientID = patientID;
        }
}
