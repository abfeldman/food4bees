package nl.food4bees.beedroid;

import java.util.ArrayList;

import android.content.Context;
import android.content.ContentValues;

import android.database.sqlite.SQLiteDatabase;

import android.database.Cursor;
import android.database.SQLException;

public class PlantsDatabase {
    private static final String TABLE_PLANT = "plant";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_COMMON_NAME = "common_name";
    private static final String COLUMN_SCIENTIFIC_NAME = "latin_name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_VERSION = "version";

    private static final String TABLE_PLANT_IMAGE = "plant_image";
    private static final String COLUMN_PLANT_ID = "plant_id";
    private static final String COLUMN_CAPTION = "caption";

    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;

    public PlantsDatabase(Context context, String dbName) {
        mDatabaseHelper = new DatabaseHelper(context, dbName);
    }

    public void open() {
        try {
            mDatabase = mDatabaseHelper.getWritableDatabase();
        } catch (SQLException e) {
            throw new Error("Unable to open the plants database.");
        }
    }

    public void close() {
        mDatabaseHelper.close();
    }

    private String[] mPlantCommonNameColumns = { COLUMN_ID,
                                                 COLUMN_COMMON_NAME };

    public ArrayList<PlantCommonName> getPlantsCommonNames() {
        ArrayList<PlantCommonName> result = new ArrayList<PlantCommonName>();

        Cursor cursor = mDatabase.query(TABLE_PLANT,
                                        mPlantCommonNameColumns,
                                        null,
                                        null,
                                        null,
                                        null,
                                        COLUMN_COMMON_NAME);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Integer id = cursor.getInt(0);
            String commonName = cursor.getString(1);

            result.add(new PlantCommonName(id, commonName));

            cursor.moveToNext();
        }
        cursor.close();

        return result;
    }

    private String[] mPlantVersionColumns = { COLUMN_ID,
                                              COLUMN_VERSION };

    public ArrayList<PlantVersion> getPlantsVersions() {
        ArrayList<PlantVersion> result = new ArrayList<PlantVersion>();

        Cursor cursor = mDatabase.query(TABLE_PLANT,
                                        mPlantVersionColumns,
                                        null,
                                        null,
                                        null,
                                        null,
                                        COLUMN_VERSION);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Integer id = cursor.getInt(0);
            Integer version = cursor.getInt(1);

            result.add(new PlantVersion(id, version));

            cursor.moveToNext();
        }
        cursor.close();

        return result;
    }

    private String[] mPlantColumns = { COLUMN_ID,
                                       COLUMN_COMMON_NAME,
                                       COLUMN_SCIENTIFIC_NAME,
                                       COLUMN_DESCRIPTION,
                                       COLUMN_VERSION };

    public Plant getPlant(Integer id) {
        Cursor cursor = mDatabase.query(TABLE_PLANT,
                                        mPlantColumns,
                                        COLUMN_ID + " = " + id,
                                        null,
                                        null,
                                        null,
                                        null);

        cursor.moveToFirst();
        assert(!cursor.isAfterLast());

        String commonName = cursor.getString(1);
        String scientificName = cursor.getString(2);
        String description = cursor.getString(3);
        Integer version = cursor.getInt(4);

        cursor.moveToNext();
        assert(cursor.isAfterLast());

        cursor.close();

        return new Plant(id, commonName, scientificName, description, version);
    }

    public void addPlant(Plant plant) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_ID, plant.getId());
        values.put(COLUMN_COMMON_NAME, plant.getCommonName());
        values.put(COLUMN_SCIENTIFIC_NAME, plant.getScientificName());
        values.put(COLUMN_DESCRIPTION, plant.getDescription());
        values.put(COLUMN_VERSION, plant.getVersion());
 
        mDatabase.insert(TABLE_PLANT, null, values);
    }

    public void deletePlant(Integer id) {
        mDatabase.delete(TABLE_PLANT, COLUMN_ID + " = ?", new String[] { id.toString() });
    }

    private String[] mPlantImageColumns = { COLUMN_ID,
                                            COLUMN_PLANT_ID,
                                            COLUMN_CAPTION };
}
