package nl.food4bees.backend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import java.nio.file.Files;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.ArrayList;
import java.util.Map;
import java.util.Iterator;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class SyncDatabaseServlet extends HttpServlet
{
    static final private String FILE_NAME_DB = "plant.db";

    private static String sourceClass = SyncDatabaseServlet.class.getName();
    private static Logger logger = Logger.getLogger(sourceClass);

    @Override
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
        throws IOException, ServletException
    {
        String[] plantIds = request.getParameterValues("id");
        String[] versions = request.getParameterValues("version");

        try {
            if (plantIds.length != versions.length) {
                // @todo: Produce an internal error.

                return;
            }

            Hashtable<Integer, Integer> remoteVersions = new Hashtable<Integer, Integer>();

            int j = plantIds.length;
            for (int i = 0; i < j; i++) {
                remoteVersions.put(Integer.parseInt(plantIds[i]),
                                   Integer.parseInt(versions[i]));
            }

            nl.food4bees.backend.plant.Database db = new nl.food4bees.backend.plant.Database();

            Hashtable<Integer, Integer> localVersions = db.getVersions();

            ArrayList<Integer> plants = new ArrayList<Integer>();

            Iterator it = localVersions.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry kv = (Map.Entry)it.next();

                Integer id = (Integer)kv.getKey();

                Integer localVersion = (Integer)kv.getValue();
                Integer remoteVersion = remoteVersions.get(id);

                if (remoteVersion == null || remoteVersion < localVersion) {
                    plants.add(id);
                }

                it.remove(); // avoids a ConcurrentModificationException
            }

            System.err.println("@@@: " + plants);

            ExportDatabase exportDatabase = new ExportDatabase();

            File tempDir = Files.createTempDirectory(null).toFile();
            File dbFile = new File(tempDir, FILE_NAME_DB);

            exportDatabase.makeDatabase(dbFile, plants);

            int fileLength = (int)dbFile.length();

            System.err.println(fileLength);

            response.setContentLength(fileLength);
            response.setHeader("Content-Disposition",
                               String.format("attachment; filename=%s", FILE_NAME_DB));
            
            FileInputStream in = new FileInputStream(dbFile);
            OutputStream out = response.getOutputStream();
            
            int bodyLength = IOUtils.copy(in, out);            
            assert bodyLength == fileLength;

            FileUtils.cleanDirectory(tempDir);
            Files.delete(tempDir.toPath());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not create SQLite database.", e);
            PrintWriter out = response.getWriter();
            out.println("Internal error");
        }
    }
}
