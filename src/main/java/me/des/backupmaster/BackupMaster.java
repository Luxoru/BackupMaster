package me.des.backupmaster;

import com.onarandombox.MultiverseCore.MultiverseCore;
import lombok.Getter;
import me.des.backupmaster.collections.database.DataManager;
import me.des.backupmaster.commands.handler.CommandManager;
import me.des.backupmaster.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

@Getter
public final class BackupMaster extends JavaPlugin {

    private WorldManager worldManager;

    @Getter
    private static MultiverseCore mv;

    @Getter
    private static BackupMaster instance;

    @Getter
    private DataManager dataManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.dataManager = new DataManager(this);
        boolean containerEnabled = this.dataManager.initAllContainers();
        if(!containerEnabled){
            Bukkit.getServer().getPluginManager().disablePlugin(this);
        }
        this.worldManager = new WorldManager(this);
        mv = (MultiverseCore) Bukkit.getPluginManager().getPlugin("Multiverse-Core");
        CommandManager manager = new CommandManager(this);
        manager.initializeCommands();
        if(mv == null){
            getLogger().log(Level.INFO, "Multiverse not found, some commands will be disabled");

        }

        instance = this;




    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
