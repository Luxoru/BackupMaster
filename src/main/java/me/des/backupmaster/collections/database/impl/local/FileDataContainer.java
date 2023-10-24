package me.des.backupmaster.collections.database.impl.local;

import com.onarandombox.MultiverseCore.MVWorld;
import me.des.backupmaster.BackupMaster;
import me.des.backupmaster.collections.database.DataContainer;
import me.des.backupmaster.collections.util.file.FileUtils;
import me.des.backupmaster.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
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
    public CompletableFuture<World> fetchWorld(String name, String worldType) {
        WorldManager.WorldType type = WorldManager.WorldType.valueOf(worldType);
        if(type == null){
            return null;
        }

        // Move world folder to main folder

        // Load the folder using multiverse.

        return CompletableFuture.supplyAsync(() -> Bukkit.getWorld(name)); //Needs to be updated
    }

    @Override
    public CompletableFuture<Boolean> saveWorld(String name) {


        return CompletableFuture.supplyAsync(() -> {
            try {
                File worldFolder = BackupMaster.getInstance().getWorldManager().getWorldFolder(name).join();
                String filePath = plugin.getConfig().getString("file.directory");
                World world = Bukkit.getWorld(name);

                String env = world.getEnvironment().toString().toLowerCase();
                String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH-mm-ss").format(Calendar.getInstance().getTime());

                if(Objects.equals(filePath, "")){
                    FileUtils.cloneFolder(worldFolder.getAbsolutePath(), worldsContainer.getAbsolutePath()+"\\"+env+"\\"+name+"-"+timeStamp);
                }else{
                    FileUtils.cloneFolder(worldFolder.getAbsolutePath(), filePath+"\\"+env+"\\"+name+"-"+timeStamp);
                }

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
    public CompletableFuture<List<String>> fetchAllWorlds(WorldManager.WorldType worldType) {


        return CompletableFuture.supplyAsync(() ->{
            String type = WorldManager.WorldType.from(worldType);

            String filePath = plugin.getConfig().getString("file.directory");


            filePath = Objects.equals(filePath, "") ? worldsContainer.getAbsolutePath()+"\\"+type : filePath+"\\"+type;
            Path path = Paths.get(filePath);
            try {
                return FileUtils.listSubfolders(path);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public boolean requiresCredentials() {
        return false;
    }
}
