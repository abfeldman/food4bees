package nl.food4bees.backend.plant;

import java.io.IOException;
import java.io.InputStream;

import java.sql.SQLException;

import java.util.Date;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;

import nl.food4bees.backend.Util;

public class AddServlet extends PlantServlet
{
    static private String sourceClass = AddServlet.class.getName();
    static private Logger logger = Logger.getLogger(sourceClass);

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
        throws ServletException, IOException
    {
        if (!checkCredentials(request)) {
            logger.info("Insufficient list plants credentials from " + request.getRemoteAddr());

            request.setAttribute("error", "Internal error");
            preserveParameters(request);

            request.getRequestDispatcher("plant.jsp").forward(request, response);

            return;
        }
        
        String error = validate(request);
        if (error != null) {
            request.setAttribute("error", error);
            preserveParameters(request);

            request.getRequestDispatcher("plant.jsp").forward(request, response);

            return;
        }

        HttpSession session = request.getSession(false);

        Object attribute = session.getAttribute("uid");
        if (attribute == null) {
            request.setAttribute("error", "Internal error");
            preserveParameters(request);

            request.getRequestDispatcher("plant.jsp").forward(request, response);

            return;
        }
        Integer uid = (Integer)attribute;
        if (uid == null) {
            request.setAttribute("error", "Internal error");
            preserveParameters(request);

            request.getRequestDispatcher("plant.jsp").forward(request, response);

            return;
        }
        
        String commonName = request.getParameter("commonName");
        String scientificName = request.getParameter("scientificName");
        String description = request.getParameter("description");
        String url = request.getParameter("url");
        String color = request.getParameter("color");
        String height = request.getParameter("height");
        String nectar = request.getParameter("nectar");
        String pollen = request.getParameter("pollen");
        String start = request.getParameter("start");
        String end = request.getParameter("end");
        String caption = request.getParameter("caption");

        try {
            Entry plant = new Entry(null,
                                    uid,
                                    commonName,
                                    scientificName,
                                    description,
                                    url,
                                    color,
                                    height,
                                    nectar,
                                    pollen,
                                    start,
                                    end,
                                    0);

            new Database().add(plant);

            request.getRequestDispatcher("index.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Internal error");
            preserveParameters(request);

            logger.log(Level.SEVERE, "Received a database exception.", e);

            request.getRequestDispatcher("plant.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Internal error");
            preserveParameters(request);

            logger.log(Level.SEVERE, "Received an unexpected exception.", e);

            request.getRequestDispatcher("plant.jsp").forward(request, response);
        }
    }
}
