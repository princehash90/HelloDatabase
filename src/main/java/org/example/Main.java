package org.example;

import java.sql.*;
import java.util.Collections;

public class Main {
    public static void main(String[] args) throws SQLException {

        String url = "jdbc:sqlite:hello.sqlite";
        Connection connection= DriverManager.getConnection(url);
        Statement statement = connection.createStatement();

        //String createTableSQL = "CREATE TABLE cats(Name TEXT, age INTEGER)";
       // statement.execute(createTableSQL);

       //String insertdataSQL= "INSERT INTO cats VALUES ('Mary',10)";
       //statement.execute(insertdataSQL);
        //String insertdataSQL = "INSERT INTO cats VALUES ('Hello Kitty',12)";
        //statement.execute(insertdataSQL);

        String getAllDataSQL = "SELECT * FROM cats";
        ResultSet allcats = statement.executeQuery(getAllDataSQL);

        while (allcats.next()){
            String name = allcats.getString("name");
            int age = allcats.getInt("age");
            System.out.println(name + " is "+age + "years old" );
        }

        //String deletedataSql= "DELETE FROM cats VALUES('Hello kitty',12)";
        //statement.execute(deletedataSql);

    }
}