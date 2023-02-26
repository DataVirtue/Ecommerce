package com.example.ecommerce;
import java.math.BigInteger;
import java.sql.*;
public class DatabaseConnection {

    String dbURl = "jdbc:mysql://localhost:3306/ecomm";


    String userName = "root";
    String password ="Mango@1801";

    private Statement getStatement (){

        try{
            Connection conn =  DriverManager.getConnection(dbURl, userName, password);
            return conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public  ResultSet getQueryTable(String query){
        Statement statement = getStatement();
        try {
            return statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int updateTable(String query){
        Statement statement = getStatement();
        try {
            return statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public BigInteger updateTable(String query, boolean returnId){

        Statement statement = getStatement();
        try {
             statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
             ResultSet rs = statement.getGeneratedKeys();
             if(rs!=null && rs.next()) return (BigInteger) rs.getObject(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new BigInteger(String.valueOf(-1));
    }
    public static void main(String[] args) {
        String query = "SELECT * FROM products";
        DatabaseConnection dbConn = new DatabaseConnection();
        ResultSet rs = dbConn.getQueryTable(query);
        if(rs !=null) System.out.println("Connected To Database");

    }

}
