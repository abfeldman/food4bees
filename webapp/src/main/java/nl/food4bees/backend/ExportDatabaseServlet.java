package nl.food4bees.backend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import java.nio.file.Files;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * Temporarily creates and return a zip
 */
public class ExportDatabaseServlet extends HttpServlet
{
    static final private String FILE_NAME_DB = "plant.db";
    static final private String FILE_NAME_ZIP = "plant.zip";

    static private String sourceClass = ExportDatabaseServlet.class.getName();
    static private Logger logger = Logger.getLogger(sourceClass);

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        try {
            File tempDir = Files.createTempDirectory(null).toFile();
            File dbFile = new File(tempDir, FILE_NAME_DB);

            ExportDatabase exportDatabase = new ExportDatabase();
            File zipFile = new File(tempDir, FILE_NAME_ZIP);

            exportDatabase.makeArchive(dbFile, zipFile);

            int fileLength = (int)zipFile.length();

            response.setContentType("application/zip");
            response.setContentLength(fileLength);
            response.setHeader("Content-Disposition", String.format("attachment; filename=%s", FILE_NAME_ZIP));
            
            FileInputStream in = new FileInputStream(zipFile);
            OutputStream out = response.getOutputStream();
            
            int bodyLength = IOUtils.copy(in, out);            
            assert bodyLength == fileLength;
            
            in.close();
            out.close();

            FileUtils.cleanDirectory(tempDir);
            Files.delete(tempDir.toPath());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not create SQLite database.", e);
            PrintWriter out = response.getWriter();
            out.println("Internal error");
        }
    }
}
