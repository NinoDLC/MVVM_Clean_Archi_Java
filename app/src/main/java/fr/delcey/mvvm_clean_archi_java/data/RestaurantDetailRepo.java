package fr.delcey.mvvm_clean_archi_java.data;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class RestaurantDetailRepo {

    public LiveData<RestaurantDetail> getRestaurantDetail(int restaurantId) {
        MutableLiveData<RestaurantDetail> restaurantDetailLiveData = new MutableLiveData<>();

        // retrofit.restaurantDetail(restaurantId).enqueue(callback) {
        //    onSuccess(body) {
        //       restaurantDetailLiveData.postValue(body)


        return restaurantDetailLiveData;
    }

    public static class RestaurantDetail {
        private int id;
        private String name;
        private List<String> openingHours;

        public RestaurantDetail(int id, String name, List<String> openingHours) {
            this.id = id;
            this.name = name;
            this.openingHours = openingHours;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public List<String> getOpeningHours() {
            return openingHours;
        }
    }
}
