package fr.delcey.mvvm_clean_archi_java.data;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class NearbyPlaceRepo {

    public LiveData<List<Restaurant>> getRestaurantsNearby(Location location) {
        MutableLiveData<List<Restaurant>> restaurantsLiveData = new MutableLiveData<>();

        // retrofit.nearby(location.lat, location.long).enqueue(callback) {
        //    onSuccess(body) {
        //       restaurantsLiveData.postValue(body)


        return restaurantsLiveData;
    }

    public static class Restaurant {
        private Integer id;
        private String name;

        public Restaurant(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
