package players;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import db.PSQLConnection;

public class InsertPlayersData {
    public static void main(String[] args) {
        char separator = File.separatorChar;
        String path = "assets" + separator + "res" + separator + "data" + separator;

        List<String[]> datas = new ArrayList<>();

        try {
            String line;
            FileReader csv = new FileReader(path + "players.csv");
            BufferedReader players = new BufferedReader(csv);

            while ((line = players.readLine()) != null) {
                datas.add(line.split(","));
            }

            players.close();
            csv.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        String header = Arrays.toString(datas.remove(0));
        String sql = "insert into players (" + header.substring(1, header.length()-1) + 
                     ") values (?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            PSQLConnection psql = new PSQLConnection();
            Connection connection = psql.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            for(String[] data : datas){
                Player player = new Player(data);
                long birthdateMillis = Instant.ofEpochSecond(player.getBIRTHDATE().toEpochSecond(LocalTime.MIDNIGHT, ZoneOffset.UTC)).toEpochMilli();
                preparedStatement.setString(1, player.getName());
                preparedStatement.setString(2, player.getFirstname());
                preparedStatement.setString(3, player.getPosition());
                preparedStatement.setInt(4, player.getAge());
                preparedStatement.setString(5, player.getNationality());
                preparedStatement.setString(6, player.getTeam());
                preparedStatement.setInt(7, player.getJersey());
                preparedStatement.setInt(8, player.getHeight());
                preparedStatement.setDate(9, new Date(birthdateMillis));
                preparedStatement.setString(10, player.getBIRTHPLACE());
                preparedStatement.setString(11, player.getStrongFoot());
                preparedStatement.setString(12, player.getPATH());
                preparedStatement.execute();
            }

            System.out.println("All is ok!");
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
