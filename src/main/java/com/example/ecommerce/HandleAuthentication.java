package com.example.ecommerce;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HandleAuthentication {


    private static byte[] getSha(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(input.getBytes(StandardCharsets.UTF_8));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getEncryptedString(String input) {
        try {
            BigInteger num = new BigInteger(1, getSha(input));
            StringBuilder hexString = new StringBuilder(num.toString(16));
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static User signUp(String userName, String password, String email) {
        DatabaseConnection dbconn = new DatabaseConnection();
        int rs = dbconn.updateTable("insert into users (username,password,email) " +
                "values( '" + userName + "','" + getEncryptedString(password) + "','" + email + "' )");

        if (rs > 0) return authenticate(userName,password);

        return null;
    }

    static User authenticate(String userName, String pass) {
        DatabaseConnection dbconn = new DatabaseConnection();
        String password = getEncryptedString(pass);
        ResultSet rs = dbconn.getQueryTable("select * from users where username = '" + userName + "' and password = '" + password + "'");

        try {
            if (rs != null && rs.next()) {


                User loggedInUser = new User(rs.getString("username"),
                        rs.getString("email"),
                        rs.getInt("uid"));
                return loggedInUser;
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;
    }

    public static void main(String[] args) {

    }
}
