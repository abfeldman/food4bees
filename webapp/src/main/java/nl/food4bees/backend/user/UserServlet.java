package nl.food4bees.backend.user;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

import java.util.regex.Pattern;

import nl.food4bees.backend.Util;

public class UserServlet extends HttpServlet
{
    private final Pattern NAME_PATTERN = Pattern.compile("^[\\p{L} .'-]+$");
    private final Pattern EMAIL_PATTERN = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    protected String validate(HttpServletRequest request)
    {
        String name = Util.trim(request.getParameter("name"));
        String email = Util.trim(request.getParameter("email"));
        String password = Util.trim(request.getParameter("password"));
        String confirmation = Util.trim(request.getParameter("confirmation"));

        if (name == null) {
            return "Missing name";
        }
        if (!NAME_PATTERN.matcher(name).matches()) {
            return "Invalid name";
        }

        if (email == null) {
            return "Missing email";
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return "Invalid email";
        }

        if (password == null) {
            return "Empty password";
        }
        if (password.length() < 2) {
            return "Password is too short";
        }
        if (!password.equals(confirmation)) {
            return "Password confirmation does not match";
        }
        // No password crack checks yet to have our lives simpler during testing

        return null;
    }

    protected void preserveParameters(HttpServletRequest request)
    {
        request.setAttribute("id", request.getParameter("id"));
        request.setAttribute("name", request.getParameter("name"));
        request.setAttribute("email", request.getParameter("email"));
        request.setAttribute("password", request.getParameter("password"));
        request.setAttribute("confirmation", request.getParameter("confirmation"));
        request.setAttribute("group", request.getParameter("group"));
    }

    protected boolean checkCredentials(HttpServletRequest request)
    {
        HttpSession session = request.getSession(false);

        Object attribute = session.getAttribute("group_name");
        if (attribute == null) {
            return false;
        }
        String groupName = (String)attribute;
        if (groupName == null) {
            return false;
        }
        if (!"Administrator".equals(groupName)) {
            return false;
        }

        return true;
    }
}
