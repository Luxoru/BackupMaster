package me.des.backupmaster.collections.database.impl.remote;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import me.des.backupmaster.BackupMaster;
import me.des.backupmaster.collections.database.DataContainer;
import me.des.backupmaster.world.WorldManager;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MongoDataContainer extends RemoteDataContainer {

    private MongoClient mongoClient;
    private MongoCollection<Document> templatesCollection;




    public MongoDataContainer(BackupMaster plugin) {
        super(plugin, "mongo");
    }

    @Override
    public CompletableFuture<String> fetchWorld(String name, String worldType) {
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
        return "mongo";
    }

    @Override
    public CompletableFuture<Boolean> loadWorld(String name, String worldType) {
        return null;
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
        database = section.getString("database", "cosmos");
        collectionName = section.getString("collection", "cosmos_templates");

        mongoClient = MongoClients.create(new ConnectionString(connectionString));
        templatesCollection = mongoClient.getDatabase(database).getCollection(collectionName); // if the collection doesn't exist, it will be created

        // validate the session
        mongoClient.listDatabaseNames().first(); // throws an exception if the connection is invalid
    }

    @Override
    protected void disconnect() {
        mongoClient.close();
    }

    @Override
    protected String createConnectionString(String ip, int port, String authsource, String username, String password, boolean ssl) {
        StringBuilder builder = new StringBuilder();
        builder.append("mongodb://");
        if (username != null && !username.isEmpty()) {
            builder.append(username);
            if (password != null && !password.isEmpty()) {
                builder.append(":").append(password);
            }
            builder.append("@");
        }

        builder.append(ip).append(":").append(port);

        if (authsource != null && !authsource.isEmpty()) {
            builder.append("/?authSource=").append(authsource);
        }

        if (ssl) {
            builder.append("&ssl=true");
        }

        return builder.toString();
    }
}
