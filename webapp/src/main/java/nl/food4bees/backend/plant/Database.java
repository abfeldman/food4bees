package nl.food4bees.backend.plant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;

import java.util.ArrayList;
import java.util.List;
import java.util.Hashtable;

import org.apache.commons.lang3.StringUtils;

public class Database extends nl.food4bees.backend.Database
{
    private final String[] cols_ = { "user_id",
                                     "common_name",
                                     "latin_name",
                                     "description",
                                     "wikipedia_url",
                                     "color",
                                     "height",
                                     "nectar_index",
                                     "pollen_index",
                                     "start_flowering",
                                     "end_flowering",
                                     "version" };

    public Database() throws SQLException
    {
    }

    private Entry makeEntry(ResultSet resultSet) throws SQLException
    {
        Entry entry = new Entry(getInteger(resultSet, "id"),
                                getInteger(resultSet, "user_id"),
                                resultSet.getString("common_name"),
                                resultSet.getString("latin_name"),
                                resultSet.getString("description"),
                                resultSet.getString("wikipedia_url"),
                                getInteger(resultSet, "color"),
                                getDouble(resultSet, "height"),
                                getDouble(resultSet, "nectar_index"),
                                getDouble(resultSet, "pollen_index"),
                                resultSet.getDate("start_flowering"),
                                resultSet.getDate("end_flowering"),
                                getInteger(resultSet, "version"));
        return entry;
    }

    public void delete(Integer id) throws SQLException
    {
        Statement statement = connection_.createStatement();

        startTransaction();
        try {
            statement.executeUpdate(String.format("DELETE FROM vegetation WHERE plant_id = %d", id));
            statement.executeUpdate(String.format("DELETE FROM plant_image WHERE plant_id = %d", id));
            statement.executeUpdate(String.format("DELETE FROM plant WHERE id = %d", id));

            commit();
        } catch (Exception e) {
            rollback();

            throw e;
        } finally {
            endTransaction();
        }
    }

    public List<ScientificName> getScientificNamesList() throws SQLException
    {
        List<ScientificName> list = new ArrayList<ScientificName>();

        Statement preparedStatement = connection_.createStatement();
        String query = "SELECT id, latin_name FROM plant";
        ResultSet resultSet = preparedStatement.executeQuery(query);
        while (resultSet.next()) {
            ScientificName entry = new ScientificName(resultSet.getInt("id"),
                                                      resultSet.getString("latin_name"));
            list.add(entry);
        }
        resultSet.close();
        preparedStatement.close();
        return list;
    }

    public Entry get(Integer id) throws SQLException
    {
        Entry plant = null;

        Statement statement = connection_.createStatement();

        String query = String.format("SELECT id, " + StringUtils.join(cols_, ", ") + " FROM plant WHERE id = %d", id);
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            plant = makeEntry(resultSet);
        }

        return plant;
    }

    public List<Entry> list() throws SQLException
    {
        List<Entry> list = new ArrayList<Entry>();

        Statement statement = connection_.createStatement();

        String query = "SELECT id, " + StringUtils.join(cols_, ", ") + " FROM plant";
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            list.add(makeEntry(resultSet));
        }

        return list;
    }

    public void edit(Entry plant) throws SQLException
    {
        ArrayList<String> cols = new ArrayList<String>();

        for (String col : cols_) {
            cols.add(col + " = ?");
        }

        String query = "UPDATE plant SET " + StringUtils.join(cols, ", ") + " WHERE id = ?";

        PreparedStatement preparedStatement = connection_.prepareStatement(query);

        int index = addFields(preparedStatement, plant);

        preparedStatement.setInt(index, plant.getId());

        preparedStatement.executeUpdate();
    }

    public Integer add(Entry plant) throws SQLException
    {
        String query = "INSERT INTO plant (" + StringUtils.join(cols_, ", ") + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection_.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

        addFields(preparedStatement, plant);

        preparedStatement.executeUpdate();

        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
            
        assert(false);

        return null;
    }

    private int addFields(PreparedStatement preparedStatement, Entry plant) throws SQLException
    {
        int index = 1;

        preparedStatement.setInt(index++, plant.getUserId());
        preparedStatement.setString(index++, plant.getCommonName());
        preparedStatement.setString(index++, plant.getScientificName());
        preparedStatement.setString(index++, plant.getDescription());
        preparedStatement.setString(index++, plant.getUrl());
        setInt(preparedStatement, index++, plant.getColor());
        setDouble(preparedStatement, index++, plant.getHeight());
        setDouble(preparedStatement, index++, plant.getNectar());
        setDouble(preparedStatement, index++, plant.getPollen());
        setDate(preparedStatement, index++, plant.getStart());
        setDate(preparedStatement, index++, plant.getEnd());
        preparedStatement.setInt(index++, plant.getVersion());

        return index;
    }

    public Hashtable<Integer, Integer> getVersions() throws SQLException
    {
        Hashtable<Integer, Integer> result = new Hashtable<Integer, Integer>();

        Statement statement = connection_.createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT id, version FROM plant");
        while (resultSet.next()) {
            result.put(getInteger(resultSet, "id"),
                       getInteger(resultSet, "version"));
        }

        return result;
    }

    public Integer getVersion(int id) throws SQLException
    {
        Statement statement = connection_.createStatement();

        String query = String.format("SELECT version FROM plant WHERE id = %d", id);

        ResultSet resultSet = statement.executeQuery(query);
        if (!resultSet.next()) {
            return null;
        }

        return resultSet.getInt("version");
    }

    public ArrayList<Integer> getIdentifiers() throws SQLException
    {
        ArrayList<Integer> result = new ArrayList<Integer>();

        Statement statement = connection_.createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT id FROM plant");
        while (resultSet.next()) {
            result.add(getInteger(resultSet, "id"));
        }

        return result;
    }
}
