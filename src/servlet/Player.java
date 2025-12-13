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
                    "<li><a href=\"pages/login.html\"><i class=\"bi bi-person-fill\"></i></a></li>" +
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
                "<div class=\"filter\"><i class=\"bi bi-funnel-fill\"></i></div>" +
            "</form>"
        );

        
        try {
            String query = "select * from players where id = ?";
            PSQLConnection psql = new PSQLConnection();
            Connection connection = psql.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, Integer.parseInt(parameter));
            ResultSet result = preparedStatement.executeQuery();
            
            if(parameter.equals("") || !result.next()) 
                out.print("<p>Nous n'avons trouvé aucun joueur correspondant à votre recherche.</p>");
            else 
                out.print
                (
                    "<img src=\"assets/img/" + result.getString("info_path") + ".png\"" + 
                    "alt=\"" + result.getString("name") + " " + result.getString("firstname") + "\">"
                );

            connection.close();
        } catch (Exception e) {
            out.print(e.getMessage());
        }
    }
}
