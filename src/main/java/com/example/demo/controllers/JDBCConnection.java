package com.example.demo.controllers;

import java.sql.Connection;
import java.sql.DriverManager;

public final class JDBCConnection {
    public static Connection getJDBCConnection() {
        Connection connection = null;
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/XEPDB1", "AQ", "warszawa");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
}