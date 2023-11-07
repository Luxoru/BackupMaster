package me.des.backupmaster.commands.handler.subcommand.impl.backup;

import me.des.backupmaster.BackupMaster;
import me.des.backupmaster.collections.database.DataContainer;
import me.des.backupmaster.collections.database.impl.local.FileDataContainer;
import me.des.backupmaster.commands.handler.subcommand.SubCommand;
import me.des.backupmaster.commands.handler.subcommand.SubCommandManager;
import me.des.backupmaster.commands.world.backup.BackupCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class BackupCreateCommand implements SubCommand {

    private DataContainer container;
    private BackupMaster plugin;


    public BackupCreateCommand(DataContainer container, BackupMaster plugin){
        this.container = container;
        this.plugin = plugin;
    }


    @Override
    public void execute(CommandSender sender, String command, String[] args) {
        if(sender instanceof Player player){
            if(args.length == 0){
                player.sendMessage("Incorrect syntax");
                return;
            }
            String worldName = args[0];
            if(BackupMaster.getInstance().getWorldManager().doesWorldExist(worldName)){
                CompletableFuture<Boolean> future = container.saveWorld(worldName);
                future.thenAcceptAsync(result ->{
                    if(result){
                        player.sendMessage("World saved succesfully");
                    }else{
                        player.sendMessage("Error occurred whilst saving the world");
                    }
                });
                player.sendMessage("Saving world");
            }else{
                player.sendMessage("World doesnt exist!");
            }
        }
    }
}
