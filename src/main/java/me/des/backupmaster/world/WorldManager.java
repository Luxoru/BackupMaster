package me.des.backupmaster.world;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import me.des.backupmaster.BackupMaster;
import me.des.backupmaster.collections.database.DataContainer;
import me.des.backupmaster.collections.database.DataManager;
import me.des.backupmaster.util.array.NoMultiverseEnabledException;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;

public class WorldManager {

    private final Set<String> worlds;
    private final BackupMaster plugin;
    private DataManager dataManager;

    public WorldManager(BackupMaster plugin) {
        this.worlds = new HashSet<>();
        this.plugin = plugin;
        dataManager = plugin.getDataManager();
        getAllWorldsOnServer();
    }


    public boolean addWorld(World world){
        return worlds.add(world.getName());
    }

    public boolean removeWorld(@NotNull World world){
        return worlds.remove(world.getName());
    }

    public World getWorld(String worldName){
        String nameOfWorld = getWorldFromName(worldName).join();
        return Bukkit.getWorld(nameOfWorld);
    }

    private CompletableFuture<String> getWorldFromName(String name){
        CompletableFuture<String> future = new CompletableFuture<>();
        CompletableFuture.supplyAsync(() ->{
            for(String world : worlds){
                if(world.equals(name)){
                    future.complete(world);
                    return future;
                }
            }
            future.complete(null);
            return future;
        });
        return future;
    }

    public boolean doesWorldExist(World world){
        return hasWorld(world).join();
    }
    public boolean doesWorldExist(String worldName){
        if(Bukkit.getWorld(worldName) == null)return false;
        return hasWorld(Bukkit.getWorld(worldName)).join();
    }

    @Contract("_ -> new")
    private @NotNull CompletableFuture<Boolean> hasWorld(World world){
        return CompletableFuture.supplyAsync(() -> worlds.contains(world.getName()));
    }

    public CompletableFuture<File> getWorldFolder(String worldName){
        return CompletableFuture.supplyAsync(() -> getFolder(worldName));
    }

    private File getFolder(String folderName){
        File rootFolder = getRootFolder();
        // Construct the path to the "worlds" directory
        String decodedPath = URLDecoder.decode(rootFolder.getAbsolutePath(), StandardCharsets.UTF_8);
        rootFolder = new File(decodedPath);
        if (rootFolder.exists() && rootFolder.isDirectory()) {
            File[] worldFolders = rootFolder.listFiles();
            if (worldFolders != null) {
                for (File worldFolder : worldFolders) {
                    if (worldFolder.isDirectory() && hasLevelDatFile(worldFolder) && worldFolder.getName().equals(folderName)) {
                        return worldFolder;
                    }
                }
            }
        }
        return null;
    }

    public File getRootFolder(){
        File pluginFile = new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        return pluginFile.getParentFile().getParentFile();
    }

    private void getAllWorldsOnServer() {

        plugin.getLogger().log(Level.INFO, "Loading Worlds ...");

        ExecutorService executor = Executors.newCachedThreadPool();


        Future<List<String>> future = executor.submit(() -> {
            List<String> worldsList = new ArrayList<>();

            File pluginFile = new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
            File rootFolder = pluginFile.getParentFile().getParentFile();

            // Construct the path to the "worlds" directory
            String decodedPath = URLDecoder.decode(rootFolder.getAbsolutePath(), StandardCharsets.UTF_8);
            rootFolder = new File(decodedPath);

            if (rootFolder.exists() && rootFolder.isDirectory()) {
                File[] worldFolders = rootFolder.listFiles();
                if (worldFolders != null) {
                    for (File worldFolder : worldFolders) {
                        if (worldFolder.isDirectory() && hasLevelDatFile(worldFolder)) {
                            worldsList.add(worldFolder.getName());
                        }
                    }
                }
            }

            return worldsList;
        });

        executor.shutdown();
        try{
            this.worlds.addAll(future.get());
            plugin.getLogger().log(Level.INFO, "\u001B[32mSuccessfully loaded worlds.\u001B[0m");
        } catch (ExecutionException | InterruptedException e) {
            plugin.getLogger().log(Level.SEVERE, "\u001B[31mError whilst loading worlds!\u001B[0m");
            throw new RuntimeException(e);
        }
    }

    public Collection<String> getWorlds(){
        return worlds;
    }



    private boolean hasLevelDatFile(File worldFolder) {
        File levelDatFile = new File(worldFolder, "level.dat");
        return levelDatFile.exists() && levelDatFile.isFile();
    }

    @Getter
    public enum WorldType {

        NORMAL("normal", 0), NETHER("nether", -1), END("the_end", 1), CUSTOM("custom", -999);


        public final int id;
        public final String worldName;

        WorldType(String worldName, int id) {
            this.worldName = worldName;
            this.id = id;
        }

        public static String from(WorldType type){
            for(WorldType t : values()){
                if(Objects.equals(t.worldName, type.worldName))return t.name();
            }
            return null;
        }


        public static WorldType valueFrom(String type){
            for(WorldType t : values()){
                if(t.name().equalsIgnoreCase(type)) return t;
            }
            return null;
        }


    }



    public boolean loadWorld(String worldName, WorldType worldType){


        String defaultLoader = plugin.getConfig().getString("default");

        DataContainer container = dataManager.getFromContainerName(defaultLoader);

        if(container == null){
            return false;
        }

        CompletableFuture<Boolean> worldCompletableFuture = container.loadWorld(worldName, worldType.name());

        worldCompletableFuture.completeExceptionally(new NoMultiverseEnabledException());
        return worldCompletableFuture.join();


    }



    public boolean worldTypeExists(String worldType){
        try{
            WorldType type = WorldType.valueFrom(worldType);
            if(type == null){
                return false;
            }
            World.Environment worldType1 = World.Environment.valueOf(worldType);
        }catch (IllegalArgumentException e){
            return false;
        }
        return true;
    }




}
