package com.soen342HB.coursecompass.spaces;

import java.util.ArrayList;
import java.util.List;
import com.soen342HB.coursecompass.core.IDAO;

public class CityDAO implements IDAO<City> {
    // temp DB
    public static List<City> db = new ArrayList<City>();

    @Override
    public void addtoDb(City city) {
        db.add(city);
    }

    @Override
    public void removeFromDb(City city) {
        db.remove(city);
    }

    @Override
    public City fetchFromDb(String id) {
        for (City city : db) {
            if (city.getCityName().equals(id)) {
                return city;
            }
        }
        return null;
    }

    public City[] fetchAllFromDb() {
        City[] cities = new City[db.size()];
        for (int i = 0; i < db.size(); i++) {
            cities[i] = db.get(i);
        }
        return cities;
    }

    @Override
    public void updateDb(City city) {
        for (City c : db) {
            if (c.getCityName().equals(city.getCityName())) {
                c.setLocations(city.getLocations());
            }
        }
    }
}
