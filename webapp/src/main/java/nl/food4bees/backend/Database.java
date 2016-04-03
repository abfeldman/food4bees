package nl.food4bees.backend;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Types;

import java.util.Date;

public class Database
{
    protected Connection connection_;

    public Database() throws SQLException
    {
        connection_ = ConnectionFactory.instance().getConnection();
    }

    public void startTransaction() throws SQLException
    {
        connection_.setAutoCommit(false);
    }

    public void endTransaction() throws SQLException
    {
        connection_.setAutoCommit(true);
    }

    public void commit() throws SQLException
    {
        connection_.commit();
    }

    public void rollback() throws SQLException
    {
        connection_.rollback();
    }

    protected Integer getInteger(ResultSet resultSet, String column) throws SQLException
    {
        int value = resultSet.getInt(column);
        if (resultSet.wasNull()) {
            return null;
        }
        return new Integer(value);
    }

    protected Double getDouble(ResultSet resultSet, String column) throws SQLException
    {
        double value = resultSet.getDouble(column);
        if (resultSet.wasNull()) {
            return null;
        }
        return new Double(value);
    }


    protected void setInt(PreparedStatement preparedStatement, int parameterIndex, Integer x) throws SQLException
    {
        if (x == null) {
            preparedStatement.setNull(parameterIndex, Types.INTEGER);
        } else {
            preparedStatement.setInt(parameterIndex, x);
        }
    }

    protected void setDouble(PreparedStatement preparedStatement, int parameterIndex, Double x) throws SQLException
    {
        if (x == null) {
            preparedStatement.setNull(parameterIndex, Types.NUMERIC);
        } else {
            preparedStatement.setDouble(parameterIndex, x);
        }
    }

    protected void setDate(PreparedStatement preparedStatement, int parameterIndex, Date x) throws SQLException
    {
        if (x == null) {
            preparedStatement.setNull(parameterIndex, Types.TIMESTAMP);
        } else {
            preparedStatement.setDate(parameterIndex, new java.sql.Date(x.getTime()));
        }
    }
}
