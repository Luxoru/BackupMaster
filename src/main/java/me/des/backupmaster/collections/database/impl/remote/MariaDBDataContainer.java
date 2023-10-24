package me.des.backupmaster.collections.database.impl.remote;

import me.des.backupmaster.BackupMaster;
import me.des.backupmaster.world.WorldManager;
import org.bukkit.World;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MariaDBDataContainer extends RemoteDataContainer{

    private final String username;
    private final String password;

    private Connection connection;

    public MariaDBDataContainer(BackupMaster plugin) {
        super(plugin, "mariadb");
        username = section.getString("username");
        password = section.getString("password");
    }

    @Override
    public CompletableFuture<World> fetchWorld(String name, String worldType) {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> saveWorld(String name) {
        return null;
    }

    @Override
    public CompletableFuture<Void> deleteWorld(String name) {
        return null;
    }

    @Override
    public CompletableFuture<Void> flush() {
        return null;
    }

    @Override
    public String getName() {
        return "mariadb";
    }

    @Override
    public CompletableFuture<List<String>> fetchAllWorlds(WorldManager.WorldType type) {
        return null;
    }

    @Override
    public boolean requiresCredentials() {
        return false;
    }

    @Override
    protected void connect() {
        try {
            this.connection = DriverManager.getConnection(connectionString, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void disconnect() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @SuppressWarnings("all")
    protected String createConnectionString(String ip, int port, String authsource, String username, String password, boolean ssl) {
        StringBuilder builder = new StringBuilder();
        builder.append("jdbc:mariadb://");
        builder.append(ip);
        builder.append(":");
        builder.append(port);
        builder.append("backupmaster");
        return builder.toString();
    }
}
