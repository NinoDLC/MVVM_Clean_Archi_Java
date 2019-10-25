package fr.delcey.mvvm_clean_archi_java.data.interwebs;

import fr.delcey.mvvm_clean_archi_java.data.interwebs.model.WeatherApiResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface WeatherApi {

    @GET("data/2.5/weather")
    Call<WeatherApiResponse> getWeatherForCity(@Query("q") String city);
}
