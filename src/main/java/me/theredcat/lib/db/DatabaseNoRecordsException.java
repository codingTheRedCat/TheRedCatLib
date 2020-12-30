package me.theredcat.lib.db;

public class DatabaseNoRecordsException extends Exception {

    public DatabaseNoRecordsException() {
        super("No records found in result set.");
    }

}
