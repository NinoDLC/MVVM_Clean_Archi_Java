package fr.delcey.mvvm_clean_archi_java.data.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        foreignKeys = @ForeignKey(
                entity = Address.class,
                parentColumns = "id",
                childColumns = "addressId",
                onDelete = ForeignKey.CASCADE
        )
)
public class Property {

    @PrimaryKey(autoGenerate = true)
    private int id = 0;

    private String type;

    private long addressId;

    public Property(String type, long addressId) {
        this.type = type;
        this.addressId = addressId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public long getAddressId() {
        return addressId;
    }
}