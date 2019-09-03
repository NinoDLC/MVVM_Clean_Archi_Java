package fr.delcey.mvvm_clean_archi_java;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class Mock {
    private Mock() {

    }

    private static List<String> addresses = new ArrayList<String>() {{
        add("10880 Malibu Point");
        add("10236 Charing Cross Rd, Los Angeles, CA 90024, USA");
        add("221b Baker St, Marylebone, London NW1 6XE, UK");
        add("Hogwarts Castle, Highlands, Scotland, Great Britain");
        add("1600 Pennsylvania Avenue, Washington DC");
    }};

    private static List<String> types = new ArrayList<String>() {{
        add("FLAT");
        add("MANSION");
        add("HOUSE");
        add("GARAGE");
        add("BUILDING");
    }};

    public static String getAddress() {
        return addresses.get(new Random().nextInt(addresses.size()));
    }


    public static String getType() {
        return types.get(new Random().nextInt(types.size()));
    }
}
