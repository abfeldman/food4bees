package nl.food4bees.backend.user;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.SQLException;

import java.util.Map;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nl.food4bees.backend.Util;
import nl.food4bees.backend.Config;

/**
 * A new password servlet that uses POST.
 *
 * @author Alexander Feldman
 */

public class NewPasswordServlet extends HttpServlet
{
    private static String sourceClass = RegisterServlet.class.getName();
    private static Logger logger = Logger.getLogger(sourceClass);

    @Override
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
        throws IOException, ServletException
    {
        String token = request.getParameter("token");
        String password = request.getParameter("password");
        String passwordConfirmation = request.getParameter("repeat");

        if (token == null) {
            error("Internal error", request, response);

            return;
        }

        if (Util.trim(password) == null) {
            error("Missing password", request, response);

            return;
        }

        if (!password.equals(passwordConfirmation)) {
            error("Passwords do not match", request, response);

            return;
        }

        try {
            Database db = new Database();

            Config config = Config.instance();

            String URL = config.getProperty("url");

            Integer uid = db.findToken(token);
            if (uid == null) {
                request.setAttribute("caption", "Password Update");
                request.setAttribute("message",
                                     "You have already used this token. Please, request a new one.");
                request.setAttribute("destination", URL + "reset_password.jsp");
                request.getRequestDispatcher("message.jsp").forward(request, response);

                return;
            }

            db.updatePassword(uid, password);
            db.deleteToken(token);

            request.setAttribute("caption", "Password Update");
            request.setAttribute("message",
                                 "Your password has been changed. " + 
                                 "You can now use your new password to log in to the Food4Bees website.");
            request.setAttribute("destination", URL + "login.jsp");
            request.getRequestDispatcher("message.jsp").forward(request, response);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Received a database exception.", e);

            error("Internal error", request, response);

            return;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Received an unexpected exception.", e);

            error("Internal error", request, response);

            return;
        }
    }

    private void preserveParameters(HttpServletRequest request)
    {
        request.setAttribute("password", request.getParameter("password"));
        request.setAttribute("repeat", request.getParameter("repeat"));
    }

    private void error(String error, HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        preserveParameters(request);

        request.setAttribute("error", error);
        request.getRequestDispatcher("new_password.jsp").forward(request, response);
    }
}
