package nl.food4bees.backend;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseStatistics extends Database
{
    DatabaseStatistics() throws SQLException
    {
    }

    Statistics getStatistics() throws SQLException {
        Statistics statistics = null;

        Integer users = null;
        Integer plants = null;
        Integer plantImages = null;
        Integer vegetationRecords = null;

        Statement statement = connection_.createStatement();

        ResultSet resultSet;

        resultSet = statement.executeQuery("SELECT COUNT(id) FROM \"user\"");
        if (resultSet.next()) {
            users = resultSet.getInt("count");
        }
        resultSet = statement.executeQuery("SELECT COUNT(id) FROM plant");
        if (resultSet.next()) {
            plants = resultSet.getInt("count");
        }
        resultSet = statement.executeQuery("SELECT COUNT(id) FROM plant_image");
        if (resultSet.next()) {
            plantImages = resultSet.getInt("count");
        }
        resultSet = statement.executeQuery("SELECT COUNT(id) FROM vegetation");
        if (resultSet.next()) {
            vegetationRecords = resultSet.getInt("count");
        }

        if (users != null &&
            plants != null &&
            plantImages != null &&
            vegetationRecords != null) {
            statistics = new Statistics(users, plants, plantImages, vegetationRecords);
        }

        return statistics;
    }
}
