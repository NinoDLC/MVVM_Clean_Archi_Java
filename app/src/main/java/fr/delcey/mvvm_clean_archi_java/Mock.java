package fr.delcey.mvvm_clean_archi_java;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.delcey.mvvm_clean_archi_java.data.database.PropertyType;
import fr.delcey.mvvm_clean_archi_java.data.database.model.Address;

import static fr.delcey.mvvm_clean_archi_java.data.database.PropertyType.BUILDING;
import static fr.delcey.mvvm_clean_archi_java.data.database.PropertyType.FLAT;
import static fr.delcey.mvvm_clean_archi_java.data.database.PropertyType.GARAGE;
import static fr.delcey.mvvm_clean_archi_java.data.database.PropertyType.HOUSE;
import static fr.delcey.mvvm_clean_archi_java.data.database.PropertyType.MANSION;

public final class Mock {
    private static final Random random = new Random();

    private Mock() {

    }

    private static List<String> paths = new ArrayList<String>() {{
        add("10880 Malibu Point");
        add("10236 Charing Cross Rd, Los Angeles, CA 90024, USA");
        add("221b Baker St, Marylebone, London NW1 6XE, UK");
        add("Hogwarts Castle, Highlands, Scotland, Great Britain");
        add("1600 Pennsylvania Avenue, Washington DC");
    }};

    private static List<String> cities = new ArrayList<String>() {{
        // RANDOM / FANBOY
        add("Malibu");
        add("Los Angeles");
        add("London");
        add("Scotland");
        add("Paris");

        // COLD !
        add("Ulaanbaatar");
        add("Astana");
        add("Moscow");
        add("Helsinki");
        add("Reykjavik");

        // HOT !
        add("Khartoum");
        add("Djibouti City");
        add("Niamey");
        add("Bangkok");

        // INCORRECT INPUT
        add("UNE VILLE INCONNUE");
    }};

    private static List<PropertyType> types = new ArrayList<PropertyType>() {{
        add(FLAT);
        add(MANSION);
        add(HOUSE);
        add(GARAGE);
        add(BUILDING);
    }};

    public static Address getRandomAddress() {
        return new Address(
            paths.get(random.nextInt(paths.size())),
            cities.get(random.nextInt(cities.size()))
        );
    }

    public static PropertyType getRandomPropertyType() {
        return types.get(random.nextInt(types.size()));
    }

    public static int getRandomSurfaceArea() {
        return random.nextInt(2_000);
    }
}
