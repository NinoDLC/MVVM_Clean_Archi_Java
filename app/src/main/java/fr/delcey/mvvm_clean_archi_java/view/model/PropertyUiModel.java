package fr.delcey.mvvm_clean_archi_java.view.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class PropertyUiModel {
    private final int id;
    @NonNull
    private final String type;
    @Nullable
    private final String mainAddress;


    public PropertyUiModel(int id, @NonNull String type, @Nullable String mainAddress) {
        this.id = id;
        this.type = type;
        this.mainAddress = mainAddress;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getType() {
        return type;
    }

    @Nullable
    public String getMainAddress() {
        return mainAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyUiModel that = (PropertyUiModel) o;
        return id == that.id &&
                type.equals(that.type) &&
                Objects.equals(mainAddress, that.mainAddress);
    }
}
