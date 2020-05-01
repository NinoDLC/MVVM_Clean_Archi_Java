package fr.delcey.mvvm_clean_archi_java.data;

import android.content.Context;
import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class GpsRepo {

    private Context context;

    private final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();

    public GpsRepo(Context context) {
        this.context = context;
    }

    public LiveData<Location> getLocationLiveData()  {
        // ... FusedLocationProviderClient(context)
        // locationMutableLiveData.postValue(position)

        return locationMutableLiveData;
    }
}
