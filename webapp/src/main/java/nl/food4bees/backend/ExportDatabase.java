package nl.food4bees.backend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.nio.file.Files;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.List;

import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

import nl.food4bees.backend.plant.Entry;
import nl.food4bees.backend.plant.Database;

public class ExportDatabase
{
    static final private String SQLITE_DB_JDBC = "jdbc:sqlite:";

    /**
     * Writes a zipped file to the response output stream with content:
     * 
     * plant.zip
     *  |- plant.db
     *  |- images
     *      |- id_000001.jpg
     *      |- id_000002.jpg
     *      |- ...
     * 
     * And sets the content type and headers.
     */
    public File makeArchive(File dbFile, File zipFile)
        throws ClassNotFoundException, IOException, SQLException
    {
        createSQLiteDatabase(SQLITE_DB_JDBC + dbFile.getCanonicalPath(), null);

        writeToZip(dbFile, zipFile);

        return zipFile;
    }

    public File makeDatabase(File dbFile, List<Integer> plants)
        throws ClassNotFoundException, IOException, SQLException
    {
        createSQLiteDatabase(SQLITE_DB_JDBC + dbFile.getCanonicalPath(), plants);

        return dbFile;
    }
    
    /**
     * Creates database file.
     * 
     * @param The URL of the sqlite connection string. 
     * @throws ClassNotFoundException
     * @throws IOException
     * @throws SQLException
     */
    private void createSQLiteDatabase(String url, List<Integer> plants)
        throws ClassNotFoundException, IOException, SQLException
    {
        // Get an sqlite connection and create tables.
        Class.forName(org.sqlite.JDBC.class.getName());
        Connection connectionSQLite = DriverManager.getConnection(url);
 
        Statement statementCreate = connectionSQLite.createStatement();
        String queryCreate =  "CREATE TABLE android_metadata (locale TEXT DEFAULT 'en_US')"; 
        statementCreate.executeUpdate(queryCreate);
        statementCreate.close();
        statementCreate = connectionSQLite.createStatement();
        queryCreate = "CREATE TABLE plant (" +
                      "_id INTEGER PRIMARY KEY NOT NULL, " +
                      "common_name VARCHAR(256), " +
                      "latin_name VARCHAR(256) NOT NULL, " +
                      "description VARCHAR(4096), " +
                      "version INTEGER NOT NULL" +
                      ")"; 
        statementCreate.executeUpdate(queryCreate);
        statementCreate.close();
        statementCreate = connectionSQLite.createStatement();
        queryCreate = "CREATE TABLE plant_image (" +
                      "_id INTEGER PRIMARY KEY NOT NULL, " +
                      "plant_id INTEGER, " +
                      "caption VARCHAR(256) NOT NULL" +
                      ")";
        statementCreate.executeUpdate(queryCreate);
        statementCreate.close();
        statementCreate = connectionSQLite.createStatement();

        queryCreate = "INSERT INTO android_metadata VALUES ('en_US')";

        statementCreate.executeUpdate(queryCreate);
        statementCreate.close();

        // Read all plants from backend and insert into sqlite.
        String queryInsert = "INSERT INTO plant (_id, common_name, latin_name, description, version) values (?, ?, ?, ?, ?)";
        PreparedStatement statementInsert = connectionSQLite.prepareStatement(queryInsert);

        Database db = new Database();
        for (Integer id : plants) {
            Entry entry = db.get(id);

            assert(entry != null);

            int i = 1;
            statementInsert.setString(i++, id.toString());
            statementInsert.setString(i++, entry.getCommonName());
            statementInsert.setString(i++, entry.getScientificName());
            statementInsert.setString(i++, entry.getDescription());
            statementInsert.setInt(i++, entry.getVersion());
            statementInsert.executeUpdate();
        }
/*
        // Select all plant images from postgres and insert into sqlite.
        querySelect = "SELECT id, plant_id, caption FROM plant_image";        
        statementSelect = connectionPostgres.createStatement();
        resultSet = statementSelect.executeQuery(querySelect);
        
        // Insert the postgres plants in sqlite.
        while(resultSet.next()) {
            String queryInsert = "INSERT INTO plant_image (_id, plant_id, caption) VALUES (?, ?, ?)";
            PreparedStatement statementInsert = connectionSQLite.prepareStatement(queryInsert);
            int i = 1;
            statementInsert.setString(i++, resultSet.getString("id"));
            statementInsert.setString(i++, resultSet.getString("plant_id"));
            statementInsert.setString(i++, resultSet.getString("caption"));
            statementInsert.executeUpdate();
            statementInsert.close();
        }
        resultSet.close();
*/
        connectionSQLite.close();        
    }
    
    /**
     * Writes the sqlite database to zip file and also the images from the database.
     * 
     * @param dbFile The existing database.
     * @param zipFile Zip file which will be overwritten.
     * @throws IOException
     * @throws SQLException
     */
    private void writeToZip(File dbFile, File zipFile) throws IOException, SQLException
    {
        // Copy the sqlite database file to the zip file.
        FileOutputStream fos = new FileOutputStream(zipFile);
        ZipOutputStream zos = new ZipOutputStream(fos);
        FileInputStream dbFis = new FileInputStream(dbFile);        
        ZipEntry zipEntry = new ZipEntry(dbFile.getName());        
        zos.putNextEntry(zipEntry);        
        IOUtils.copy(dbFis, zos);
        dbFis.close();
        zos.closeEntry();

        // Add all images to the zip file.
        Connection connectionPostgres = ConnectionFactory.instance().getConnection();

        String querySelect = "SELECT id, image FROM plant_image";        
        Statement statementSelect = connectionPostgres.createStatement();
        ResultSet resultSet = statementSelect.executeQuery(querySelect);
        
        while (resultSet.next()) {
            Integer id = resultSet.getInt("id");
            byte[] image = resultSet.getBytes("image");
            zipEntry = new ZipEntry(String.format("images/id_%06d.jpg", id));
            zipEntry.setSize(image.length);
            zos.putNextEntry(zipEntry);
            zos.write(image);
            zos.closeEntry();
        }
        
        zos.close();
    }
}
