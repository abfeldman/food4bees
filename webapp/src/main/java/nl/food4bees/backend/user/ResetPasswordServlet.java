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
import nl.food4bees.backend.Mailer;
import nl.food4bees.backend.TokenGenerator;

/**
 * A user password reminder servlet that uses POST.
 *
 * @author Alexander Feldman
 */

public class ResetPasswordServlet extends HttpServlet
{
    private static String sourceClass = RegisterServlet.class.getName();
    private static Logger logger = Logger.getLogger(sourceClass);

    @Override
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
        throws IOException, ServletException
    {
        String email = request.getParameter("email");

        if (Util.trim(email) == null) {
            error("Missing email", request, response);

            return;
        }

        try {
            Database db = new Database();

            Integer id = null;
            if ((id = db.getId(email)) == null) {
                error("Invalid email address", request, response);

                return;
            }

            String name = db.get(id).getName();

            assert(name != null);

            String verificationToken = TokenGenerator.getToken();
            db.addVerificationToken(id, verificationToken);

            Map<String, String> params = new HashMap<String, String>();

            Config config = Config.instance();

            String secureURL = config.getProperty("secure_url");

            params.put("url", secureURL + "new_password.jsp?token=" + verificationToken);

            Mailer.instance().sendMail("reset_password",
                                       email,
                                       name,
                                       "webmaster@food4bees.nl",
                                       "The Food4Bees Web Administrator",
                                       params);

            request.setAttribute("caption", "Password Reset");
            request.setAttribute("message",
                                 "We have sent you an email with a link for resetting your password. " +
                                 "To change your password first click on this link.");
            request.setAttribute("destination", "login.jsp");
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
        request.setAttribute("email", request.getParameter("email"));
    }

    private void error(String error, HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        preserveParameters(request);

        request.setAttribute("error", error);
        request.getRequestDispatcher("reset_password.jsp").forward(request, response);
    }
}
