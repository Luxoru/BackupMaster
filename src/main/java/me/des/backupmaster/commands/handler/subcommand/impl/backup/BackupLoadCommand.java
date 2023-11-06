package me.des.backupmaster.commands.handler.subcommand.impl.backup;

import me.des.backupmaster.BackupMaster;
import me.des.backupmaster.collections.database.DataContainer;
import me.des.backupmaster.collections.database.impl.local.FileDataContainer;
import me.des.backupmaster.commands.handler.subcommand.SubCommand;
import me.des.backupmaster.commands.handler.subcommand.SubCommandManager;
import me.des.backupmaster.util.list.ListUtils;
import me.des.backupmaster.world.WorldManager;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class BackupLoadCommand implements SubCommand {

    private final DataContainer container;
    private final BackupMaster plugin;



    public BackupLoadCommand(DataContainer container, BackupMaster plugin){
        this.container = container;
        this.plugin = plugin;

    }

    private boolean worldLoading = false;


    @Override
    public void execute(CommandSender sender, String command, String[] args) {

        if(sender instanceof Player player){
            if(args.length >= 2){

                    if(!plugin.getWorldManager().worldTypeExists(args[1])){
                        player.sendMessage("SANKDLAJSLD");
                        return;
                    }
                    if(worldLoading){
                        player.sendMessage("IN USE");
                        return;
                    }
                    try{
                        worldLoading = true;
                        player.sendMessage("Started world loading!");
                        boolean hasWorldLoaded =  plugin.getWorldManager().loadWorld(args[0], WorldManager.WorldType.valueFrom(args[1]));

                        if(hasWorldLoaded){
                            player.sendMessage("World loaded successfully");
                        }else{
                            player.sendMessage("An error occurred loading this world");
                        }
                    }finally {
                        worldLoading = false;
                    }





                return;
            }else if(args.length == 1) {
                player.sendMessage("/backup load <worldName> <env>");
                return;
            }
            player.sendMessage("Fetching worlds...");
            CompletableFuture<List<String>> worlds = container.fetchAllWorlds(WorldManager.WorldType.NORMAL);
            List<String> w = worlds.join();

            player.sendMessage("Worlds fetched!");

            TextComponent component = ListUtils.formatListToClickableComponent(w, "backup load");
            player.spigot().sendMessage(component);
        }

    }
}
