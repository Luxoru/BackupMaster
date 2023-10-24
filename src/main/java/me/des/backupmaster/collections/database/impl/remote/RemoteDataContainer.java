package me.des.backupmaster.collections.database.impl.remote;

import me.des.backupmaster.BackupMaster;
import me.des.backupmaster.collections.database.DataContainer;
import org.bukkit.configuration.ConfigurationSection;

import java.util.concurrent.CompletableFuture;

public abstract class RemoteDataContainer implements DataContainer {

    protected final BackupMaster plugin;
    protected String connectionString;
    protected String database;
    protected String collectionName;

    protected final ConfigurationSection section;

    public RemoteDataContainer(BackupMaster plugin, String type){
        this.plugin = plugin;
        this.section = plugin.getConfig().getConfigurationSection(type);
        enable(section);
    }


    @Override
    public CompletableFuture<Boolean> enable(ConfigurationSection section) {
        return CompletableFuture.supplyAsync(() ->{
            connectionString = section.getString("connection-string");

            if (connectionString == null) {
                String ip = section.getString("ip");
                int port = section.getInt("port");
                String authsource = section.getString("auth-source");
                String username = section.getString("username");
                String password = section.getString("password");
                boolean ssl = section.getBoolean("ssl", false);
                createConnectionString(ip, port, authsource, username, password, ssl);
            }




            try {
                connect();
                return true;
            } catch (Exception expected) { // catching MongoException doesn't work for some reason
                return false;
            }
        });
    }

    protected abstract void connect();

    protected abstract void disconnect();

    protected abstract String createConnectionString(String ip, int port, String authsource, String username, String password, boolean ssl);


}
