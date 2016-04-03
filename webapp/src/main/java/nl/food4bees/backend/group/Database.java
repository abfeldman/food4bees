package nl.food4bees.backend.group;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

public class Database extends nl.food4bees.backend.Database
{
    public Database() throws SQLException
    {
    }

    public List<Entry> list() throws SQLException
    {
        List<Entry> list = new ArrayList<Entry>();

        Statement statement = connection_.createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT id, name FROM \"group\"");
        while (resultSet.next()) {
            Entry group = new Entry(resultSet.getInt("id"),
                                    resultSet.getString("name"));

            list.add(group);
        }

        return list;
    }

    public Integer getId(String name) throws SQLException
    {
        Statement statement = connection_.createStatement();

        String query = String.format("SELECT id FROM \"group\" WHERE name = '%s'", name);

        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            return resultSet.getInt("id");
        }

        return null;
    }
}
