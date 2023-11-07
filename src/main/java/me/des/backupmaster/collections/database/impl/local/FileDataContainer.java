package me.des.backupmaster.collections.database.impl.local;

import me.des.backupmaster.BackupMaster;
import me.des.backupmaster.collections.database.DataContainer;
import me.des.backupmaster.collections.util.file.FileUtils;
import me.des.backupmaster.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
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
    @Nullable
    public CompletableFuture<String> fetchWorld(String name, String worldType) {


        String filePath = getFileSavingPath()+"\\"+worldType+"\\"+name;
        System.out.println(filePath);
        File rootFolder = BackupMaster.getInstance().getWorldManager().getRootFolder();
        // Define a regular expression pattern to capture the text before the first dash
        Pattern pattern = Pattern.compile("([^\\-]+)-\\d{4}_\\d{2}_\\d{2}_\\d{2}-\\d{2}-\\d{2}");

        // Create a Matcher to find the pattern in the input string
        Matcher matcher = pattern.matcher(name);

        String result = null;

        if(matcher.find()){
            result = rootFolder +"\\"+ name;
            result = matcher.group(1);

            int count = 1;
            boolean exists = Files.exists(Path.of(result));
            while (exists){
                count++;
                exists = Files.exists(Path.of(result.concat(String.valueOf(count))));
            }

            if(count != 0){
                result = result.concat(String.valueOf(count));
            }

            try{
                cloneFolder(filePath, result);
            } catch (IOException e) {
                return null;
            }

        }




        return CompletableFuture.completedFuture(result);
    }

    @Override
    public CompletableFuture<Boolean> saveWorld(String name) {


        try {
            File worldFolder = BackupMaster.getInstance().getWorldManager().getWorldFolder(name).join();
            String filePath = getFileSavingPath();
            World world = Bukkit.getWorld(name);
            String env = world.getEnvironment().toString().toLowerCase();

            cloneFolder(worldFolder.getAbsolutePath(), formatSavedFolderPath(env,filePath, name));

            return CompletableFuture.completedFuture(true); // Successfully cloned
        } catch (Exception ex) {
            BackupMaster.getInstance().getLogger().log(Level.SEVERE, "Error occurred while cloning", ex);

        }
        return CompletableFuture.completedFuture(false); // Error occurred during cloning
    }


    private String getFileSavingPath(){
        String filePath = plugin.getConfig().getString("file.directory");
        if(Objects.equals(filePath, "")){
            return worldsContainer.getAbsolutePath();
        }
        return filePath;
    }

    private void cloneFolder(String from, String to) throws IOException{
        FileUtils.cloneFolder(from, to);
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


        WorldManager.WorldType type = WorldManager.WorldType.valueFrom(worldType);

        // Define a regular expression pattern to capture the text before the first dash
        Pattern pattern = Pattern.compile("([^\\-]+)-\\d{4}_\\d{2}_\\d{2}_\\d{2}-\\d{2}-\\d{2}");

        // Create a Matcher to find the pattern in the input string
        Matcher matcher = pattern.matcher(name);

        String result = "";
        // Move world folder to main folder



            String filePath = getFileSavingPath()+"\\"+type+"\\"+name;


            File rootFolder = BackupMaster.getInstance().getWorldManager().getRootFolder();


        // Load the folder using multiverse.




            // Check if the pattern is found
            if (matcher.find()) {
                // Get the captured group (text before the first dash)
                result = matcher.group(1);
                String pathTo = (rootFolder.getAbsolutePath()+"\\"+result);

                try{
                    cloneFolder(filePath, pathTo);
                } catch (IOException e) {
                    return CompletableFuture.completedFuture(false);
                }


                int count = 0;
                boolean exists = Files.exists(Path.of(pathTo));
                while (exists){
                    count++;
                    exists = Files.exists(Path.of(pathTo+count));
                }


                boolean importWorld = BackupMaster.getMv().getMVWorldManager().addWorld(result.concat(String.valueOf(count)), World.Environment.valueOf(type.name().toUpperCase()), "", WorldType.NORMAL, true, "");
                boolean res = BackupMaster.getMv().getMVWorldManager().loadWorld(result.concat(String.valueOf(count)));
                System.out.println("WORLD LOADED = "+res);
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
