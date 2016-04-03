package nl.food4bees.backend.plant_image;

import java.util.List;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import nl.food4bees.backend.Util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public abstract class ImageServlet extends HttpServlet
{
    protected String id = null;
    protected String plantId = null;
    protected String caption = null;
    protected byte[] image = null;

    protected void processRequest(HttpServletRequest request) throws FileUploadException
    {
        List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
        for (FileItem item : items) {
            if (!item.isFormField()) {
                image = item.get();
            } else {
                String name = item.getFieldName();
                String value = item.getString();
                if (name.equals("id")) {
                    id = value;
                } else if (name.equals("plantid")) {
                    plantId = value;
                } else if (name.equals("caption")) {
                    caption = value;
                }
            }
        }
    }

    protected String validate(HttpServletRequest request, boolean withFile) throws IllegalStateException, IOException, ServletException
    {        
        if (Util.trim(caption) == null) {
            return "Missing caption";
        }

        if (withFile) {
            if (image == null || image.length == 0) {
                return "Missing image";
            }
        }
                
        return null;
    }

    protected void preserveParameters(HttpServletRequest request)
    {
        request.setAttribute("id", id);
        request.setAttribute("plantid", plantId);
        request.setAttribute("caption", caption);
    }
}
