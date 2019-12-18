/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thanv
 */
public class ConnectionUtil {
    public static Connection con;
    public static Connection conDB()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://remotemysql.com:3306/8gQHxi21p3", "8gQHxi21p3", "aqyoPY4Hp2");
//            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/apdb", "root", "1234");
            return con;
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println("ConnectionUtil : "+ex.getMessage());
           return null;
        }
    }
    public static String getDBName()
    {
        return "8gQHxi21p3";
    }
    
    public static boolean updateQueryWithPreparedStatment(String preparedQuery, String[] inputString) {
        try {
            PreparedStatement st = con.prepareStatement(preparedQuery);
            for (int i = 0; i < inputString.length; i++) {
                st.setString(i+1, inputString[i]);
            }
            st.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    public static ResultSet executeQueryWithPreparedStatement(
            String preparedQuery,
            String[] inputString) throws SQLException 
    {
        PreparedStatement st;
        st = con.prepareStatement(preparedQuery);
        for (int i = 0; i < inputString.length; i++) {
            st.setString(i+1, inputString[i]);
        }
        
        return st.executeQuery();
    }
}
