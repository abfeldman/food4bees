package nl.food4bees.backend.user;

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

import nl.food4bees.backend.Util;

/**
 * A user authentication servlet that uses POST.
 *
 * @author Alexander Feldman
 */

public class AuthenticateServlet extends HttpServlet
{
    private static String sourceClass = AuthenticateServlet.class.getName();
    private static Logger logger = Logger.getLogger(sourceClass);

    @Override
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
        throws IOException, ServletException
    {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (Util.trim(email) == null) {
            error("Missing email", request, response);

            return;
        }

        if (Util.trim(password) == null) {
            error("Missing password", request, response);

            return;
        }

        LoginCredentials loginCredentials;
        try {
            Database database = new Database();

            loginCredentials = database.getLoginCredentials(email, password);
            if (loginCredentials == null) {
                error("Bad credentials", request, response);

                return;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Received a database exception.", e);

            error("Internal error", request, response);

            return;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Received an unexpected exception.", e);

            error("Internal error", request, response);

            return;
        }

        assert loginCredentials != null;

        Integer uid = loginCredentials.getUserId();
        String groupName = loginCredentials.getGroupName();

        assert uid != null;

        HttpSession session = request.getSession(true);

        session.setAttribute("uid", uid);
        session.setAttribute("group_name", groupName);

        boolean beedroid = request.getParameter("beedroid") != null;
        if (beedroid) {
            PrintWriter out = response.getWriter();

            out.println("Success");
        } else {
            String ref = request.getParameter("ref");
            if (ref != null) {
                response.sendRedirect(ref);
            } else {
                response.sendRedirect("index.jsp");
            }
        }
    }

    private void preserveParameters(HttpServletRequest request)
    {
        request.setAttribute("ref", request.getParameter("ref"));
        request.setAttribute("email", request.getParameter("email"));
        request.setAttribute("password", request.getParameter("password"));
    }

    private void error(String error, HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        boolean beedroid = request.getParameter("beedroid") != null;

        if (beedroid) {
            PrintWriter out = response.getWriter();

            out.println(error);
        } else {
            preserveParameters(request);

            request.setAttribute("error", error);
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
