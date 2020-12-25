package me.theredcat.lib.db;

public class DatabaseSetupException extends RuntimeException{

    public DatabaseSetupException(String message, Throwable cause){
        super(message,cause);
    }

}
