package fr.delcey.mvvm_clean_archi_java.view;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import org.hamcrest.beans.HasPropertyWithValue;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import fr.delcey.mvvm_clean_archi_java.LiveDataTestUtil;
import fr.delcey.mvvm_clean_archi_java.R;
import fr.delcey.mvvm_clean_archi_java.data.database.AddressDao;
import fr.delcey.mvvm_clean_archi_java.data.database.PropertyDao;
import fr.delcey.mvvm_clean_archi_java.data.database.PropertyType;
import fr.delcey.mvvm_clean_archi_java.data.database.model.Address;
import fr.delcey.mvvm_clean_archi_java.data.database.model.Property;
import fr.delcey.mvvm_clean_archi_java.data.interwebs.WeatherRepository;
import fr.delcey.mvvm_clean_archi_java.view.model.PropertyUiModel;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

// Use JUnit4 for Unit Testing
@RunWith(MockitoJUnitRunner.class)
public class MainViewModelTest {

    // Check documentation but basically, with this rule : livedata.postValue() is the same as livedata.setValue()
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private Context context;

    @Mock
    private WeatherRepository weatherRepository;

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

        viewModel = new MainViewModel(context, addressDao, propertyDao, weatherRepository);
    }

    @Test
    public void shouldMapCorrectlyOneAddressWithOnePropertyWithoutTemperature() throws InterruptedException {
        // Given
        mockFirstAddress();

        List<Property> properties = new ArrayList<>();
        Property property = new Property(PropertyType.HOUSE, 350, 1);
        property.setId(1);
        properties.add(property);
        propertiesLiveData.setValue(properties);

        given(
            context.getString(PropertyType.HOUSE.getHumanReadableStringRes())
        ).willReturn("PropertyType.HOUSE");

        given(
            context.getString(R.string.unknown_temperature)
        ).willReturn("???");

        given(
            context.getString(
                R.string.template_address_type,
                "PropertyType.HOUSE",
                350,
                "101 rue des Dames, Paris",
                "???"
            )
        ).willReturn("PropertyType.HOUSE (de 350 m²), situé au 101 rue des Dames, Paris a comme température : ???");

        // When
        List<PropertyUiModel> result = LiveDataTestUtil.getOrAwaitValue(viewModel.getUiModelsLiveData());

        // Then
        assertEquals(1, result.size());
        // The most basic way to assert in a collection : get(0) after checking for size == 1
        assertEquals(
            "PropertyType.HOUSE (de 350 m²), situé au 101 rue des Dames, Paris a comme température : ???",
            result.get(0).getDescription()
        );
    }

    @Test
    public void shouldMapCorrectlyOneAddressWithTwoPropertiesWithoutTemperature() throws InterruptedException {
        // Given
        mockFirstAddress();

        List<Property> properties = new ArrayList<>();
        Property property = new Property(PropertyType.HOUSE, 350, 1);
        property.setId(1);
        properties.add(property);
        Property property2 = new Property(PropertyType.FLAT, 27, 1);
        property2.setId(2);
        properties.add(property2);
        propertiesLiveData.setValue(properties);

        given(
            context.getString(PropertyType.HOUSE.getHumanReadableStringRes())
        ).willReturn("PropertyType.HOUSE");
        given(
            context.getString(PropertyType.FLAT.getHumanReadableStringRes())
        ).willReturn("PropertyType.FLAT");

        given(
            context.getString(R.string.unknown_temperature)
        ).willReturn("???");

        given(
            context.getString(
                R.string.template_address_type,
                "PropertyType.HOUSE",
                350,
                "101 rue des Dames, Paris",
                "???"
            )
        ).willReturn("PropertyType.HOUSE (de 350 m²), situé au 101 rue des Dames, Paris a comme température : ???");

        given(
            context.getString(
                R.string.template_address_type,
                "PropertyType.FLAT",
                27,
                "101 rue des Dames, Paris",
                "???"
            )
        ).willReturn("PropertyType.FLAT (de 27 m²), situé au 101 rue des Dames, Paris a comme température : ???");

        // When
        List<PropertyUiModel> result = LiveDataTestUtil.getOrAwaitValue(viewModel.getUiModelsLiveData());

        // Then
        assertEquals(2, result.size());

        // A more powerfull way to assert in a collection : it works with any order
        assertThat(
            result,
            containsInAnyOrder(
                hasProperty("description", is("PropertyType.HOUSE (de 350 m²), situé au 101 rue des Dames, Paris a comme température : ???")),
                hasProperty("description", is("PropertyType.FLAT (de 27 m²), situé au 101 rue des Dames, Paris a comme température : ???"))
            )
        );

        // Another more powerfull way to assert in a collection : every item has this property
        assertThat(
            result,
            everyItem(
                HasPropertyWithValue.hasProperty(
                    "temperatureColor",
                    is(R.color.default_temperature)
                )
            )
        );
    }

    @Test
    public void shouldMapCorrectlyTwoAddressesWithTwoPropertiesWithoutTemperature() throws InterruptedException {
        // Given
        List<Address> addresses = new ArrayList<>();
        Address address = new Address("101 rue des Dames", "Paris");
        address.setId(1);
        addresses.add(address);
        Address address2 = new Address("31 rue Tronchet", "Paris");
        address2.setId(2);
        addresses.add(address2);
        addressesLiveData.setValue(addresses);

        List<Property> properties = new ArrayList<>();
        Property property = new Property(PropertyType.HOUSE, 350, 1);
        property.setId(1);
        properties.add(property);
        Property property2 = new Property(PropertyType.FLAT, 27, 2);
        property2.setId(2);
        properties.add(property2);
        propertiesLiveData.setValue(properties);

        given(
            context.getString(PropertyType.HOUSE.getHumanReadableStringRes())
        ).willReturn("PropertyType.HOUSE");
        given(
            context.getString(PropertyType.FLAT.getHumanReadableStringRes())
        ).willReturn("PropertyType.FLAT");

        given(
            context.getString(R.string.unknown_temperature)
        ).willReturn("???");

        given(
            context.getString(
                R.string.template_address_type,
                "PropertyType.HOUSE",
                350,
                "101 rue des Dames, Paris",
                "???"
            )
        ).willReturn("PropertyType.HOUSE (de 350 m²), situé au 101 rue des Dames, Paris a comme température : ???");

        given(
            context.getString(
                R.string.template_address_type,
                "PropertyType.FLAT",
                27,
                "31 rue Tronchet, Paris",
                "???"
            )
        ).willReturn("PropertyType.FLAT (de 27 m²), situé au 31 rue Tronchet, Paris, Paris a comme température : ???");

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

/*
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
    }*/


    private void mockFirstAddress() {
        List<Address> addresses = new ArrayList<>();
        Address address = new Address("101 rue des Dames", "Paris");
        address.setId(1);
        addresses.add(address);
        addressesLiveData.setValue(addresses);
    }
}