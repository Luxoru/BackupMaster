package me.des.backupmaster.collections.database.impl.local;

import me.des.backupmaster.BackupMaster;
import me.des.backupmaster.collections.database.DataContainer;
import me.des.backupmaster.collections.util.file.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class FileDataContainer implements DataContainer {

    private final BackupMaster plugin;
    private final File worldsContainer;

    public FileDataContainer(@NotNull BackupMaster plugin){
        this.plugin = plugin;

        File dataFolder = plugin.getDataFolder();
        worldsContainer = new File(dataFolder, "worlds");
        createFolder(worldsContainer);

    }

    private void createFolder(@NotNull File worldsContainer) {
        if(!worldsContainer.exists()){
            worldsContainer.mkdirs();
        }
    }

    @Override
    public CompletableFuture<World> fetchWorld(String name) {
        return CompletableFuture.supplyAsync(() -> Bukkit.getWorld(name)); //Needs to be updated
    }

    @Override
    public CompletableFuture<Boolean> saveWorld(String name) {


        return CompletableFuture.supplyAsync(() -> {
            try {
                File worldFolder = BackupMaster.getInstance().getWorldManager().getWorldFolder(name).join();

                FileUtils.cloneFolder(worldFolder.getAbsolutePath(), worldsContainer.getAbsolutePath()+"\\"+name);

                return true; // Successfully cloned
            } catch (Exception ex) {
                BackupMaster.getInstance().getLogger().log(Level.SEVERE, "Error occurred while cloning", ex);
                return false; // Error occurred during cloning
            }
        });
    }




    @Override
    public CompletableFuture<Void> deleteWorld(String name) {
        CompletableFuture.runAsync(() ->{
            FileUtils.deleteFolder(worldsContainer.getAbsolutePath()+"\\"+name);
        });
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> flush() {
        return null;
    }

    @Override
    public String getName() {
        return "file";
    }

    @Override
    public CompletableFuture<Collection<String>> fetchAllWorlds() {
        return CompletableFuture.supplyAsync(() -> BackupMaster.getInstance().getWorldManager().getWorlds()); //Needs to be updated
    }

    @Override
    public boolean requiresCredentials() {
        return false;
    }
}
