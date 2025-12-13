package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import db.PSQLConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ContentType;

@WebServlet("/search")
public class Search extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType(ContentType.contentTypeHTTP);
        PrintWriter out = res.getWriter();

        String parameter = req.getParameter("player");

        out.print
        (
            "<head>" +
                "<title>Search : " + parameter + "</title>" +
                "<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css\">" +
                "<link rel=\"stylesheet\" href=\"assets/styles/styles.css\">" +
                "<link rel=\"stylesheet\" href=\"assets/styles/search.css\">" +
                "<link rel=\"preload\" href=\"assets/font/Poppins.ttf\" as=\"font\" type=\"font/woff2\" crossorigin>" +
            "</head>"
        );

        out.print("<body>");

        out.print
        (
            "<header>" +
            "<h1><a href=\"index.html\">SOCCER PLAYER WIKI</a></h1>" +
                "<ul>" +
                    "<li><i class=\"bi bi-brightness-high-fill\"></i></li>" +
                    "<li><a href=\"pages/login.html\"><i class=\"bi bi-person-fill\"></i></a></li>" +
                "</ul>" +
            "</header>"
        );

        out.print("<main>");
        out.print("<section>");

        out.print
        (
            "<form action=\"./search\" method=\"get\" class=\"search\">" +
                "<input type=\"search\" name=\"player\" placeholder=\"" + parameter + "\">" +
                "<button type=\"submit\">Rechercher un joueur</button>" +
                "<div class=\"filter\"><i class=\"bi bi-funnel-fill\"></i></div>" +
            "</form>"
        );

        if(parameter.equals("")) out.print("<p>Aucune recherche n'a été renseignée.</p>");

        // ---------------------------------------------------------------------------------------------------------

        try {
            String queryOne = "select * from players where name = ? and firstname = ?";
            String queryTwo = "select * from players where name like ? or firstname like ?";
            
            PSQLConnection psql = new PSQLConnection();
            Connection connection = psql.getConnection();
            
            String[] player = parameter.split(" ");
            String name, firstname; 
            
            if(player.length >= 2){
                name = nameVerification(player[0]);
                if(player.length > 2) {
                    for(int i = 1; i< player.length-1; i++) 
                        name += " " + player[i];
                    firstname = nameVerification(player[player.length-1]);
                } else {
                    firstname = nameVerification(player[1]);
                }
                
                PreparedStatement preparedStatement = connection.prepareStatement(queryOne);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, firstname);
                ResultSet result = preparedStatement.executeQuery();
                
                if(result.next())
                    res.sendRedirect("player?id=" + result.getInt("id"));
                else 
                    out.print("<p>Nous n'avons trouvé aucun joueur correspondant à votre recherche.</p>");
            } 
            
            else if(player.length == 1){
                name = player[0];

                PreparedStatement preparedStatement = connection.prepareStatement(queryTwo);
                preparedStatement.setString(1, "%"+name+"%");
                preparedStatement.setString(2, "%"+name+"%");
                ResultSet result = preparedStatement.executeQuery();
                
                if(!result.next()) {
                    name = nameVerification(player[0]);
                    preparedStatement.setString(1, "%"+name+"%");
                    preparedStatement.setString(2, "%"+name+"%");
                    result = preparedStatement.executeQuery();

                    if(!result.next()) 
                        out.print("<p>Nous n'avons trouvé aucun joueur correspondant à votre recherche.</p>");
                    else {
                        out.print(
                            "<div class=\"result\">" +
                                "<a href=\"player?id=" + result.getInt("id") + "\"><div class=\"player\">" +
                                    "<img src=\"assets/img/" + result.getString("info_path") + ".png\" alt=\"player\">" +
                                    "<p>" + result.getString("name") + " " + result.getString("firstname") + "</p>" +
                                "</div></a>"
                        );
                        while (result.next()) {
                            out.print(
                                "<a href=\"player?id=" + result.getInt("id") + "\"><div class=\"player\">" +
                                    "<img src=\"assets/img/" + result.getString("info_path") + ".png\" alt=\"player\">" +
                                    "<p>" + result.getString("name") + " " + result.getString("firstname") + "</p>" +
                                "</div></a>"
                            );
                        }
                        out.print("</div>");
                    }
                }

                else {
                    out.print(
                        "<div class=\"result\">" +
                            "<a href=\"player?id=" + result.getInt("id") + "\"><div class=\"player\">" +
                                "<img src=\"assets/img/" + result.getString("info_path") + ".png\" alt=\"player\">" +
                                "<p>" + result.getString("name") + " " + result.getString("firstname") + "</p>" +
                            "</div></a>"
                    );
                    while (result.next()) {
                        out.print(
                            "<a href=\"player?id=" + result.getInt("id") + "\"><div class=\"player\">" +
                                "<img src=\"assets/img/" + result.getString("info_path") + ".png\" alt=\"player\">" +
                                "<p>" + result.getString("name") + " " + result.getString("firstname") + "</p>" +
                            "</div></a>"
                        );
                    }
                    out.print("</div>");
                }
            } 
            
            else {
                out.print("<p>Aucune recherche n'a été renseignée.</p>");
            }

            connection.close();
        } catch (Exception e) {
            out.print(e.getMessage());
        }

        // ---------------------------------------------------------------------------------------------------------

        out.print("</section>");
        out.print("</main>");
        out.print("</body>");
    }

    private String nameVerification(String name){
        String result = spaceVerification(name);
        String firstOccurence = result.substring(0, 1);
        return result.replaceFirst(firstOccurence, firstOccurence.toUpperCase());
    }

    private String spaceVerification(String string){
        String result;
        int idxBegin;
        int idxEnd;

        idxBegin = 0;
        while(string.charAt(idxBegin) == ' '){
            idxBegin++;
        }
        
        idxEnd = string.length()-1; 
        while (string.charAt(idxEnd) == ' ') {
            idxEnd--;
        }

        result = string.substring(idxBegin, idxEnd+1);
        return result.toLowerCase();
    }
}