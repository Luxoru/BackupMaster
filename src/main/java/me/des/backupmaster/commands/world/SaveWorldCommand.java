package me.des.backupmaster.commands.world;

import me.des.backupmaster.BackupMaster;
import me.des.backupmaster.collections.database.impl.local.FileDataContainer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public class SaveWorldCommand implements CommandExecutor {

    private final FileDataContainer fileDataContainer;

    private final BackupMaster plugin;

    public SaveWorldCommand(BackupMaster plugin){
        this.plugin = plugin;
        this.fileDataContainer = new FileDataContainer(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String string, @NotNull String[] args) {
        if(sender instanceof Player player){
            if(args.length == 0){
                player.sendMessage("Incorrect syntax");
                return true;
            }
            String worldName = args[0];
            if(BackupMaster.getInstance().getWorldManager().doesWorldExist(worldName)){
                CompletableFuture<Boolean> future = fileDataContainer.saveWorld(worldName);
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
        return true;
    }
}
