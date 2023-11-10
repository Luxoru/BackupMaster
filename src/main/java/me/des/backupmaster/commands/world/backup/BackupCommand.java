package me.des.backupmaster.commands.world.backup;

import me.des.backupmaster.BackupMaster;
import me.des.backupmaster.collections.database.impl.local.FileDataContainer;
import me.des.backupmaster.commands.handler.subcommand.SubCommandManager;
import me.des.backupmaster.commands.handler.subcommand.impl.backup.BackupCreateCommand;
import me.des.backupmaster.commands.handler.subcommand.impl.backup.BackupLoadCommand;
import me.des.backupmaster.util.ChatUtil;
import me.des.backupmaster.util.array.ArrayUtils;
import me.des.backupmaster.util.list.ListUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class BackupCommand implements CommandExecutor {


    protected BackupMaster plugin;
    private SubCommandManager manager;
    protected FileDataContainer fileDataContainer;


    public BackupCommand(SubCommandManager subCommandManager, BackupMaster plugin){
        this.plugin = plugin;
        this.manager = subCommandManager;
        this.fileDataContainer = new FileDataContainer(plugin);

        subCommandManager.registerSubCommands("backup", "create", new BackupCreateCommand(fileDataContainer, plugin));
        subCommandManager.registerSubCommands("backup", "load", new BackupLoadCommand(fileDataContainer, plugin));
    }




    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length > 0){
            return manager.executeSubCommand(commandSender, command.getLabel().toLowerCase(), args[0], ArrayUtils.removeFirstItem(args, String.class));
        }
        else{
            String subCommands = ListUtils.formatListInLineToString(manager.getSubCommandNames("backup"));
            commandSender.sendMessage(ChatUtil.formatString("&cInvalid format: &e/backup ("+subCommands+")"));
        }
        return true;
    }


    protected BackupMaster getPlugin() {
        return plugin;
    }

    protected SubCommandManager getManager() {
        return manager;
    }

    protected FileDataContainer getFileDataContainer() {
        return fileDataContainer;
    }
}
