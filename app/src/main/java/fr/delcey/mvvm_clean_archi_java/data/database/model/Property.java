package fr.delcey.mvvm_clean_archi_java.data.database.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import fr.delcey.mvvm_clean_archi_java.data.database.PropertyType;

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

    private PropertyType type;

    private int surfaceArea;

    private long addressId;

    public Property(PropertyType type, int surfaceArea, long addressId) {
        this.type = type;
        this.addressId = addressId;
        this.surfaceArea = surfaceArea;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(PropertyType type) {
        this.type = type;
    }

    public void setSurfaceArea(int surfaceArea) {
        this.surfaceArea = surfaceArea;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public int getId() {
        return id;
    }

    public PropertyType getType() {
        return type;
    }

    public int getSurfaceArea() {
        return surfaceArea;
    }

    public long getAddressId() {
        return addressId;
    }

    @NonNull
    @Override
    public String toString() {
        return "Property{" +
            "id=" + id +
            ", type=" + type +
            ", surfaceArea=" + surfaceArea +
            ", addressId=" + addressId +
            '}';
    }
}