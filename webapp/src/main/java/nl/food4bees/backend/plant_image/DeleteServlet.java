package nl.food4bees.backend.plant_image;

import java.io.IOException;

import java.sql.SQLException;

import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.food4bees.backend.Util;

@WebServlet("/delete-plant-image")
public class DeleteServlet extends ImageServlet
{
    private static String sourceClass = DeleteServlet.class.getName();
    private static Logger logger = Logger.getLogger(sourceClass);

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
        throws ServletException, IOException
    {
        String plantIdParameter = request.getParameter("plantid");
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

            request.setAttribute("error", "Internal error");

            request.getRequestDispatcher("manage_plant_images.jsp?plantid=" + plantIdParameter).forward(request, response);

            return;
        }
        if (!checkCredentials(request)) {
            logger.info("Insufficient delete plant credentials from " + request.getRemoteAddr());

            request.setAttribute("error", "Insufficient credentials");

            request.getRequestDispatcher("manage_plant_images.jsp?plantid=" + plantIdParameter).forward(request, response);

            return;
        }

        Integer id = Integer.parseInt(idParameter);

        try {
            new Database().delete(id);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Received a database exception.", e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Received an unexpected exception.", e);
        }

        request.getRequestDispatcher("manage_plant_images.jsp?plantid=" + plantIdParameter).forward(request, response);
    }
}
