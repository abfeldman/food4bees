package nl.food4bees.backend.user;

import java.io.IOException;

import java.sql.SQLException;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.food4bees.backend.Util;

public class AddServlet extends UserServlet
{
    static private String sourceClass = AddServlet.class.getName();
    static private Logger logger = Logger.getLogger(sourceClass);

    /**
     * Called by the form in user.jsp for adding a new user.
     *
     * @see HttpServlet#doPost
     */
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
        throws ServletException, IOException
    {
        if (!checkCredentials(request)) {
            logger.info("Insufficient add user credentials from " + request.getRemoteAddr());

            request.getRequestDispatcher("user.jsp").forward(request, response);

            return;
        }

        String groupParameter = request.getParameter("group");
        if (groupParameter == null || !Util.isInteger(groupParameter)) {
            logger.info("Malformed group parameter from " + request.getRemoteAddr());

            request.getRequestDispatcher("user.jsp").forward(request, response);

            return;
        }

        Integer group = Integer.parseInt(groupParameter);

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Entry user = new Entry(null, name, email, password, group);

        String error = validate(request);
        if (error != null) {
            request.setAttribute("error", error);
            preserveParameters(request); // We don't want the user to reenter what was already entered.

            request.getRequestDispatcher("user.jsp").forward(request, response);

            return;
        }

        try {
            new Database().add(user);

            // Success

            request.getRequestDispatcher("index.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Internal error");
            preserveParameters(request);

            logger.log(Level.SEVERE, "Received a database exception.", e);

            request.getRequestDispatcher("user.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Internal error");
            preserveParameters(request);

            logger.log(Level.SEVERE, "Received an unexpected exception.", e);

            request.getRequestDispatcher("user.jsp").forward(request, response);
        }
    }
}
