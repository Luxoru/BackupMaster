package me.des.backupmaster.commands.handler.subcommand.impl.backup;

import me.des.backupmaster.BackupMaster;
import me.des.backupmaster.collections.database.DataContainer;
import me.des.backupmaster.commands.handler.subcommand.SubCommand;
import me.des.backupmaster.util.list.ListUtils;
import me.des.backupmaster.world.WorldManager;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BackupFetchCommand implements SubCommand {

    private final BackupMaster plugin;
    private final DataContainer container;

    public BackupFetchCommand(BackupMaster plugin, DataContainer container){
        this.plugin = plugin;
        this.container = container;
    }


    @Override
    public void execute(CommandSender sender, String command, String[] args) {
        if(sender instanceof Player player){
            if(args.length >= 2){

                if(!plugin.getWorldManager().worldTypeExists(args[1])){
                    player.sendMessage("SANKDLAJSLD");
                    return;
                }

                CompletableFuture<String> worldFuture =  plugin.getDataManager().getDefaultContainer().fetchWorld(args[0], "normal");


                worldFuture.thenAcceptAsync((res) ->{
                    player.sendMessage("Restored backup into main world folder. Use multiverse to load this world ("+res+")");
                });



                return;
            }
            player.sendMessage("Fetching worlds...");
            CompletableFuture<List<String>> worlds = container.fetchAllWorlds(WorldManager.WorldType.NORMAL);
            List<String> w = worlds.join();

            player.sendMessage("Worlds fetched!");

            TextComponent component = ListUtils.formatListToClickableComponent(w, "backup fetch");
            player.spigot().sendMessage(component);

        }

    }
}
