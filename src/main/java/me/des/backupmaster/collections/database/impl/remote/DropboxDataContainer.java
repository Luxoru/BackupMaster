package me.des.backupmaster.collections.database.impl.remote;

import me.des.backupmaster.BackupMaster;
import me.des.backupmaster.world.WorldManager;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DropboxDataContainer extends RemoteDataContainer{

    public DropboxDataContainer(BackupMaster plugin, String type) {
        super(plugin, type);
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
        return null;
    }

    @Override
    public CompletableFuture<Boolean> loadWorld(String name, String worldType) {
        return null;
    }

    @Override
    public CompletableFuture<List<String>> fetchAllWorlds(WorldManager.WorldType worldType) {
        return null;
    }

    @Override
    public boolean requiresCredentials() {
        return false;
    }

    @Override
    protected void connect() {

    }

    @Override
    protected void disconnect() {

    }

    @Override
    protected String createConnectionString(String ip, int port, String authsource, String username, String password, boolean ssl) {
        return null;
    }
}
