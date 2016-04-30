package nl.food4bees.backend.plant;

// Java
import java.io.IOException;

import java.sql.SQLException;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// Servlets
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class EntriesTag extends SimpleTagSupport
{
    static private String sourceClass = EntriesTag.class.getName();
    static private Logger logger = Logger.getLogger(sourceClass);

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

            List<Entry> list = new Database().list();

            request.setAttribute("plants", list);
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
