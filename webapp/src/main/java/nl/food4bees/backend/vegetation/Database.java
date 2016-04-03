package nl.food4bees.backend.vegetation;

import org.postgresql.geometric.PGpolygon;
import org.postgresql.geometric.PGpoint;

import nl.food4bees.backend.Polygon;
import nl.food4bees.backend.Point;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

import java.util.List;
import java.util.Locale;
import java.util.ArrayList;

public class Database extends nl.food4bees.backend.Database
{
    Database() throws SQLException
    {
    }

    public void addVegetation(Integer user,
                              Integer plant,
                              Polygon area,
                              Double amount)
        throws SQLException
    {
        String polygon = "POLYGON '(";

        int j = area.size();

        for (int i = 0; i < j; i++) {
            Point p = area.getPoint(i);

            if (i != 0) {
                polygon += ", ";
            }

            polygon += "(";
            polygon += p.getX();
            polygon += ", ";
            polygon += p.getY();
            polygon += ")";
        }

        polygon += ")'";

        String query = String.format("INSERT INTO vegetation (user_id, plant_id, area, amount) VALUES (%d, %d, %s, %f)",
                                     user,
                                     plant,
                                     polygon,
                                     amount);

        Statement statement = connection_.createStatement();
        statement.executeUpdate(query);
    }

    public List<DisplayEntry> getVegetationList() throws SQLException, Polygon.ParsingException
    {
        List<DisplayEntry> list = new ArrayList<DisplayEntry>();
        
        Statement statement = connection_.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT \"time\", email, latin_name, area FROM vegetation, \"user\", plant WHERE vegetation.user_id = \"user\".id AND vegetation.plant_id = plant.id");
        while (resultSet.next()) {
            Polygon polygon = makePolygon((PGpolygon)resultSet.getObject("area"));

            DisplayEntry entry = new DisplayEntry(resultSet.getDate("time"),
                                                  resultSet.getString("email"),
                                                  resultSet.getString("latin_name"),
                                                  polygon);
            list.add(entry);
        }

        return list;
    }

    private Entry makeEntry(ResultSet resultSet) throws SQLException, Polygon.ParsingException
    {
        Polygon polygon = makePolygon((PGpolygon)resultSet.getObject("area"));

        Entry entry = new Entry(resultSet.getInt("id"),
                                resultSet.getInt("user_id"),
                                resultSet.getInt("plant_id"),
                                polygon,
                                resultSet.getDouble("amount"),
                                resultSet.getDate("time"));
        return entry;
    }

    private Polygon makePolygon(PGpolygon polygon)
    {
        Polygon result = new Polygon();

        for (PGpoint point : polygon.points) {
            result.addPoint(new Point(point.x, point.y));
        }

        return result;
    }
}
