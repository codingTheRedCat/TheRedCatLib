package me.theredcat.lib.db;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteHandler extends DatabaseHandler{

    private File dbFile;

    public SQLiteHandler(File SQLiteFile){
        dbFile = SQLiteFile;
    }

    @Override
    protected void setupConn() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getPath());
    }

    @Override
    protected void loadDriver() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new DatabaseSetupException("SQLite Driver class not found", e);
        }
    }
}
