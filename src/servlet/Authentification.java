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
import jakarta.servlet.http.HttpSession;
import utils.ContentType;

@WebServlet("/auth/authentification")
public class Authentification extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType(ContentType.contentTypeHTTP);
        PrintWriter out = res.getWriter();
        HttpSession session = req.getSession();

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try {
            String query = "select * from members where username = ?";
            PSQLConnection psql = new PSQLConnection();
            Connection connection = psql.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, username);
            ResultSet result = statement.executeQuery();

            if(!result.next()){
                res.sendRedirect("./register.html");
            } else {
                if(result.getString("password").equals(password)){
                    session.setAttribute("login", username);
                    session.setMaxInactiveInterval(3600);
                    res.sendRedirect("../user?username=" + username);
                } else {
                    res.sendRedirect("./login.html?auth=false");
                }
            }

            connection.close();
        } catch (Exception e) {
            out.print(e.getMessage());
        }
    }
}
