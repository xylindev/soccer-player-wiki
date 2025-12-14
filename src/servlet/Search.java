package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
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
                    "<li><a href=./user><i class=\"bi bi-person-fill\"></i></a></li>" +
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

        // ---------------------------------------------------------------------------------------------------------

        try {
            String queryOne = "select * from players where name = ? and firstname = ?";
            String queryTwo = "select * from players where name like ? or firstname like ?";
            
            PSQLConnection psql = new PSQLConnection();
            Connection connection = psql.getConnection();
            
            List<String> player = inputVerification(parameter.split(" "));

            String name = "";
            String firstname; 
            
            if(player.size() >= 2){
                firstname = player.get(0);
                if(player.size() > 2) {
                    for(int i=1; i<player.size(); i++){
                        name += player.get(i);
                        if(i<player.size()-1)
                            name += " ";
                    }
                } else {
                    name = player.get(1);
                }
                
                PreparedStatement preparedStatement = connection.prepareStatement(queryOne);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, firstname);
                ResultSet result = preparedStatement.executeQuery();
                
                if(result.next())
                    res.sendRedirect("player?id=" + result.getInt("id"));
                else {
                    preparedStatement.setString(1, firstname);
                    preparedStatement.setString(2, name);
                    result = preparedStatement.executeQuery();
                    
                    if(result.next())
                        res.sendRedirect("player?id=" + result.getInt("id"));
                    else 
                        out.print("<p>Nous n'avons trouvé aucun joueur correspondant à votre recherche.</p>");
                }
            } 
            
            else if(player.size() == 1){
                name = player.get(0);
                PreparedStatement preparedStatement = connection.prepareStatement(queryTwo);
                preparedStatement.setString(1, "%"+name+"%");
                preparedStatement.setString(2, "%"+name+"%");
                ResultSet result = preparedStatement.executeQuery();
                boolean wasNull = false;

                out.print("<div class=\"result\">");

                while(result.next()) {
                    out.print(
                        "<a href=\"player?id=" + result.getInt("id") + "\"><div class=\"player\">" +
                            "<img src=\"assets/img/" + result.getString("info_path") + ".png\" alt=\"player\">" +
                            "<p>" + result.getString("firstname") + " " + result.getString("name") + "</p>" +
                        "</div></a>"
                    );
                    wasNull = true;
                }

                preparedStatement.setString(1, "%"+name.toLowerCase()+"%");
                preparedStatement.setString(2, "%"+name.toLowerCase()+"%");
                result = preparedStatement.executeQuery();

                while (result.next()) {
                    out.print(
                        "<a href=\"player?id=" + result.getInt("id") + "\"><div class=\"player\">" +
                        "<img src=\"assets/img/" + result.getString("info_path") + ".png\" alt=\"player\">" +
                        "<p>" + result.getString("firstname") + " " + result.getString("name") + "</p>" +
                        "</div></a>"
                    );
                    wasNull = true;
                }

                out.print("</div>");

                if(!wasNull) 
                    out.print("<p>Nous n'avons trouvé aucun joueur correspondant à votre recherche.</p>");
            }

            else { out.print("<p>Aucune recherche n'a été renseignée.</p>"); }

            connection.close();
        } catch (Exception e) {
            out.print(e.getMessage());
        }

        // ---------------------------------------------------------------------------------------------------------

        out.print("</section>");
        out.print("</main>");
        out.print("</body>");
    }

    private List<String> inputVerification(String[] player){
        List<String> result = new ArrayList<>();

        for(int idx=0; idx<player.length; idx++)
            if(!player[idx].equals(""))
                result.add(nameVerification(player[idx]));

        return result;
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