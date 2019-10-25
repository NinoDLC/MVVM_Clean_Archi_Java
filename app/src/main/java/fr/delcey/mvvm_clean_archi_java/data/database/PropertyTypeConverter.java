package fr.delcey.mvvm_clean_archi_java.data.database;

import androidx.room.TypeConverter;

public class PropertyTypeConverter {
    @TypeConverter
    public static PropertyType propertyTypeFromString(String propertyTypeName) {
        return PropertyType.valueOf(propertyTypeName);
    }

    @TypeConverter
    public static String propertyTypeToString(PropertyType propertyType) {
        return propertyType.name();
    }
}
