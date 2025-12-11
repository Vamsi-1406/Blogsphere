package com.blogsphere;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestH2Connection {
    public static void main(String[] args) {
        System.out.println("Testing H2 in-memory database connection...");
        try {
            // Load the H2 driver
            Class.forName("org.h2.Driver");
            
            // Connect to the H2 in-memory database
            Connection conn = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
            System.out.println("Connection established successfully!");
            
            // Create a test table
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE TEST(ID INT PRIMARY KEY, NAME VARCHAR(255))");
            stmt.execute("INSERT INTO TEST VALUES(1, 'Hello')");
            
            // Query the data
            ResultSet rs = stmt.executeQuery("SELECT * FROM TEST");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("ID") + ", NAME: " + rs.getString("NAME"));
            }
            
            // Close resources
            rs.close();
            stmt.close();
            conn.close();
            
            System.out.println("H2 database test completed successfully!");
        } catch (Exception e) {
            System.err.println("Error testing H2 connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 