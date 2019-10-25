package fr.delcey.mvvm_clean_archi_java.data.database;

import androidx.annotation.StringRes;

import fr.delcey.mvvm_clean_archi_java.R;

public enum PropertyType {
    FLAT(R.string.flat),
    MANSION(R.string.mansion),
    HOUSE(R.string.house),
    GARAGE(R.string.garage),
    BUILDING(R.string.building);

    @StringRes
    private int humanReadableStringRes;

    PropertyType(@StringRes int humanReadableStringRes) {
        this.humanReadableStringRes = humanReadableStringRes;
    }

    @StringRes
    public int getHumanReadableStringRes() {
        return humanReadableStringRes;
    }
}
