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

public class EditServlet extends ImageServlet
{
    static private String sourceClass = EditServlet.class.getName();
    static private Logger logger = Logger.getLogger(sourceClass);

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
        throws ServletException, IOException
    {
        String idParameter = request.getParameter("id");
        String plantIdParameter = request.getParameter("plantid");

        if (idParameter == null || !Util.isInteger(idParameter) ||
            plantIdParameter == null || !Util.isInteger(plantIdParameter)) {
            request.setAttribute("id", idParameter);
            request.setAttribute("plantid", plantIdParameter);

            logger.info("Malformed id parameter from " + request.getRemoteAddr());

            request.getRequestDispatcher("manage_plants.jsp").forward(request, response);

            return;
        }

        if (!checkCredentials(request)) {
            logger.info("Insufficient edit image credentials from " + request.getRemoteAddr());

            request.setAttribute("error", "Insufficient credentials");
            preserveParameters(request);

            request.getRequestDispatcher("manage_plant_images.jsp?plant_id=" + plantIdParameter).forward(request, response);

            return;
        }
        
        try {
            Entry plantImage = new Database().get(Integer.parseInt(idParameter));
            if (plantImage == null) {
                logger.info("Requested data of a non-existent plant from " + request.getRemoteAddr());

                request.setAttribute("plantid", plantIdParameter);
                request.getRequestDispatcher("manage_plants.jsp").forward(request, response);

                return;
            }

            request.setAttribute("id", plantImage.getId());
            request.setAttribute("plantid", plantImage.getPlantId());
            request.setAttribute("caption", plantImage.getCaption());
            request.setAttribute("size", plantImage.getSize());

            request.getRequestDispatcher("plant_image.jsp").forward(request, response);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Received a database exception.", e);

            request.setAttribute("id", idParameter);
            request.setAttribute("plantid", plantIdParameter);
            request.getRequestDispatcher("manage_plant_images.jsp").forward(request, response);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Received an unexpected exception.", e);

            request.setAttribute("id", idParameter);
            request.setAttribute("plantid", plantIdParameter);
            request.getRequestDispatcher("manage_plant_images.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
        throws ServletException, IOException
    {
        if (!checkCredentials(request)) {
            logger.info("Insufficient edit image credentials from " + request.getRemoteAddr());

            request.setAttribute("error", "Insufficient credentials");
            preserveParameters(request);

            request.getRequestDispatcher("manage_plants.jsp").forward(request, response);

            return;
        }
        
        try {
            processRequest(request);
        } catch (FileUploadException e) {
            request.setAttribute("error", "Internal error");
            preserveParameters(request);

            logger.log(Level.SEVERE, "Received a request parsing exception.", e);

            request.getRequestDispatcher("plant_image.jsp").forward(request, response);
        }

        if (id == null || !Util.isInteger(id) ||
            plantId == null || !Util.isInteger(plantId)) {
            logger.info("Malformed id parameter from " + request.getRemoteAddr());

            preserveParameters(request);
            request.getRequestDispatcher("plant_image.jsp").forward(request, response);

            return;
        }

        String error = validate(request, false);
        
        if (error != null) {
            request.setAttribute("error", error);

            preserveParameters(request);
            request.getRequestDispatcher("plant_image.jsp").forward(request, response);

            return;
        }

        try {
            Entry plantImage = new Entry(Util.parseInteger(id),
                                         Util.parseInteger(plantId),
                                         caption,
                                         image);

            new Database().edit(plantImage);

            // Success
            request.setAttribute("plantid", plantId);

            request.getRequestDispatcher("manage_plant_images.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Internal error");

            logger.log(Level.SEVERE, "Received a database exception.", e);

            preserveParameters(request);
            request.getRequestDispatcher("plant_image.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Internal error");

            logger.log(Level.SEVERE, "Received an unexpected exception.", e);

            preserveParameters(request);
            request.getRequestDispatcher("plant_image.jsp").forward(request, response);
        }
    }
}
