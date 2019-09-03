package fr.delcey.mvvm_clean_archi_java.view;

import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import fr.delcey.mvvm_clean_archi_java.Mock;
import fr.delcey.mvvm_clean_archi_java.data.AddressDao;
import fr.delcey.mvvm_clean_archi_java.data.PropertyDao;
import fr.delcey.mvvm_clean_archi_java.data.model.Address;
import fr.delcey.mvvm_clean_archi_java.data.model.Property;
import fr.delcey.mvvm_clean_archi_java.view.model.PropertyUiModel;

class MainViewModel extends ViewModel {

    private MediatorLiveData<List<PropertyUiModel>> mUiModelsLiveData = new MediatorLiveData<>();

    @NonNull
    private final AddressDao mAddressDao;
    @NonNull
    private final PropertyDao mPropertyDao;

    public MainViewModel(@NonNull AddressDao addressDao, @NonNull PropertyDao propertyDao) {
        mAddressDao = addressDao;
        mPropertyDao = propertyDao;

        wireUpMediator();
    }

    private void wireUpMediator() {
        final LiveData<List<Property>> propertiesLiveData = mPropertyDao.getPropertiesLiveData();
        final LiveData<List<Address>> addressesLiveData = mAddressDao.getAddressesLiveData();

        mUiModelsLiveData.addSource(propertiesLiveData, new Observer<List<Property>>() {
            @Override
            public void onChanged(List<Property> properties) {
                mUiModelsLiveData.setValue(combinePropertiesAndAddresses(properties, addressesLiveData.getValue()));
            }
        });

        mUiModelsLiveData.addSource(addressesLiveData, new Observer<List<Address>>() {
            @Override
            public void onChanged(List<Address> addresses) {
                mUiModelsLiveData.setValue(combinePropertiesAndAddresses(propertiesLiveData.getValue(), addresses));
            }
        });
    }

    @Nullable
    private List<PropertyUiModel> combinePropertiesAndAddresses(@Nullable List<Property> properties, @Nullable List<Address> addresses) {
        if (properties == null || addresses == null) {
            return null;
        }

        List<PropertyUiModel> result = new ArrayList<>();

        for (Property property : properties) {
            String propertyAdress = null;

            for (Address address : addresses) {
                if (address.getId() == property.getId()) {
                    propertyAdress = address.getPath();
                }
            }

            PropertyUiModel propertyUiModel = new PropertyUiModel(property.getId(), property.getType(), propertyAdress);

            result.add(propertyUiModel);
        }

        return result;
    }

    LiveData<List<PropertyUiModel>> getUiModelsLiveData() {
        return mUiModelsLiveData;
    }

    void addNewProperty() {
        new InsertDataAsyncTask(mAddressDao, mPropertyDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

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

            long newAddressId = mAddressDao.insertAddress(new Address(Mock.getAddress()));

            mPropertyDao.insertProperty(new Property(Mock.getType(), newAddressId));

            return null;
        }
    }
}
