package players;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import db.PSQLConnection;

public class CreatePlayersTable {
    public static void main(String[] args) {
        char separatorChar = File.separatorChar;
        String path = "assets" + separatorChar + "sql" + separatorChar;
        String sql = "";
        
        try {
            String line;
            FileReader sqlPlayers = new FileReader(path + "players.sql");
            BufferedReader reader = new BufferedReader(sqlPlayers);

            while((line = reader.readLine()) != null){
                sql += line;
            }

            reader.close();
            sqlPlayers.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            PSQLConnection psql = new PSQLConnection();
            Connection connection = psql.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            if(!preparedStatement.execute())
                System.out.println("All is ok");;

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
