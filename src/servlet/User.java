package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.ContentType;

@WebServlet("/user")
public class User extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType(ContentType.contentTypeHTTP);
        PrintWriter out = res.getWriter();

        HttpSession session = req.getSession();
        String login = (String) session.getAttribute("login");

        if(login == null){
            res.sendRedirect("./auth/login.html");
            return;
        }

        if(req.getParameter("username") == null || !req.getParameter("username").equals(login)){
            res.sendRedirect("./user?username=" + login);
            return;
        }

        try {
            
        } catch (Exception e) {
            out.print(e.getMessage());
        }
    }
}
