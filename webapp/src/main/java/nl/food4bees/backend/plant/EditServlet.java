package nl.food4bees.backend.plant;

import java.io.IOException;

import java.sql.SQLException;

import java.util.Calendar;
import java.util.Date;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.food4bees.backend.Util;

public class EditServlet extends PlantServlet
{
    static private String sourceClass = EditServlet.class.getName();
    static private Logger logger = Logger.getLogger(sourceClass);

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
        throws ServletException, IOException
    {
        String idParameter = request.getParameter("id");
        if (idParameter == null || !Util.isInteger(idParameter)) {
            logger.info("Malformed id parameter from " + request.getRemoteAddr());

            request.getRequestDispatcher("plant.jsp").forward(request, response);

            return;
        }

        Integer id = Integer.parseInt(idParameter);

        try {
            Entry plant = new Database().get(id); // Create an object from the database.
            if (plant == null) {
                logger.info("Requested data of a non-existent plant from " + request.getRemoteAddr());

                request.getRequestDispatcher("plant.jsp").forward(request, response);

                return;
            }

            setAttributes(request, plant);

            request.getRequestDispatcher("plant.jsp").forward(request, response);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Received a database exception.", e);

            request.getRequestDispatcher("manage_plants.jsp").forward(request, response);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Received an unexpected exception.", e);

            request.getRequestDispatcher("manage_plants.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
        throws ServletException, IOException
    {
        String idParameter = request.getParameter("id");
        if (idParameter == null || !Util.isInteger(idParameter)) {
            logger.info("Malformed id parameter from " + request.getRemoteAddr());

            request.getRequestDispatcher("plant.jsp").forward(request, response);

            return;
        }

        Integer id = Integer.parseInt(idParameter);

        String error = validate(request);
        if (error != null) {
            request.setAttribute("error", error);
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

        try {
            Database db = new Database();

            Integer version = db.getVersion(id);
            System.err.println(version);

            assert(version != null);

            Entry plant = new Entry(id,
                                    1, // @todo: Fix hardcoded value
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
                                    version + 1);

            db.edit(plant);

            // Success.

            request.getRequestDispatcher("manage_plants.jsp").forward(request, response);
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

    private void setAttributes(HttpServletRequest request, Entry plant)
    {
        request.setAttribute("id", plant.getId());
        request.setAttribute("commonName", plant.getCommonName());
        request.setAttribute("scientificName", plant.getScientificName());
        request.setAttribute("description", plant.getDescription());
        request.setAttribute("url", plant.getUrl());
        if (plant.getColor() != null) {
            request.setAttribute("color", Integer.toHexString(plant.getColor()));
        }
        request.setAttribute("height", plant.getHeight());
        request.setAttribute("nectar", plant.getNectar());
        request.setAttribute("pollen", plant.getPollen());
        request.setAttribute("start", formatDate(plant.getStart()));
        request.setAttribute("end", formatDate(plant.getEnd()));
    }

    public String formatDate(Date date)
    {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return String.format("%02d-%02d", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1);
    }
}
