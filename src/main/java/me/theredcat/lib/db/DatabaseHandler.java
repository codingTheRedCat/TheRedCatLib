package me.theredcat.lib.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DatabaseHandler {

    protected Connection connection;

    protected abstract void setupConn() throws SQLException;

    protected abstract void loadDriver();


    public void connect() throws SQLException {
        if (connection != null  &&  !(connection.isClosed()))
            return;

        setupConn();
    }

    public void disconnect() throws SQLException {
        if (connection != null  &&  !(connection.isClosed()))
            connection.close();
    }

    public void reconnect() throws SQLException {
        disconnect();

        setupConn();
    }


    public Connection getConnection() throws SQLException {
        connect();

        return connection;
    }


    public int execute(String sql) throws SQLException {
        Statement statement = getConnection().createStatement();

        final int resultCode = statement.executeUpdate(sql);

        statement.close();

        return resultCode;
    }

    public void execute(String sql, ResultSetOperation resultSetOperations) throws SQLException {
        Statement statement = getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        try {
            resultSetOperations.doOperation(resultSet);
        } finally {
            resultSet.close();
            statement.close();
        }
    }

    public void executeRequireNotEmpty(String sql, ResultSetOperation resultSetOperations) throws SQLException, DatabaseNoRecordsException {
        Statement statement = getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        if (!resultSet.next()) {
            resultSet.close();
            statement.close();

            throw new DatabaseNoRecordsException();
        }

        try {
            resultSetOperations.doOperation(resultSet);
        } finally {
            resultSet.close();
            statement.close();
        }
    }


}
