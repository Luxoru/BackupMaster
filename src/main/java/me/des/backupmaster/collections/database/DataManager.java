package me.des.backupmaster.collections.database;

import me.des.backupmaster.BackupMaster;
import me.des.backupmaster.collections.database.impl.local.FileDataContainer;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public class DataManager {

    private final BackupMaster plugin;

    private final HashMap<String, DataContainer> enabledContainers;

    public DataManager(BackupMaster plugin){
        this.plugin = plugin;
        enabledContainers = new HashMap<>();
    }

    public boolean initAllContainers(){

        boolean containerEnabled = false;
        FileConfiguration config = plugin.getConfig();
        if(config.getBoolean("mariadb.enabled")){
            containerEnabled = true;
            enableContainer("mariadb");
        }
        if(config.getBoolean("file.enabled")){
            containerEnabled = true;
            enableContainer("file");
        }
        if(config.getBoolean("mongodb.enabled")){
            containerEnabled = true;
            enableContainer("mongodb");
        }
        return containerEnabled;
    }


    private void enableContainer(String container){
        enabledContainers.put(container, fromContainerName(container));
    }



    private DataContainer fromContainerName(String containerName){

        switch (containerName){

            case "file" -> {
                return new FileDataContainer(plugin);
            }
            default -> {
                return null;
            }
        }

    }


    public DataContainer getDefaultContainer(){
        FileConfiguration config = plugin.getConfig();

        return enabledContainers.get(config.getString("default"));
    }


    public DataContainer getFromContainerName(String containerName){
        return enabledContainers.get(containerName);
    }


}
