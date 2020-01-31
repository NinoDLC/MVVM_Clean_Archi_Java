package fr.delcey.mvvm_clean_archi_java.view.model;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;

import java.util.Objects;

public class PropertyUiModel {

    private final int id;

    @NonNull
    private final String description;

    @ColorRes
    private final int temperatureColor;

    public PropertyUiModel(int id, @NonNull String description, @ColorRes int temperatureColor) {
        this.id = id;
        this.description = description;
        this.temperatureColor = temperatureColor;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    @ColorRes
    public int getTemperatureColor() {
        return temperatureColor;
    }

    @NonNull
    @Override
    public String toString() {
        return "PropertyUiModel{" +
            "id=" + id +
            ", description='" + description + '\'' +
            ", temperatureColor=" + temperatureColor +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyUiModel that = (PropertyUiModel) o;
        return id == that.id &&
            temperatureColor == that.temperatureColor &&
            description.equals(that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, temperatureColor);
    }
}
