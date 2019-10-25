package fr.delcey.mvvm_clean_archi_java.data.database.model;

import androidx.annotation.VisibleForTesting;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Address {
    @PrimaryKey(autoGenerate = true)
    private int id = 0;

    private String path;

    private String city;

    public Address(String path, String city) {
        this.path = path;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public String getCity() {
        return city;
    }

    @VisibleForTesting
    public void setId(int id) {
        this.id = id;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}