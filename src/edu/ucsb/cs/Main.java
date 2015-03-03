package edu.ucsb.cs;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;


public class Main {

    public static void main(String[] args) {
	// write your code here
        System.out.println("Hello world!!!!!");

        Connection connect = null;
        Statement statement = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(
                    "jdbc:mysql://localhost/healthmessagesexchange2?"+
                            "user=root&password="
            );

            statement = connect.createStatement();

            resultSet = statement.executeQuery("select * from healthmessagesexchange2.messages");
            //writeResultSet(resultSet);
            while(resultSet.next())
            {
                String user = resultSet.getString("FamilyName");
                System.out.println(user);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }


    }
}
