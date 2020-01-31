package fr.delcey.mvvm_clean_archi_java;

import androidx.annotation.Nullable;

import java.util.Map;

public class Utils {
    public static String convertMapToString(@Nullable Map<?, ?> map) {
        if (map == null) {
            return "null Map";
        }

        StringBuilder mapAsString = new StringBuilder("{");
        for (Object key : map.keySet()) {
            mapAsString
                .append(key)
                .append("=")
                .append(map.get(key))
                .append(", ");
        }
        mapAsString.delete(mapAsString.length() - 2, mapAsString.length()).append("}");
        return mapAsString.toString();
    }
}
