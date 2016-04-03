package nl.food4bees.backend.user;

import java.io.IOException;

import java.sql.SQLException;

import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.food4bees.backend.Util;

public class EditServlet extends UserServlet
{
    static private String sourceClass = EditServlet.class.getName();
    static private Logger logger = Logger.getLogger(sourceClass);

    /**
     * Called when the administrator clicks edit on manage_users.jsp
     * and used to fill-in the form in user.jsp for modifying an
     * existing user.
     *
     * @see HttpServlet#doGet
     */
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
        throws ServletException, IOException
    {
        String idParameter = request.getParameter("id");
        if (idParameter == null || !Util.isInteger(idParameter)) {
            /*
             * This may happen iff the user submits a GET request
             * without an id or with an id parameter that is not an
             * integer. Don't show an error message because,
             * technically this is not an error and most probably a
             * hacking attempt.
             */
            logger.info("Malformed id parameter from " + request.getRemoteAddr());

            request.getRequestDispatcher("user.jsp").forward(request, response);

            return;
        }

        Integer id = Integer.parseInt(idParameter);

        try {
            Database db = new Database();
            Entry user = db.get(id); // Create an object from the database.
            if (user == null) {
                /*
                 * This may happend iff the user submits a GET request
                 * with an invalid id.  Don't show an error message
                 * because, technically this is not an error and most
                 * probably a hacking attempt.
                 */
                logger.info("Requested data of a non-existent user from " + request.getRemoteAddr());

                request.getRequestDispatcher("user.jsp").forward(request, response);

                return;
            }

            setAttributes(request, user);

            request.getRequestDispatcher("user.jsp").forward(request, response);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Received a database exception.", e);

            request.getRequestDispatcher("manage_users.jsp").forward(request, response);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Received an unexpected exception.", e);

            request.getRequestDispatcher("manage_users.jsp").forward(request, response);
        }
    }

    /**
     * Called by the form in user.jsp for editing an existing user.
     *
     * @see HttpServlet#doPost
     */
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
        throws ServletException, IOException
    {
        String idParameter = request.getParameter("id");
        if (idParameter == null || !Util.isInteger(idParameter)) {
            /*
             * This may happen iff the user submits a GET request
             * without an id or with an id parameter that is not an
             * integer. Don't show an error message because,
             * technically this is not an error and most probably a
             * hacking attempt.
             */
            logger.info("Malformed id parameter from " + request.getRemoteAddr());

            request.getRequestDispatcher("user.jsp").forward(request, response);

            return;
        }
        String groupParameter = request.getParameter("group");
        if (groupParameter == null || !Util.isInteger(groupParameter)) {
            logger.info("Malformed group parameter from " + request.getRemoteAddr());

            request.getRequestDispatcher("user.jsp").forward(request, response);

            return;
        }

        Integer id = Integer.parseInt(idParameter);
        Integer group = Integer.parseInt(groupParameter);

        assert(id != null);
        assert(group != null);

        String error = validate(request);
        if (error != null) {
            request.setAttribute("error", error);
            preserveParameters(request); // We don't want the user to reenter what was already entered.

            request.getRequestDispatcher("user.jsp").forward(request, response);

            return;
        }

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            Entry user = new Entry(id, name, email, password, group);

            new Database().edit(user);

            // Success.

            request.getRequestDispatcher("manage_users.jsp").forward(request, response);
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

    private void setAttributes(HttpServletRequest request, Entry user)
    {
        request.setAttribute("id", user.getId());
        request.setAttribute("name", user.getName());
        request.setAttribute("email", user.getEmail());
        request.setAttribute("password", user.getPassword());
        request.setAttribute("confirmation", user.getPassword());
        request.setAttribute("group", user.getGroup());
    }
}
