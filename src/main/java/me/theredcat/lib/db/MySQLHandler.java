package me.theredcat.lib.db;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

public class MySQLHandler extends DatabaseHandler {

    private final URL connectionURL;

    private final Properties connectionProperties;

    public MySQLHandler(String host, String databaseName, Properties properties){
        try {
            connectionURL = new URL("jdbc:mysql://" + host + '/' + databaseName);
        } catch (MalformedURLException e) {
            throw new DatabaseSetupException("Can not create URL from given arguments", e);
        }

        connectionProperties = properties;
    }

    public MySQLHandler(String host, String databaseName, String user, String password){
        try {
            connectionURL = new URL("jdbc:mysql://" + host + '/' + databaseName);
        } catch (MalformedURLException e) {
            throw new DatabaseSetupException("Can not create URL from given arguments", e);
        }

        connectionProperties = new Properties();

        connectionProperties.setProperty("user",user);
        connectionProperties.setProperty("password", password);
    }

    @Override
    protected void setupConn() throws SQLException {
        connection = DriverManager.getConnection(connectionURL.toString(), connectionProperties);
    }

    @Override
    protected void loadDriver() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new DatabaseSetupException("MySQL Driver class not found", e);
        }
    }

    public static MySQLHandler fromConfig(org.bukkit.configuration.ConfigurationSection configSection, String hostnameKey, String portKey, String databaseNameKey, String usernameKey, String passwordKey){
        final Map<String, Object> values = configSection.getValues(false);

        Properties props = new Properties();

        String hostname = null, database = null;

        int port = 0;

        for (Map.Entry<String, Object> entry : values.entrySet()){

            if (hostnameKey != null  &&  hostnameKey.equals(entry.getKey())){
                hostname = (String) entry.getValue();
                continue;
            }

            if (portKey != null  && portKey.equals(entry.getKey())){
                port = (int) entry.getValue();
                continue;
            }

            if (databaseNameKey != null  && databaseNameKey.equals(entry.getKey())){
                database = (String) entry.getValue();
                continue;
            }

            if (usernameKey != null  && usernameKey.equals(entry.getKey())){
                props.setProperty("user", (String) entry.getValue());
                continue;
            }

            if (passwordKey != null  && passwordKey.equals(entry.getKey())){
                props.setProperty("password", (String) entry.getValue());
                continue;
            }

            props.setProperty(entry.getKey(), entry.getValue().toString());

        }

        return new MySQLHandler(hostname+':'+port, database, props);
    }

    public static MySQLHandler fromConfig(net.md_5.bungee.config.Configuration configSection, String hostnameKey, String portKey, String databaseNameKey, String usernameKey, String passwordKey){
        Properties props = new Properties();

        String hostname = null, database = null;

        int port = 0;

        for (String key : configSection.getKeys()){

            if (hostnameKey != null  &&  hostnameKey.equals(key)){
                hostname = (String) configSection.get(key);
                continue;
            }

            if (portKey != null  && portKey.equals(key)){
                port = (int) configSection.get(key);
                continue;
            }

            if (databaseNameKey != null  && databaseNameKey.equals(key)){
                database = (String) configSection.get(key);
                continue;
            }

            if (usernameKey != null  && usernameKey.equals(key)){
                props.setProperty("user", (String) configSection.get(key));
                continue;
            }

            if (passwordKey != null  && passwordKey.equals(key)){
                props.setProperty("password", (String) configSection.get(key));
                continue;
            }

            props.setProperty(key, configSection.get(key).toString());

        }

        return new MySQLHandler(hostname+':'+port, database, props);
    }


}
