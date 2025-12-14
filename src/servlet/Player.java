package servlet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import db.PSQLConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ContentType;

@WebServlet("/player")
public class Player extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType(ContentType.contentTypeHTTP);
        PrintWriter out = res.getWriter();

        String parameter = req.getParameter("id");

        out.print
        (
            "<head>" +
                "<title> </title>" +
                "<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css\">" +
                "<link rel=\"stylesheet\" href=\"assets/styles/styles.css\">" +
                "<link rel=\"stylesheet\" href=\"assets/styles/player.css\">" +
                "<link rel=\"preload\" href=\"assets/font/Poppins.ttf\" as=\"font\" type=\"font/woff2\" crossorigin>" +
            "</head>"
        );

        out.print
        (
            "<header>" +
            "<h1><a href=\"index.html\">SOCCER PLAYER WIKI</a></h1>" +
                "<ul>" +
                    "<li><i class=\"bi bi-brightness-high-fill\"></i></li>" +
                    "<li><a href=./user><i class=\"bi bi-person-fill\"></i></a></li>" +
                "</ul>" +
            "</header>"
        );

        out.print("<main>");
        out.print("<section>");

        out.print
        (
            "<form action=\"./search\" method=\"get\" class=\"search\">" +
                "<input type=\"search\" name=\"player\">" +
                "<button type=\"submit\">Rechercher un joueur</button>" +
            "</form>"
        );

        
        try {
            String query = "select * from players where id = ?";

            PSQLConnection psql = new PSQLConnection();
            Connection connection = psql.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, Integer.parseInt(parameter));
            ResultSet result = preparedStatement.executeQuery();
            
            if(!result.next()) 
                out.print("<p>Aucun enregistrement ne correspond au joueur recherché.</p>");
            else {
                String biography = "";
                String statistics = "";
                List<String> statisticList = new ArrayList<>();

                String pathBiography = "webapps/soccer-player-info/assets/res/player-biography/" + result.getString("info_path") + ".txt";
                String pathStatistic = "webapps/soccer-player-info/assets/res/player-statistic/" + result.getString("info_path") + ".csv";

                try {
                    String line;
                    FileReader biographyFile = new FileReader(pathBiography);
                    FileReader statisticFile = new FileReader(pathStatistic);
                    BufferedReader readerBiography = new BufferedReader(biographyFile);
                    BufferedReader readerStatistic = new BufferedReader(statisticFile);

                    biography += readerBiography.readLine();

                    while ((line = readerStatistic.readLine()) != null) {
                        statisticList.add(line);
                    }

                    biographyFile.close();
                    statisticFile.close();
                    readerBiography.close();
                    readerStatistic.close();
                } catch (Exception e) {
                    out.print("<p class=\"red error\"> Une erreur s’est produite lors de l’extraction des données demandées. </p>");
                }

                statistics += "<table>";
                for(String getContent : statisticList){
                    String[] stats = getContent.split(",");
                    statistics += "<tr>";
                    for(int idx = 0; idx < stats.length; idx++){
                        statistics += "<th>" + stats[idx] + "</th>";
                    }
                    statistics += "</tr>";
                }
                statistics += "</table>";

                int age = result.getInt("age");
                String nationality = result.getString("nationality");
                int height = result.getInt("height");
                Date birthdate = result.getDate("birthdate");
                String birthplace = result.getString("birthplace");
                String team = result.getString("team");
                int jersey = result.getInt("jersey");
                String position = result.getString("position");
                String strongFoot = result.getString("strong_foot");

                switch (position) {
                    case "ATT":
                        position = "Attaquant";
                        break;
                    case "DEF":
                        position = "Défenseur";
                        break;
                    case "MIL":
                        position = "Milieu";
                        break;
                    case "GAR":
                        position = "Gardien";
                        break;
                    default:
                        break;
                }

                out.print
                (
                    "<div class=\"player_info\">" +
                        "<div class=\"player\">" +
                            "<div class=\"img\"><img src=\"assets/img/" + result.getString("info_path") + ".png\"" + "alt=\"" + result.getString("name") + " " + result.getString("firstname") + "\"></div>" +
                            "<div class=\"description\">" +
                                "<p> <span class=\"bold\">Age :</span> " + age + "</p>" +
                                "<p> <span class=\"bold\">Nationalité :</span> " + nationality + "</p>" +
                                "<p> <span class=\"bold\">Taille :</span> " + height + " cm</p>" +
                                "<p> <span class=\"bold\">Date de naissance :</span> " + birthdate + "</p>" +
                                "<p> <span class=\"bold\">Lieu de naissance :</span> " + birthplace + "</p>" +
                                "<p> <span class=\"bold\">Team :</span> " + team + "</p>" +
                                "<p> <span class=\"bold\">Maillot :</span> " + jersey + "</p>" +
                                "<p> <span class=\"bold\">Position :</span> " + position + "</p>" +
                                "<p> <span class=\"bold\">Pied fort :</span> " + strongFoot + "</p>" +
                            "</div>" +
                        "</div>" +
                        "<div class=\"information\">" +
                            "<div class=\"name\">" +
                                "<h2>" + result.getString("firstname") + " " + result.getString("name") + "</h2>" +
                                "<form action=\"favorite\" method=\"post\"><button name=\"favorite\"><i class=\"bi bi-star-fill\"></i></button></form>" +
                            "</div>" +

                            "<div class=\"data\">" +
                                "<div class=\"biography\">" +
                                    "<p>" + biography + "</p>" +
                                "</div>" +
                                "<div class=\"statistic\">" +
                                    "<h2> Statistiques </h2>" + 
                                    statistics +
                                "</div>" +
                            "</div>"+
                        "</div>" +
                    "</div>"
                );
            }

            connection.close();
        } catch (Exception e) {
            if(parameter == null || parameter.equals("")) 
                out.print("<p class=\"error\">Vous n’avez saisi aucun terme de recherche. Merci d’en indiquer un pour continuer.</p>");
        }
    }
}
