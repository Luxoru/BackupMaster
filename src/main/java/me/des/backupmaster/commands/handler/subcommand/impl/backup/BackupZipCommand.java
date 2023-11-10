package me.des.backupmaster.commands.handler.subcommand.impl.backup;

import me.des.backupmaster.BackupMaster;
import me.des.backupmaster.collections.database.DataContainer;
import me.des.backupmaster.collections.util.file.FileZipper;
import me.des.backupmaster.commands.handler.subcommand.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.lang.module.Configuration;

public class BackupZipCommand implements SubCommand {

    private final BackupMaster plugin;
    private final DataContainer container;

    public BackupZipCommand(DataContainer container, BackupMaster plugin){
        this.container = container;
        this.plugin = plugin;
    }


    @Override
    public void execute(CommandSender sender, String command, String[] args) {

        if(sender instanceof Player player){

            if(args.length != 1 ){

                //Error msg

                return;

            }

            String worldZip = args[0];

            ConfigurationSection section = plugin.getConfig();

        }

    }
}
