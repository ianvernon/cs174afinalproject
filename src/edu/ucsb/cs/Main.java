package edu.ucsb.cs;



import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;


public class Main {

    private static Scanner inFromConsole = new Scanner(System.in);
    //DONE
    public static void main(String[] args)
    {
        DataGrabber dg1 = new DataGrabber("healthmessagesexchange3", "messages", "HealthInformationSystem");
        DataGrabber dg2 = new DataGrabber("healthmessagesexchange3", "messages2", "HealthInformationSystem");
	    //grabData("healthmessagesexchange3", "messages2", "HealthInformationSystem");
        mainMenu();


    }
    //DONE
    public static void displayMainMenu()
    {
        System.out.println("************ Welcome to HealthInformationSystem! **************");
        System.out.println("If you want to exit, enter -1.");
        System.out.println("Privilege levels are as follows:");
        System.out.println("\tPatient: 0");
        System.out.println("\tDoctor: 1");
        System.out.println("\tAdministrator: 2");
        System.out.println("Please enter privilege level: ");
    }
    //DONE
    public static void mainMenu()
    {
        String privilegeLevel;
        //displayMainMenu();
        do {
            displayMainMenu();
            privilegeLevel = inFromConsole.next();

            // patient case
            if (privilegeLevel.equals("0")) {
                //patientCase();
                PatientCase patientCase = new PatientCase();
            }
            // doctor case;
            else if (privilegeLevel.equals("1")) {
                //doctorCase();
                DoctorCase doctorCase = new DoctorCase();
            }
            //admin case
            else if (privilegeLevel.equals("2")) {
                //adminCase();
                AdminCase adminCase = new AdminCase();

            }
            else if(privilegeLevel.equals("-1"))
            {
                System.out.println("**********EXITING***********");
                return;
            }
            // should never get here, but just to be safe!
            else {
                while (!validPrivilege(privilegeLevel)) {
                    System.out.println("Not a valid privilege. Input -1 to exit. Otherwise, Please try again: ");
                    privilegeLevel = inFromConsole.next();
                    if (privilegeLevel.equals("-1")) {
                        System.out.println("*************EXITING***************");
                        return;
                    }
                }
            }
        }while(!privilegeLevel.equals("-1"));
        System.out.println("************EXITING*************");

    }
    public static boolean validPrivilege(String input)
    {
        if(!(input.equals("0") || input.equals("1") || input.equals("2")))
        {
            return false;
        }

        else
        {
            return true;
        }
    }
}
