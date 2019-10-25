package fr.delcey.mvvm_clean_archi_java.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import fr.delcey.mvvm_clean_archi_java.data.database.model.Address;

@Dao
public
interface AddressDao {

    @Query("SELECT * FROM Address")
    LiveData<List<Address>> getAddressesLiveData();

    @Insert
    long insertAddress(Address address);
}