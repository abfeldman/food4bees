package nl.food4bees.backend.plant_image;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.food4bees.backend.Util;

import org.apache.commons.fileupload.FileUploadException;

public class AddServlet extends ImageServlet
{
    static private String sourceClass = AddServlet.class.getName();
    static private Logger logger = Logger.getLogger(sourceClass);

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
                                                       throws ServletException, IOException
    {
        try {
            processRequest(request);
        } catch (FileUploadException e) {
            request.setAttribute("error", "Internal error");
            preserveParameters(request);

            logger.log(Level.SEVERE, "Received a request parsing exception.", e);

            request.getRequestDispatcher("plant_image.jsp").forward(request, response);
        }

        String error = validate(request, true);
        if (error != null) {
            request.setAttribute("error", error);
            preserveParameters(request);

            request.getRequestDispatcher("plant_image.jsp").forward(request, response);

            return;
        }

        try {
            new Database().add(Util.parseInteger(plantId),
                               Util.trim(caption),
                               image);

            // Success

            request.getRequestDispatcher("manage_plants.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Internal error");
            preserveParameters(request);

            logger.log(Level.SEVERE, "Received a database exception.", e);

            request.getRequestDispatcher("plant_image.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Internal error");
            preserveParameters(request);

            logger.log(Level.SEVERE, "Received an unexpected exception.", e);

            request.getRequestDispatcher("plant_image.jsp").forward(request, response);
        }
    }
}
