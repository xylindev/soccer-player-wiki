package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import db.PSQLConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ContentType;

@WebServlet("/auth/register")
public class Register extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType(ContentType.contentTypeHTTP);
        PrintWriter out = res.getWriter();

        String name = req.getParameter("name");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        
        try {
            String insert = "insert into members values (?,?,?)";
            PSQLConnection psql = new PSQLConnection();
            Connection connection = psql.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insert);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, password);

            preparedStatement.execute();

            res.sendRedirect("./login.html");

            connection.close();
        } catch (Exception e) {
            out.print(e.getMessage());
        }
    }
}
