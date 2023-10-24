package me.des.backupmaster.util.list;

import me.des.backupmaster.util.string.StringUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;

public class ListUtils {

    public static  <T> String formatListInLineToString(List<T> listToFormat){
        StringBuilder formattedString = new StringBuilder();
        listToFormat.forEach(element -> {
            formattedString.append(" ").append(element.toString());
            formattedString.append(" |");
        });


        return StringUtils.removeLastCharacter(formattedString.toString());
    }


    public static TextComponent formatListToClickableComponent(List<String> list, String baseCommand){

        TextComponent component = new TextComponent("------------ Backups ------------");

        for(String elem : list){
            TextComponent textElem = new TextComponent("\n elem");
            textElem.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "\n/"+baseCommand+" "+elem));
            component.addExtra(textElem);
        }

        component.addExtra("\n---------------------------------");
        return component;
    }

}
