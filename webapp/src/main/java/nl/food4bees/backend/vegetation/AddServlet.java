package nl.food4bees.backend.vegetation;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.SQLException;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nl.food4bees.backend.Point;
import nl.food4bees.backend.Polygon;

/**
 * A servlet that adds vegetation to the database via a POST HTTP request.
 *
 * @author Alexander Feldman
 */

public class AddServlet extends HttpServlet
{
    private static String sourceClass = AddServlet.class.getName();
    private static Logger logger = Logger.getLogger(sourceClass);

    @Override
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
        throws IOException, ServletException
    {
        HttpSession session = request.getSession(false);

        Integer user = (Integer)session.getAttribute("uid");

        String plant = request.getParameter("plant");
        String amount = request.getParameter("amount");

        String[] x = request.getParameterValues("x");
        String[] y = request.getParameterValues("y");

        PrintWriter out = response.getWriter();

        if (user == null ||
            plant == null ||
            amount == null) {
            out.println("Failure");

            return;
        }
        // @todo: validate user, plant, and amount types

        if (x == null || y == null || x.length != y.length) {
            out.println("Failure");

            return;
        }

        Polygon polygon = new Polygon();

        int j = x.length;
        for (int i = 0; i < j; i++) {
            polygon.addPoint(new Point(Double.parseDouble(x[i]),
                                       Double.parseDouble(y[i])));
        }

        try {
            new Database().addVegetation(user,
                                         Integer.parseInt(plant),
                                         polygon,
                                         Double.parseDouble(amount));
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Received a database exception.", e);

            out.println("Failure");

            return;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Received an unexpected exception.", e);

            out.println("Failure");

            return;
        }

        out.println("Success");
    }
}
