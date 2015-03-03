package edu.ucsb.cs;

import java.io.IOException;

import javax.xml.parsers.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.sql.Connection;
import java.sql.DriverManager;

public class parser {
public static void main() {
    try {


        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        String url = "jdbc:mysql://localhost:3306/dbname";
        String user = "root";
        String password = "";

        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection(url, user, password);
        st = con.createStatement();
        rs = st.executeQuery("SELECT * FROM healthmessagesexchange.messagequeue ORDER BY control_id DESC LIMIT 1;");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        DocumentBuilder db = dbf.newDocumentBuilder();

        Document dom = db.parse(rs.getString(0));

    } catch (ParserConfigurationException pce) {
        pce.printStackTrace();
    } catch (SAXException se) {
        se.printStackTrace();
    } catch (IOException ioe) {
        ioe.printStackTrace();
    } catch (Exception ex){
        ex.printStackTrace();
    }
}
}

