package fr.delcey.mvvm_clean_archi_java.view;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.delcey.mvvm_clean_archi_java.data.GpsRepo;
import fr.delcey.mvvm_clean_archi_java.data.NearbyPlaceRepo;
import fr.delcey.mvvm_clean_archi_java.data.RestaurantDetailRepo;
import fr.delcey.mvvm_clean_archi_java.data.RestaurantDetailRepo.RestaurantDetail;

public class ListViewModel extends ViewModel {

    private final RestaurantDetailRepo restaurantDetailRepo;

    private final MediatorLiveData<List<UiModel>> mediatorLiveData = new MediatorLiveData<>();

    // Key : restaurantId, Value : restaurantDetail
    private final MediatorLiveData<Map<Integer, RestaurantDetail>> restaurantDetailMediatorLiveData = new MediatorLiveData<>();
    // Stocke tous les IDs de restaurants qu'on est en train de fetcher la donnée
    private final List<Integer> restaurantDetailAlreadyCalled = new ArrayList<>();

    public ListViewModel(GpsRepo gpsRepo, NearbyPlaceRepo nearbyPlaceRepo, RestaurantDetailRepo restaurantDetailRepo) {
        this.restaurantDetailRepo = restaurantDetailRepo;

        LiveData<Location> gpsLiveData = gpsRepo.getLocationLiveData();
        LiveData<List<NearbyPlaceRepo.Restaurant>> restaurantsLiveData = Transformations.switchMap(gpsLiveData, new Function<Location, LiveData<List<NearbyPlaceRepo.Restaurant>>>() {
            @Override
            public LiveData<List<NearbyPlaceRepo.Restaurant>> apply(Location location) {
                return nearbyPlaceRepo.getRestaurantsNearby(location);
            }
        });

        mediatorLiveData.addSource(restaurantsLiveData, new Observer<List<NearbyPlaceRepo.Restaurant>>() {
            @Override
            public void onChanged(List<NearbyPlaceRepo.Restaurant> restaurants) {
                ListViewModel.this.combine(restaurants, restaurantDetailMediatorLiveData.getValue());
            }
        });

        mediatorLiveData.addSource(restaurantDetailMediatorLiveData, new Observer<Map<Integer, RestaurantDetail>>() {
            @Override
            public void onChanged(Map<Integer, RestaurantDetail> stringRestaurantDetailMap) {
                ListViewModel.this.combine(restaurantsLiveData.getValue(), stringRestaurantDetailMap);
            }
        });
    }

    private void combine(@Nullable List<NearbyPlaceRepo.Restaurant> restaurants, @Nullable Map<Integer, RestaurantDetail> restaurantDetailMap) {
        if (restaurants == null) {
            return;
        }

        List<UiModel> uiModels = new ArrayList<>();

        for (NearbyPlaceRepo.Restaurant restaurant : restaurants) {
            String name = restaurant.getName();
            String distance = "50m"; // calculer la distance avec restaurant.getVicinity() et Location
            String openingHour = null;

            if (restaurantDetailMap == null || restaurantDetailMap.get(restaurant.getId()) == null) {
                if (!restaurantDetailAlreadyCalled.contains(restaurant.getId())) {
                    restaurantDetailAlreadyCalled.add(restaurant.getId());
                    LiveData<RestaurantDetail> restaurantDetailLiveData = restaurantDetailRepo.getRestaurantDetail(restaurant.getId());

                    restaurantDetailMediatorLiveData.addSource(restaurantDetailLiveData, new Observer<RestaurantDetail>() {
                        @Override
                        public void onChanged(RestaurantDetail restaurantDetail) {
                            Map<Integer, RestaurantDetail> updatedRestaurantDetailMap = restaurantDetailMediatorLiveData.getValue();

                            if (updatedRestaurantDetailMap == null) {
                                updatedRestaurantDetailMap = new HashMap<>();
                            }

                            updatedRestaurantDetailMap.put(restaurant.getId(), restaurantDetail);

                            restaurantDetailAlreadyCalled.remove(restaurant.getId());

                            restaurantDetailMediatorLiveData.setValue(updatedRestaurantDetailMap);
                        }
                    });
                }
            } else {
                RestaurantDetail restaurantDetail =  restaurantDetailMap.get(restaurant.getId());

                if (restaurantDetail != null) {
                    openingHour = restaurantDetail.getOpeningHours().toString();
                }
            }

            uiModels.add(new UiModel(name, distance, openingHour));
        }

        mediatorLiveData.setValue(uiModels);
    }


    private static class UiModel {

        @NonNull
        private final String name;
        @NonNull
        private final String distance;
        @Nullable
        private final String openingHours;

        public UiModel(@NonNull String name, @NonNull String distance, @Nullable String openingHours) {
            this.name = name;
            this.distance = distance;
            this.openingHours = openingHours;
        }

        @NonNull
        public String getName() {
            return name;
        }

        @NonNull
        public String getDistance() {
            return distance;
        }

        @Nullable
        public String getOpeningHours() {
            return openingHours;
        }
    }
}
