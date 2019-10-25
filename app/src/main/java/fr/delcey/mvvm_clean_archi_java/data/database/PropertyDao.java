package fr.delcey.mvvm_clean_archi_java.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import fr.delcey.mvvm_clean_archi_java.data.database.model.Property;

@Dao
public
interface PropertyDao {

    @Query("SELECT * FROM Property")
    LiveData<List<Property>> getPropertiesLiveData();

    @Insert
    void insertProperty(Property property);

    @Update
    void updateProperty(Property property);
}