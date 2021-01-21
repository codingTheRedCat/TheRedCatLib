package me.theredcat.lib.db;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Represents a simple operation on a SQL ResultSet
 *
 * @param <T>
 */
public interface ResultSetOperation<T> {

    /**
     * Executes the operation
     *
     * @param resultSet ResultSet for operation
     * @return Result of the operation
     * @throws SQLException If an SQLException occurs while doing operation
     */
    T doOperation(ResultSet resultSet) throws SQLException;

}
