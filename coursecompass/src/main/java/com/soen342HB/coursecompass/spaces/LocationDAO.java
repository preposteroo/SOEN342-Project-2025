package com.soen342HB.coursecompass.spaces;

import java.util.ArrayList;
import java.util.List;
import com.soen342HB.coursecompass.core.BaseDAO;

public class LocationDAO extends BaseDAO<Location> {
    // temp DB
    public static List<Location> db = new ArrayList<Location>();

    @Override
    public void addtoDb(Location location) {
        db.add(location);
    }

    @Override
    public void removeFromDb(Location location) {
        db.remove(location);
    }

    @Override
    public Location fetchFromDb(String id) {
        for (Location location : db) {
            if (location.getLocationName().equals(id)) {
                return location;
            }
        }
        return null;
    }

    public Location[] fetchAllFromDb() {
        Location[] locations = new Location[db.size()];
        for (int i = 0; i < db.size(); i++) {
            locations[i] = db.get(i);
        }
        return locations;
    }

    @Override
    public void updateDb(Location location) {
        for (Location l : db) {
            if (l.getLocationName().equals(location.getLocationName())) {
                l.setSpaces(location.getSpaces());
            }
        }
    }

}
