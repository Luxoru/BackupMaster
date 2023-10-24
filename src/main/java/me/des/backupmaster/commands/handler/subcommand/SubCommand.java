package me.des.backupmaster.commands.handler.subcommand;

import org.bukkit.command.CommandSender;

public interface SubCommand {

    void execute(CommandSender sender, String command, String[] args);

}
