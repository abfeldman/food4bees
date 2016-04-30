package nl.food4bees.backend.user;

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

    /**
     * Creates a list of all users. This list contains all user
     * objects and is used in the manage_users.jsp. The result is
     * stored in the list request attribute. In case of an error this
     * attribute will not be defined and manage_users.jsp should
     * display an internal error.
     *
     * @see SimpleTagSupport#doTag()
     */
    public void doTag() throws JspException, IOException
    {
        try {
            PageContext pageContext = (PageContext)getJspContext();
            HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();

            if (!checkCredentials(request)) {
                logger.info("Insufficient list users credentials from " + request.getRemoteAddr());

                request.setAttribute("error", "Insufficient credentials");
                
                return;
            }

            List<DisplayEntry> list = new Database().list();

            request.setAttribute("users", list);
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
        if (!"Administrator".equals(groupName)) {
            return false;
        }

        return true;
    }
}
