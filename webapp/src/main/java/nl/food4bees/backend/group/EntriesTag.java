package nl.food4bees.backend.group;

// Java
import java.io.IOException;

import java.sql.SQLException;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// Servlets
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class EntriesTag extends SimpleTagSupport
{
    static private String sourceClass = EntriesTag.class.getName();
    static private Logger logger = Logger.getLogger(sourceClass);

    /**
     * Creates a list of all groups. This list contains all group
     * objects and is used in user management. The result is stored in
     * the list request attribute. In case of an error this attribute
     * will not be defined and the page having this custom tag should
     * display an internal error.
     *
     * @see SimpleTagSupport#doTag()
     */
    public void doTag() throws JspException, IOException
    {
        try {
            List<Entry> list = new Database().list();

            PageContext pageContext = (PageContext)getJspContext();
            HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();

            request.setAttribute("groups", list);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Received a database exception.", e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Received an unexpected exception.", e);
        }
    }
}
