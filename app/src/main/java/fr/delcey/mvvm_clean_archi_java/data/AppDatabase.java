package fr.delcey.mvvm_clean_archi_java.data;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import fr.delcey.mvvm_clean_archi_java.MainApplication;
import fr.delcey.mvvm_clean_archi_java.data.model.Address;
import fr.delcey.mvvm_clean_archi_java.data.model.Property;

@Database(entities = {Property.class, Address.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PropertyDao propertyDao();

    public abstract AddressDao addressDao();

    private static AppDatabase sInstance;

    public static AppDatabase getInstance() {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(
                            MainApplication.getInstance(),
                            AppDatabase.class,
                            "Database.db"
                    ).build();
                }
            }
        }

        return sInstance;
    }
}