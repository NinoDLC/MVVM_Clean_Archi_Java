package fr.delcey.mvvm_clean_archi_java.data.interwebs.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeatherValues {
    @SerializedName("temp")
    @Expose
    private Double temperature;

    public WeatherValues(Double temperature) {
        this.temperature = temperature;
    }

    public Double getTemperature() {
        return temperature;
    }

    // THERE SHOULD BE MORE INFORMATIONS HERE
}
