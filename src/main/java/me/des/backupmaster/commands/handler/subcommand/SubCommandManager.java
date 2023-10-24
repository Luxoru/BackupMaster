package me.des.backupmaster.commands.handler.subcommand;

import lombok.Getter;
import me.des.backupmaster.BackupMaster;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubCommandManager {

    private final Map<String, Map<String, SubCommand>> commandSubCommands;
    @Getter
    private final BackupMaster plugin;

    public SubCommandManager(BackupMaster plugin){
        this.plugin = plugin;
        commandSubCommands = new HashMap<>();
    }

    public void registerSubCommands(String commandLabel, String subCommandLabel, SubCommand subCommand){
        Map<String, SubCommand> subCommands = commandSubCommands.computeIfAbsent(commandLabel.toLowerCase(), k -> new HashMap<>());
        subCommands.put(subCommandLabel.toLowerCase(), subCommand);
    }

    public List<String> getSubCommandNames(String commandLabel){
        Map<String, SubCommand> subCommands = commandSubCommands.get(commandLabel.toLowerCase());

        if(subCommands == null){
            return null;
        }

        List<String> subCommandNames = new ArrayList<>();

        subCommands.forEach((command, executor) -> subCommandNames.add(command.toLowerCase()));
        return subCommandNames;
    }

    public boolean executeSubCommand(CommandSender sender, @NotNull String commandLabel, String subCommandLabel, String[] args) {

        Map<String, SubCommand> subCommands = commandSubCommands.get(commandLabel.toLowerCase());
        if (subCommands != null) {
            SubCommand subCommand = subCommands.get(subCommandLabel.toLowerCase());
            if (subCommand != null) {
                subCommand.execute(sender, subCommandLabel, args);
                return true;
            }
        }
        return true;
    }

}
