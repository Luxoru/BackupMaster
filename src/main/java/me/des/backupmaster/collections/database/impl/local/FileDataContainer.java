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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        CompletableFuture<Boolean> future = loadWorld(name, worldType);

        if(future.join()){
            return CompletableFuture.supplyAsync(() -> Bukkit.getWorld(name));
        }

        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Boolean> saveWorld(String name) {


        return CompletableFuture.supplyAsync(() -> {
            try {
                File worldFolder = BackupMaster.getInstance().getWorldManager().getWorldFolder(name).join();
                String filePath = getFileSavingPath();
                World world = Bukkit.getWorld(name);
                String env = world.getEnvironment().toString().toLowerCase();

                cloneFolder(worldFolder.getAbsolutePath(), formatSavedFolderPath(env,filePath, name));

                return true; // Successfully cloned
            } catch (Exception ex) {
                BackupMaster.getInstance().getLogger().log(Level.SEVERE, "Error occurred while cloning", ex);
                return false; // Error occurred during cloning
            }
        });
    }


    private String getFileSavingPath(){
        String filePath = plugin.getConfig().getString("file.directory");
        if(Objects.equals(filePath, "")){
            return worldsContainer.getAbsolutePath();
        }
        return filePath;
    }

    private void cloneFolder(String from, String to){


        try {

            FileUtils.cloneFolder(from, to);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    private String formatSavedFolderPath(String env, String container, String worldName){
        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH-mm-ss").format(Calendar.getInstance().getTime());
        return container+"\\"+env+"\\"+worldName+"-"+timeStamp;
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
    public CompletableFuture<Boolean> loadWorld(String name, String worldType) {
        WorldManager.WorldType type = WorldManager.WorldType.valueOf(worldType);


        // Move world folder to main folder

        String filePath = getFileSavingPath()+"\\"+type+"\\"+name;

        File rootFolder = BackupMaster.getInstance().getWorldManager().getRootFolder();

        cloneFolder(filePath, rootFolder.getAbsolutePath());


        // Load the folder using multiverse.



        // Define a regular expression pattern to capture the text before the first dash
        Pattern pattern = Pattern.compile("([^\\-]+)-\\d{4}_\\d{2}_\\d{2}_\\d{2}-\\d{2}-\\d{2}");

        // Create a Matcher to find the pattern in the input string
        Matcher matcher = pattern.matcher(name);

        String result = "";

        // Check if the pattern is found
        if (matcher.find()) {
            // Get the captured group (text before the first dash)
            result = matcher.group(1);
            BackupMaster.getMv().getMVWorldManager().loadWorld(result);
            return CompletableFuture.completedFuture(true); // This will print "worldName" or any other variation before the first dash
        }else{
            return CompletableFuture.completedFuture(false);
        }
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
