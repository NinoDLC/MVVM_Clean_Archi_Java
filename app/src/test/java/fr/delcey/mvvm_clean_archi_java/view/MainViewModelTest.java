package fr.delcey.mvvm_clean_archi_java.view;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import org.hamcrest.beans.HasPropertyWithValue;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import fr.delcey.mvvm_clean_archi_java.LiveDataTestUtil;
import fr.delcey.mvvm_clean_archi_java.data.database.AddressDao;
import fr.delcey.mvvm_clean_archi_java.data.database.PropertyDao;
import fr.delcey.mvvm_clean_archi_java.data.database.model.Address;
import fr.delcey.mvvm_clean_archi_java.data.database.model.Property;
import fr.delcey.mvvm_clean_archi_java.view.model.PropertyUiModel;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

// Use JUnit4 for Unit Testing
@RunWith(JUnit4.class)
public class MainViewModelTest {

    // Check documentation but basically, with this rule : livedata.postValue() is the same as livedata.setValue()
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private MainViewModel viewModel;

    private MutableLiveData<List<Address>> addressesLiveData;
    private MutableLiveData<List<Property>> propertiesLiveData;

    @Before
    public void setup() {
        AddressDao addressDao = Mockito.mock(AddressDao.class);
        PropertyDao propertyDao = Mockito.mock(PropertyDao.class);

        addressesLiveData = new MutableLiveData<>();
        propertiesLiveData = new MutableLiveData<>();

        Mockito.doReturn(addressesLiveData).when(addressDao).getAddressesLiveData();
        Mockito.doReturn(propertiesLiveData).when(propertyDao).getPropertiesLiveData();

        viewModel = new MainViewModel(addressDao, propertyDao);
    }

    @Test
    public void shouldMapCorrectlyOneAddressWithOneProperty() throws InterruptedException {
        // Given
        List<Address> addresses = new ArrayList<>();
        Address address = new Address("101 rue des Dames");
        address.setId(1);
        addresses.add(address);
        addressesLiveData.setValue(addresses);

        List<Property> properties = new ArrayList<>();
        Property property = new Property("Business", 1);
        property.setId(1);
        properties.add(property);
        propertiesLiveData.setValue(properties);

        // When
        List<PropertyUiModel> result = LiveDataTestUtil.getOrAwaitValue(viewModel.getUiModelsLiveData());

        // Then
        assertEquals(1, result.size());
        // The most basic way to assert in a collection : get(0) after checking for size == 1
        assertEquals("101 rue des Dames", result.get(0).getMainAddress());
        // The most basic way to assert in a collection : get(0) after checking for size == 1
        assertEquals("Business", result.get(0).getType());
    }

    @Test
    public void shouldMapCorrectlyOnePropertyWithTwoAddresses() throws InterruptedException {
        // Given
        List<Address> addresses = new ArrayList<>();
        Address address = new Address("101 rue des Dames");
        address.setId(1);
        addresses.add(address);
        addressesLiveData.setValue(addresses);

        List<Property> properties = new ArrayList<>();
        Property property1 = new Property("Business", 1);
        property1.setId(1);
        properties.add(property1);
        propertiesLiveData.setValue(properties);
        Property property2 = new Property("Flat", 1);
        property2.setId(1);
        properties.add(property2);
        propertiesLiveData.setValue(properties);

        // When
        List<PropertyUiModel> result = LiveDataTestUtil.getOrAwaitValue(viewModel.getUiModelsLiveData());

        // Then
        assertEquals(2, result.size());
        // A more powerfull way to assert in a collection : it works with any order
        assertThat(
                result,
                containsInAnyOrder(
                        hasProperty("type", is("Flat")),
                        hasProperty("type", is("Business"))
                )
        );
        // Another more powerfull way to assert in a collection : every item has this property
        assertThat(
                result,
                everyItem(
                        HasPropertyWithValue.<PropertyUiModel>hasProperty( // Need to explicit the generic type of this... Fuck Java.
                                "mainAddress",
                                is("101 rue des Dames")
                        )
                )
        );
    }

    @Test
    public void shouldMapCorrectlyTwoPropertiesWithOneAddressEach() throws InterruptedException {
        // Given
        List<Address> addresses = new ArrayList<>();
        Address address1 = new Address("101 rue des Dames");
        address1.setId(1);
        addresses.add(address1);
        Address address2 = new Address("31 rue Tronchet");
        address2.setId(2);
        addresses.add(address2);
        addressesLiveData.setValue(addresses);

        List<Property> properties = new ArrayList<>();
        Property property1 = new Property("Business", 1);
        property1.setId(1);
        properties.add(property1);
        propertiesLiveData.setValue(properties);
        Property property2 = new Property("Flat", 1);
        property2.setId(2);
        properties.add(property2);
        propertiesLiveData.setValue(properties);

        // When
        List<PropertyUiModel> result = LiveDataTestUtil.getOrAwaitValue(viewModel.getUiModelsLiveData());

        // Then
        assertEquals(2, result.size());
        // The most powerfull way to assert in a collection : it works with any order with complex items
        assertThat(
                result,
                containsInAnyOrder(
                        allOf(
                                hasProperty("type", is("Flat")),
                                hasProperty("mainAddress", is("31 rue Tronchet"))
                        ),
                        allOf(
                                hasProperty("type", is("Business")),
                                hasProperty("mainAddress", is("101 rue des Dames"))
                        )
                )
        );
    }
}