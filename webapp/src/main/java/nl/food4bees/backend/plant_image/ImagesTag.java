package nl.food4bees.backend.plant_image;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import nl.food4bees.backend.plant_image.Database;
import nl.food4bees.backend.plant_image.Entry;

public class ImagesTag extends SimpleTagSupport
{
    static private String sourceClass = SimpleTagSupport.class.getName();
    static private Logger logger = Logger.getLogger(sourceClass);

    private Integer plantId;

    public void setPlant(String plant)
    {
        try {
            plantId = Integer.parseInt(plant);
        } catch (NumberFormatException nfe) {
            plantId = null;
        }
    }

    public void doTag() throws JspException, IOException
    {
        try {
            PageContext pageContext = (PageContext)getJspContext();
            HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();

            if (!checkCredentials(request)) {
                logger.info("Insufficient list plants credentials from " + request.getRemoteAddr());

                request.setAttribute("error", "Insufficient credentials");
                
                return;
            }

            List<Entry> list = new Database().getList(plantId);

            request.setAttribute("plant_images", list);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Received a database exception.", e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Received an unexpected exception.", e);
        }
    }

    private boolean checkCredentials(HttpServletRequest request)
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
