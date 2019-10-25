package fr.delcey.mvvm_clean_archi_java.data.interwebs.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeatherApiResponse {

    @SerializedName("main")
    @Expose
    private WeatherValues weatherValues;

    @SerializedName("name")
    @Expose
    private String name;

    public WeatherApiResponse(WeatherValues weatherValues, String name) {
        this.weatherValues = weatherValues;
        this.name = name;
    }

    public WeatherValues getWeatherValues() {
        return weatherValues;
    }

    public String getName() {
        return name;
    }
}
