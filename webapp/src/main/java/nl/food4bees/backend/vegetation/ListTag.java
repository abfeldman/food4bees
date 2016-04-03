package nl.food4bees.backend.vegetation;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspContext;

import  javax.servlet.http.HttpServletRequest;

import java.io.IOException;

import java.sql.SQLException;

import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ListTag extends SimpleTagSupport
{
    static private String sourceClass = ListTag.class.getName();
    static private Logger logger = Logger.getLogger(sourceClass);

    public void doTag() throws JspException, IOException
    {
        try {
            List<DisplayEntry> list = new Database().getVegetationList();

            PageContext pageContext = (PageContext)getJspContext();
            HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
            request.setAttribute("vegetation", list);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Received a database exception.", e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Received an unexpected exception.", e);
        }
    }
}
