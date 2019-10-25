package fr.delcey.mvvm_clean_archi_java.view;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import fr.delcey.mvvm_clean_archi_java.MainApplication;
import fr.delcey.mvvm_clean_archi_java.data.database.AddressDao;
import fr.delcey.mvvm_clean_archi_java.data.database.AppDatabase;
import fr.delcey.mvvm_clean_archi_java.data.database.PropertyDao;
import fr.delcey.mvvm_clean_archi_java.data.interwebs.WeatherRepository;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private static ViewModelFactory sFactory;

    @NonNull
    private final AddressDao addressDao;
    @NonNull
    private final PropertyDao propertyDao;
    @NonNull
    private final WeatherRepository weatherRepository;


    private ViewModelFactory(
            @NonNull AddressDao addressDao,
            @NonNull PropertyDao propertyDao,
            @NonNull WeatherRepository weatherRepository
    ) {
        this.addressDao = addressDao;
        this.propertyDao = propertyDao;
        this.weatherRepository = weatherRepository;
    }

    public static ViewModelFactory getInstance() {
        if (sFactory == null) {
            synchronized (ViewModelFactory.class) {
                if (sFactory == null) {
                    sFactory = new ViewModelFactory(
                            AppDatabase.getInstance().addressDao(),
                            AppDatabase.getInstance().propertyDao(),
                            new WeatherRepository()
                    );
                }
            }
        }

        return sFactory;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(
                    MainApplication.getInstance(),
                    addressDao,
                    propertyDao,
                    weatherRepository
            );
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}