package com.soen342HB.coursecompass.spaces;

import java.util.ArrayList;
import java.util.List;
import com.soen342HB.coursecompass.core.BaseDAO;

public class SpaceDAO extends BaseDAO<Space> {
    // temp DB
    public static List<Space> db = new ArrayList<Space>();

    @Override
    public void addtoDb(Space space) {
        db.add(space);
    }

    @Override
    public void removeFromDb(Space space) {
        db.remove(space);
    }

    @Override
    public Space fetchFromDb(String id) {
        for (Space space : db) {
            if (space.getSpaceName().equals(id)) {
                return space;
            }
        }
        return null;
    }

    public Space[] fetchAllFromDb() {
        Space[] spaces = new Space[db.size()];
        for (int i = 0; i < db.size(); i++) {
            spaces[i] = db.get(i);
        }
        return spaces;
    }

    @Override
    public void updateDb(Space space) {}

}
