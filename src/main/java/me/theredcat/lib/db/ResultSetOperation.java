package me.theredcat.lib.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetOperation {

    void doOperation(ResultSet resultSet) throws SQLException;

}
