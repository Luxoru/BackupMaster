package me.des.backupmaster.commands.handler;

import me.des.backupmaster.BackupMaster;
import me.des.backupmaster.commands.handler.subcommand.SubCommandManager;
import me.des.backupmaster.commands.world.backup.BackupCommand;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommandManager {

    private final BackupMaster plugin;
    private final Map<String, CommandExecutor> commands;

    private final SubCommandManager subCommandManager;



    public CommandManager(BackupMaster plugin) {
        this.plugin = plugin;
        this.commands = new ConcurrentHashMap<>();
        subCommandManager = new SubCommandManager(plugin);
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
        registerCommand("backup", new BackupCommand(subCommandManager, plugin));
        // Add more commands as needed
    }

}
