package me.des.backupmaster.util;

import com.fasterxml.jackson.databind.util.ArrayBuilders;
import me.des.backupmaster.util.array.ArrayUtils;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ChatUtil {

    @Contract("_ -> new")
    public static @NotNull String formatString(String string){
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String @NotNull [] formatArray(String @NotNull [] arr){

        List<String> elements = new ArrayList<>();

        for(String str : arr){
            elements.add(formatString(str));
        }

        return ArrayUtils.forceListToArray(elements, String.class);

    }


    public static List<String> formatList(@NotNull List<String> list){
        return list.stream().map(ChatUtil::formatString).collect(Collectors.toList());
    }

}
