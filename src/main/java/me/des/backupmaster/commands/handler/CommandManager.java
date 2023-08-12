package me.des.backupmaster.commands.handler;

import me.des.backupmaster.BackupMaster;
import me.des.backupmaster.commands.world.SaveWorldCommand;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommandManager {

    private BackupMaster plugin;
    private Map<String, CommandExecutor> commands;



    public CommandManager(BackupMaster plugin) {
        this.plugin = plugin;
        this.commands = new ConcurrentHashMap<>();
    }

    public void registerCommand(String commandName, CommandExecutor commandExecutor) {
        PluginCommand command = plugin.getCommand(commandName);
        if (command != null) {
            command.setExecutor(commandExecutor);
            commands.put(commandName, commandExecutor);
        } else {
            plugin.getLogger().warning("Failed to register command: " + commandName);
        }

    }


    public void unregisterCommands() {
        for (String commandName : commands.keySet()) {
            PluginCommand command = plugin.getCommand(commandName);
            if (command != null) {
                command.setExecutor(null);
            }
        }
        commands.clear();
    }

    // Example method to initialize and register all commands
    public void initializeCommands() {
        // Instantiate and register your commands
        registerCommand("saveWorld", new SaveWorldCommand(plugin));
        // Add more commands as needed
    }

}
