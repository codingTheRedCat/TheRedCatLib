package me.theredcat.lib.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Consumer;

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

    public void execute(String sql, Consumer<ResultSet> resultSetOperations) throws SQLException {
        Statement statement = getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        resultSetOperations.accept(resultSet);

        resultSet.close();
        statement.close();
    }

}
