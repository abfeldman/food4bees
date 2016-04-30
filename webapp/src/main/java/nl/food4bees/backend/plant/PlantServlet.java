package nl.food4bees.backend.plant;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

import java.util.regex.Pattern;

import nl.food4bees.backend.Util;

class PlantServlet extends HttpServlet
{
    private final Pattern DATE_PATTERN = Pattern.compile("[0-9]{1,2}-[0-9]{1,2}");

    protected String validate(HttpServletRequest request)
    {
        String commonName = Util.trim(request.getParameter("commonName"));
        String scientificName = Util.trim(request.getParameter("scientificName"));
        String description = Util.trim(request.getParameter("description"));
        String url = Util.trim(request.getParameter("url"));
        String color = Util.trim(request.getParameter("color"));
        String height = Util.trim(request.getParameter("height"));
        String nectar = Util.trim(request.getParameter("nectar"));
        String pollen = Util.trim(request.getParameter("pollen"));
        String start = Util.trim(request.getParameter("start"));
        String end = Util.trim(request.getParameter("end"));
        String caption = Util.trim(request.getParameter("caption"));

        if (scientificName == null) {
            return "Missing scientific name";
        }

        if (height != null && !Util.isFloat(height)) {
            return "Invalid height (expected a number)";
        }
        if (nectar != null && !Util.isFloat(nectar)) {
            return "Invalid nectar index (expected a number)";
        }
        if (pollen != null && !Util.isFloat(pollen)) {
            return "Invalid pollen index (expected a number)";
        }
        if (start != null && !DATE_PATTERN.matcher(start).matches()) {
            return "Invalid date for the start of flowering";
        }
        if (end != null && !DATE_PATTERN.matcher(end).matches()) {
            return "Invalid date for the end of flowering";
        }

        return null;
    }

    protected void preserveParameters(HttpServletRequest request)
    {
        request.setAttribute("id", request.getParameter("id"));
        request.setAttribute("commonName", request.getParameter("commonName"));
        request.setAttribute("scientificName", request.getParameter("scientificName"));
        request.setAttribute("description", request.getParameter("description"));
        request.setAttribute("url", request.getParameter("url"));
        request.setAttribute("color", request.getParameter("color"));
        request.setAttribute("height", request.getParameter("height"));
        request.setAttribute("nectar", request.getParameter("nectar"));
        request.setAttribute("pollen", request.getParameter("pollen"));
        request.setAttribute("start", request.getParameter("start"));
        request.setAttribute("end", request.getParameter("end"));
        request.setAttribute("caption", request.getParameter("caption"));
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
        if (!"Administrator".equals(groupName) && !"Editor".equals(groupName)) {
            return false;
        }

        return true;
    }
}
