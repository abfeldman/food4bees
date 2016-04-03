package nl.food4bees.backend.user;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class EmailsTag extends SimpleTagSupport
{
    static private String sourceClass = EmailsTag.class.getName();
    static private Logger logger = Logger.getLogger(sourceClass);

    public void doTag() throws JspException, IOException
    {
        try {
            List<Email> list = new Database().getEmailList();

            PageContext pageContext = (PageContext)getJspContext();
            HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
            request.setAttribute("users", list);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Received a database exception.", e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Received an unexpected exception.", e);
        }
    }
}
