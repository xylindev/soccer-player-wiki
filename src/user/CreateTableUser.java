package user;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;

import db.PSQLConnection;

public class CreateTableUser {
    public static void main(String[] args) {
        char separator = File.separatorChar;
        String path = "assets" + separator + "sql" + separator + "members.sql";
        String sql = "";

        try {
            String line;
            FileReader members = new FileReader(path);
            BufferedReader reader = new BufferedReader(members);

            while ((line = reader.readLine()) != null) { sql += line; }

            PSQLConnection psql = new PSQLConnection();
            Connection connection = psql.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();

            System.out.println("All is ok!");

            members.close();
            reader.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
