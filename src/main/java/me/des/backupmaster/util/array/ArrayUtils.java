package me.des.backupmaster.util.array;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.List;

public class ArrayUtils {

    @SuppressWarnings("unchecked")
    public static <T> T @NotNull [] forceListToArray (@NotNull List<T> list, Class<T> clazz){

        T[] arr = (T[]) Array.newInstance(clazz, list.size());

        return list.toArray(arr);

    }

    @SuppressWarnings("unchecked")
    public static <T> T[] removeFirstItem(T[] arr, Class<T> clazz){
        if (arr.length <= 1) {
            return (T[]) Array.newInstance(clazz, 0); // Return an empty array if the input array has no or one element
        }

        T[] modifiedArray = (T[]) Array.newInstance(clazz, arr.length - 1);
        System.arraycopy(arr, 1, modifiedArray, 0, arr.length - 1);
        return modifiedArray;
    }

}
