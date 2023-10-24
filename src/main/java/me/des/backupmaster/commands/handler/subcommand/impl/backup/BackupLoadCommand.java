package me.des.backupmaster.commands.handler.subcommand.impl.backup;

import me.des.backupmaster.BackupMaster;
import me.des.backupmaster.collections.database.impl.local.FileDataContainer;
import me.des.backupmaster.commands.handler.subcommand.SubCommand;
import me.des.backupmaster.util.list.ListUtils;
import me.des.backupmaster.world.WorldManager;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BackupLoadCommand implements SubCommand {

    private final FileDataContainer container;
    private final BackupMaster plugin;

    public BackupLoadCommand(FileDataContainer container, BackupMaster plugin){
        this.container = container;
        this.plugin = plugin;
    }


    @Override
    public void execute(CommandSender sender, String command, String[] args) {

        if(sender instanceof Player player){
            player.sendMessage("Fetching worlds...");
            CompletableFuture<List<String>> worlds = container.fetchAllWorlds(WorldManager.WorldType.NORMAL);
            List<String> w = worlds.join();

            player.sendMessage("Worlds fetched!");

            TextComponent component = ListUtils.formatListToClickableComponent(w, "backup load");
            player.spigot().sendMessage(component);
        }

    }
}
