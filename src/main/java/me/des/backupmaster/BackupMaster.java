package me.des.backupmaster;

import lombok.Getter;
import me.des.backupmaster.commands.handler.CommandManager;
import me.des.backupmaster.world.WorldManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class BackupMaster extends JavaPlugin {

    private WorldManager worldManager;
    @Getter
    private static BackupMaster instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        worldManager = new WorldManager(this);
        instance = this;
        CommandManager manager = new CommandManager(this);
        manager.initializeCommands();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
