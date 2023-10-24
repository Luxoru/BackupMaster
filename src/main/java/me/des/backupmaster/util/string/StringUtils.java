package me.des.backupmaster.util.string;

public class StringUtils {

    public static String removeLastCharacter(String str) {
        if (str == null || str.isEmpty()) {
            return str; // Return the input string as is if it is null or empty
        }

        return str.substring(0, str.length() - 1);
    }


}
