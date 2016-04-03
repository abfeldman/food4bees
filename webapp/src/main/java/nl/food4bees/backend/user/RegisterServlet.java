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
 * A user registration servlet that uses POST.
 *
 * @author Alexander Feldman
 */

public class RegisterServlet extends HttpServlet
{
    private static String sourceClass = RegisterServlet.class.getName();
    private static Logger logger = Logger.getLogger(sourceClass);

    @Override
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
        throws IOException, ServletException
    {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String passwordConfirmation = request.getParameter("repeat");

        if (Util.trim(name) == null) {
            error("Missing name", request, response);

            return;
        }

        if (Util.trim(email) == null) {
            error("Missing email", request, response);

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

        Integer uid = null;
        try {
            Database db = new Database();

            if (db.hasUser(email)) {
                error("This email is already registered", request, response);

                return;
            }

            Entry user = new Entry(null, name, email, password, new nl.food4bees.backend.group.Database().getId("User"));
            String verificationToken = TokenGenerator.getToken();

            db.startTransaction();
            try {
                Integer id = db.add(user);
                db.addVerificationToken(id, verificationToken);

                db.commit();
            } catch (Exception e) {
                db.rollback();

                throw e;
            } finally {
                db.endTransaction();
            }

            Map<String, String> params = new HashMap<String, String>();

            Config config = Config.instance();

            String secureURL = config.getProperty("secure_url");

            params.put("url", secureURL + "verify-registration?token=" + verificationToken);

            Mailer.instance().sendMail("validate_email",
                                       email,
                                       name,
                                       "webmaster@food4bees.nl",
                                       "The Food4Bees Web Administrator",
                                       params);

            request.setAttribute("caption", "Registration Completed Successfully");
            request.setAttribute("message",
                                 "Thank you for registering. " + 
                                 "In order to verify your identity, we have sent you an email.");
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
        request.setAttribute("name", request.getParameter("name"));
        request.setAttribute("email", request.getParameter("email"));
        request.setAttribute("password", request.getParameter("password"));
        request.setAttribute("repeat", request.getParameter("repeat"));
    }

    private void error(String error, HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        preserveParameters(request);

        request.setAttribute("error", error);
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }
}
