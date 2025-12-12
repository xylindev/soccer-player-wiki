package db;

import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        try {
            PSQLConnection psqlConnection = new PSQLConnection();
            Connection connection = psqlConnection.getConnection();

            if(connection != null)
                System.out.println("All is ok!");
            
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
