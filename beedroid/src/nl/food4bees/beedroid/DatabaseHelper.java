package nl.food4bees.beedroid;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_PATH = "/data/data/nl.food4bees.beedroid/databases/";
    private static final int DB_VERSION = 2;

    private final String mDatabaseName;

    public DatabaseHelper(Context context, String databaseName) {
        super(context, databaseName, null, DB_VERSION);

        mDatabaseName = databaseName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    static public boolean existsDatabase(String databaseName) {
        return new File(DB_PATH + databaseName).exists();
    }

    /**
     * Copies the database from the local assets folder to a data
     * directory so it can be edited.
     */
    static public void copyDatabase(Context context, String databaseName)
        throws IOException {
        new File(DB_PATH).mkdirs();

        InputStream input = context.getAssets().open(databaseName);        
        OutputStream output = new FileOutputStream(DB_PATH + databaseName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }

        output.flush();
        output.close();
        input.close();
    }
}
