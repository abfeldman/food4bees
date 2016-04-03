package nl.food4bees.backend.user;

import java.io.IOException;

import java.sql.SQLException;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nl.food4bees.backend.Config;

/**
 * A user registration confirmation servlet.
 *
 * @author Alexander Feldman
 */

public class VerifyRegistrationServlet extends HttpServlet
{
    private static String sourceClass = RegisterServlet.class.getName();
    private static Logger logger = Logger.getLogger(sourceClass);

    @Override
    public void doGet(HttpServletRequest request,
                       HttpServletResponse response)
        throws IOException, ServletException
    {
        String token = request.getParameter("token");

        try {
            Config config = Config.instance();

            String URL = config.getProperty("url");

            Database db = new Database();

            Integer uid = db.findToken(token);
            if (uid == null) {
                request.setAttribute("caption", "Email Verification Error");
                request.setAttribute("message", "Your registration request has already been processed.");
                request.setAttribute("destination", URL + "login.jsp");
                request.getRequestDispatcher("message.jsp").forward(request, response);

                return;
            }

            db.enableUser(uid);
            db.deleteToken(token);

            request.setAttribute("caption", "Email Verification Succeeded");
            request.setAttribute("message", "Thank you for confirming your email address. You can now log in.");
            request.setAttribute("destination", URL + "login.jsp");
            request.getRequestDispatcher("message.jsp").forward(request, response);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Received a database exception.", e);

            request.getRequestDispatcher("login.jsp").forward(request, response);

            return;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Received an unexpected exception.", e);

            request.getRequestDispatcher("login.jsp").forward(request, response);

            return;
        }
    }
}
