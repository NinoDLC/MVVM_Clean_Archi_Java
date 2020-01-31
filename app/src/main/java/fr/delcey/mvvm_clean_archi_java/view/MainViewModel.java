package fr.delcey.mvvm_clean_archi_java.view;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.delcey.mvvm_clean_archi_java.Mock;
import fr.delcey.mvvm_clean_archi_java.R;
import fr.delcey.mvvm_clean_archi_java.data.database.AddressDao;
import fr.delcey.mvvm_clean_archi_java.data.database.PropertyDao;
import fr.delcey.mvvm_clean_archi_java.data.database.model.Address;
import fr.delcey.mvvm_clean_archi_java.data.database.model.Property;
import fr.delcey.mvvm_clean_archi_java.data.interwebs.WeatherRepository;
import fr.delcey.mvvm_clean_archi_java.data.interwebs.model.WeatherValues;
import fr.delcey.mvvm_clean_archi_java.view.model.PropertyUiModel;

class MainViewModel extends ViewModel {

    private MediatorLiveData<List<PropertyUiModel>> mUiModelsLiveData = new MediatorLiveData<>();

    // Key (String) is the name of the city, Value (Double) is the temperature of the city in °C
    private MutableLiveData<HashMap<String, Double>> mCityTemperatureLiveData = new MutableLiveData<>();

    @NonNull
    private final Context mContext;
    @NonNull
    private final AddressDao mAddressDao;
    @NonNull
    private final PropertyDao mPropertyDao;
    @NonNull
    private final WeatherRepository mWeatherRepository;

    public MainViewModel(
        @NonNull Context context,
        @NonNull AddressDao addressDao,
        @NonNull PropertyDao propertyDao,
        @NonNull WeatherRepository weatherRepository
    ) {
        mContext = context;
        mAddressDao = addressDao;
        mPropertyDao = propertyDao;
        mWeatherRepository = weatherRepository;

        wireUpMediator();
    }


    LiveData<List<PropertyUiModel>> getUiModelsLiveData() {
        return mUiModelsLiveData;
    }

    void addNewProperty() {
        new InsertDataAsyncTask(mAddressDao, mPropertyDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void wireUpMediator() {
        final LiveData<List<Property>> propertiesLiveData = mPropertyDao.getPropertiesLiveData();
        final LiveData<List<Address>> addressesLiveData = mAddressDao.getAddressesLiveData();

        mUiModelsLiveData.addSource(propertiesLiveData, properties ->
            mUiModelsLiveData.setValue(
                combinePropertiesAndAddresses(
                    properties,
                    addressesLiveData.getValue(),
                    mCityTemperatureLiveData.getValue()
                )
            ));

        mUiModelsLiveData.addSource(addressesLiveData, addresses ->
            mUiModelsLiveData.setValue(
                combinePropertiesAndAddresses(
                    propertiesLiveData.getValue(),
                    addresses,
                    mCityTemperatureLiveData.getValue()
                )
            ));

        mUiModelsLiveData.addSource(mCityTemperatureLiveData, cityTemperatureHashMap ->
            mUiModelsLiveData.setValue(
                combinePropertiesAndAddresses(
                    propertiesLiveData.getValue(),
                    addressesLiveData.getValue(),
                    cityTemperatureHashMap
                )
            ));
    }

    @Nullable
    private List<PropertyUiModel> combinePropertiesAndAddresses(
        @Nullable List<Property> properties,
        @Nullable List<Address> addresses,
        @Nullable HashMap<String, Double> cityTemperatureHashMap
    ) {
        if (properties == null || addresses == null) {
            return null;
        }

        return map(properties, addresses, cityTemperatureHashMap);
    }

    private List<PropertyUiModel> map(
        @NonNull List<Property> properties,
        @NonNull List<Address> addresses,
        @Nullable HashMap<String, Double> cityTemperatureHashMap
    ) {
        List<PropertyUiModel> result = new ArrayList<>();

        for (Property property : properties) {
            Address propertyAdress = null;

            for (Address address : addresses) {
                if (address.getId() == property.getAddressId()) {
                    propertyAdress = address;
                    break;
                }
            }

            if (propertyAdress != null) {

                // Let's try to find the temperature of the city, maybe we already have it since
                // multiple properties can have the same city !
                Double temperature = null;

                if (cityTemperatureHashMap != null) {
                    temperature = cityTemperatureHashMap.get(propertyAdress.getCity());
                }

                // We didn't find it. It's ok, fetch it and display a grey color for the moment
                if (temperature == null) {
                    queryWeather(propertyAdress.getCity());
                }

                PropertyUiModel propertyUiModel = new PropertyUiModel(
                    property.getId(),
                    getPropertyName(
                        mContext,
                        propertyAdress,
                        property,
                        temperature
                    ),
                    getTemperatureColor(temperature)
                );

                result.add(propertyUiModel);
            } else {
                Log.w(MainViewModel.class.getSimpleName(), "Could not find an address for property = " + property);
            }
        }

        return result;
    }

    @ColorRes
    private int getTemperatureColor(@Nullable Double temperature) {
        if (temperature == null) {
            return R.color.default_temperature;
        }

        if (temperature < -20) {
            return R.color.temperature_minus20;
        } else if (temperature < -10) {
            return R.color.temperature_minus10;
        } else if (temperature < 0) {
            return R.color.temperature_0;
        } else if (temperature < 10) {
            return R.color.temperature_10;
        } else if (temperature < 20) {
            return R.color.temperature_20;
        } else if (temperature < 30) {
            return R.color.temperature_30;
        } else {
            return R.color.temperature_40;
        }
    }

    private void queryWeather(String city) {
        // This AsyncTask will get the temperature of the city asynchronously on the interwebs,
        // and then will call the function "addNewCityTemperature" on the MainThread when query is
        // finished.
        new GetWeatherAsyncTask(this, mWeatherRepository, city).execute();
    }

    private void addNewCityTemperature(String city, Double temperature) {
        if (temperature != null) {
            HashMap<String, Double> cityTemperatureHashMap = mCityTemperatureLiveData.getValue();

            if (cityTemperatureHashMap == null) {
                cityTemperatureHashMap = new HashMap<>();
            }

            cityTemperatureHashMap.put(city, temperature);

            mCityTemperatureLiveData.setValue(cityTemperatureHashMap);
        }
    }

    @NonNull
    private String getPropertyName(
        @NonNull Context context,
        @NonNull Address address,
        @NonNull Property property,
        @Nullable Double temperature
    ) {
        String humanReadablePropertyType = context.getString(property.getType().getHumanReadableStringRes());

        String humanReadableAddress = address.getPath() + ", " + address.getCity();

        String humanReadableTemperature;
        if (temperature != null) {
            humanReadableTemperature = context.getString(R.string.temperature_format, temperature);
        } else {
            humanReadableTemperature = context.getString(R.string.unknown_temperature);
        }

        return context.getString(
            R.string.template_address_type,
            humanReadablePropertyType,
            property.getSurfaceArea(),
            humanReadableAddress,
            humanReadableTemperature
        );
    }

    // Every AsyncTask must be "static class" instead of "class".
    private static class InsertDataAsyncTask extends AsyncTask<Void, Void, Void> {

        @NonNull
        private final AddressDao mAddressDao;
        @NonNull
        private final PropertyDao mPropertyDao;

        private InsertDataAsyncTask(@NonNull AddressDao addressDao, @NonNull PropertyDao propertyDao) {
            mAddressDao = addressDao;
            mPropertyDao = propertyDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            long newAddressId = mAddressDao.insertAddress(Mock.getRandomAddress());

            mPropertyDao.insertProperty(
                new Property(
                    Mock.getRandomPropertyType(),
                    Mock.getRandomSurfaceArea(),
                    newAddressId)
            );

            return null;
        }
    }

    // Every AsyncTask must be "static class" instead of "class".
    private static class GetWeatherAsyncTask extends AsyncTask<Void, Void, Double> {

        @NonNull
        private final WeakReference<MainViewModel> modelWeakReference;

        @NonNull
        private final WeatherRepository mWeatherRepository;

        @NonNull
        private final String mCity;

        private GetWeatherAsyncTask(
            @NonNull MainViewModel model,
            @NonNull WeatherRepository weatherRepository,
            @NonNull String city
        ) {
            modelWeakReference = new WeakReference<>(model);
            mWeatherRepository = weatherRepository;
            mCity = city;
        }

        @Nullable
        @Override
        protected Double doInBackground(Void... voids) {

            Double temperature = null;

            try {
                WeatherValues weatherValues = mWeatherRepository.getWeatherForCity(mCity);

                if (weatherValues != null) {
                    temperature = weatherValues.getTemperature();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return temperature;
        }

        // onPostExecute is done on the MainThread, so we can use LiveData.setValue() that is
        // synchronous instead of LiveData.postValue() that can have terrible race condition bugs.
        @Override
        protected void onPostExecute(@Nullable Double temperature) {
            // We keep a weak reference of the ViewModel because we don't want to leak it !
            MainViewModel referenced = modelWeakReference.get();

            if (referenced != null) {
                referenced.addNewCityTemperature(mCity, temperature);
            }
        }
    }
}
