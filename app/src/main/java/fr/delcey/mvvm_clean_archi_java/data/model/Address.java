package fr.delcey.mvvm_clean_archi_java.data.model;

import androidx.annotation.VisibleForTesting;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Address {
    @PrimaryKey(autoGenerate = true)
    private int id = 0;

    private String path;

    public Address(String path) {
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    @VisibleForTesting
    public void setId(int id) {
        this.id = id;
    }

    public void setPath(String path) {
        this.path = path;
    }
}