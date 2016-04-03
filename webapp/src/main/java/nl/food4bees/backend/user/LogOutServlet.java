package nl.food4bees.backend.user;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.SQLException;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * A user logout servlet.
 *
 * @author Alexander Feldman
 */

public class LogOutServlet extends HttpServlet
{
    private static String sourceClass = LogOutServlet.class.getName();
    private static Logger logger = Logger.getLogger(sourceClass);

    @Override
    public void doGet(HttpServletRequest request,
                       HttpServletResponse response)
        throws IOException, ServletException
    {
        HttpSession session = request.getSession(true);

        session.invalidate();

        String ref = request.getParameter("ref");
        if (ref != null) {
            response.sendRedirect(ref);
        }
    }
}
