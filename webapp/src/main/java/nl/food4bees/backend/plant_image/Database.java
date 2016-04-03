package nl.food4bees.backend.plant_image;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

public class Database extends nl.food4bees.backend.Database
{
    Database() throws SQLException
    {
    }

    public List<Entry> getList(Integer plantId) throws SQLException
    {
        List<Entry> list = new ArrayList<Entry>();

        String query = "SELECT id, plant_id, caption, LENGTH(image) FROM plant_image WHERE plant_id = ?";

        PreparedStatement preparedStatement = connection_.prepareStatement(query);
        preparedStatement.setInt(1, plantId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Entry plantImage = makePlantImage(resultSet);
            list.add(plantImage);
        }
        resultSet.close();
        preparedStatement.close();

        return list;
    }

    public Entry get(Integer id) throws SQLException
    {
        Entry plantImage = null;

        String query = "SELECT id, plant_id, caption, LENGTH(image) FROM plant_image WHERE id = ?";

        PreparedStatement preparedStatement = connection_.prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            plantImage = makePlantImage(resultSet);
        }
        resultSet.close();
        preparedStatement.close();

        return plantImage;
    }

    private Entry makePlantImage(ResultSet resultSet) throws SQLException
    {
        Entry plantImage = new Entry(resultSet.getInt("id"),
                                     resultSet.getInt("plant_id"),
                                     resultSet.getString("caption"),
                                     resultSet.getInt("length"));
        return plantImage;
    }

    public void add(Integer plantId,
                    String caption,
                    byte[] image) throws SQLException
    {
        PreparedStatement preparedStatement = connection_.prepareStatement("INSERT INTO plant_image(plant_id, caption, image) VALUES (?, ?, ?)");
        preparedStatement.setInt(1, plantId);
        preparedStatement.setString(2, caption);
        preparedStatement.setBytes(3, image);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void edit(Entry image) throws SQLException
    {
        Statement statement = connection_.createStatement();

        String query = String.format("UPDATE plant_image SET caption = '%s' WHERE id = %d",
                                     image.getCaption(),
                                     image.getId());

        startTransaction();

        try {
            statement.executeUpdate(query);

            byte[] data = image.getImage();
            if (data != null && data.length > 0) {
                query = "UPDATE plant_image SET image = ? WHERE id = ?";

                PreparedStatement preparedStatement = connection_.prepareStatement(query);
            
                preparedStatement.setBytes(1, data);
                preparedStatement.setInt(2, image.getId());
            
                preparedStatement.executeUpdate();

                preparedStatement.close();
            }
        } catch (SQLException e) {
            rollback();

            throw e;
        } finally {
            commit();
        }

        endTransaction();
    }

    public void delete(Integer id) throws SQLException
    {
        Statement statement = connection_.createStatement();

        String query = String.format("DELETE FROM plant_image WHERE id = %d", id);

        statement.executeUpdate(query);
    }
}
