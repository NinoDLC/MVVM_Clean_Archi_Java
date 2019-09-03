package fr.delcey.mvvm_clean_archi_java.view;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import fr.delcey.mvvm_clean_archi_java.data.AddressDao;
import fr.delcey.mvvm_clean_archi_java.data.AppDatabase;
import fr.delcey.mvvm_clean_archi_java.data.PropertyDao;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private static ViewModelFactory sFactory;

    @NonNull
    private final AddressDao addressDao;
    @NonNull
    private final PropertyDao propertyDao;


    private ViewModelFactory(@NonNull AddressDao addressDao, @NonNull PropertyDao propertyDao) {
        this.addressDao = addressDao;
        this.propertyDao = propertyDao;
    }

    public static ViewModelFactory getInstance() {
        if (sFactory == null) {
            synchronized (ViewModelFactory.class) {
                if (sFactory == null) {
                    sFactory = new ViewModelFactory(
                            AppDatabase.getInstance().addressDao(),
                            AppDatabase.getInstance().propertyDao()
                    );
                }
            }
        }

        return sFactory;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(addressDao, propertyDao);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}