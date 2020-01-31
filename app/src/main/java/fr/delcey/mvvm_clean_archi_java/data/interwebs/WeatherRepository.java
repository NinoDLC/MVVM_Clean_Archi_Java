package fr.delcey.mvvm_clean_archi_java.data.interwebs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;

import fr.delcey.mvvm_clean_archi_java.data.interwebs.model.WeatherApiResponse;
import fr.delcey.mvvm_clean_archi_java.data.interwebs.model.WeatherValues;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherRepository {

    private static final String APIKEY_NAME = "appid";
    private static final String APIKEY_VALUE = "ef632f14705ca08eddf8cd7fc7a29d4f";
    private static final String METRICS_UNITS_NAME = "units";
    private static final String METRICS_UNITS_VALUE = "metric";

    private WeatherApi service;

    public WeatherRepository() {
        // API Key interceptor
        Interceptor apikeyAndMetricsInterceptor = new Interceptor() {
            @NonNull
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.url()
                    .newBuilder()
                    .addQueryParameter(APIKEY_NAME, APIKEY_VALUE)
                    .addQueryParameter(METRICS_UNITS_NAME, METRICS_UNITS_VALUE)
                    .build();
                request = request.newBuilder().url(url).build();
                return chain.proceed(request);
            }
        };

        // Logging interceptor (to see http call on logcat)
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        // Inject interceptors to client
        OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(apikeyAndMetricsInterceptor)
            .addInterceptor(loggingInterceptor)
            .build();

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        service = retrofit.create(WeatherApi.class);
    }

    // TODO SOME CACHING SHOULD BE DONE HERE !
    @Nullable
    public WeatherValues getWeatherForCity(@NonNull String city) throws IOException {
        WeatherApiResponse response = service.getWeatherForCity(city).execute().body();

        if (response != null) {
            return response.getWeatherValues();
        } else {
            return null;
        }
    }
}
