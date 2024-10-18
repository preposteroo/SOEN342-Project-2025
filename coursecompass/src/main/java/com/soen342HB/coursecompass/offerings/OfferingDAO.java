package com.soen342HB.coursecompass.offerings;

import com.soen342HB.coursecompass.core.IDAO;
import java.util.ArrayList;

public class OfferingDAO implements IDAO<Offering> {
    // temp DB
    public static ArrayList<Offering> db = new ArrayList<Offering>();

    @Override
    public void addToDb(Offering offering) {
        db.add(offering);
    }

    @Override
    public void removeFromDb(Offering offering) {
        db.remove(offering);
    }

    @Override
    public Offering fetchFromDb(String id) {
        for (Offering offering : db) {
            if (offering.getSchedule().getSpace().getSpaceName().equals(id)) {
                return offering;
            }
        }
        return null;
    }

    @Override
    public void updateDb(Offering offering) {
        for (Offering o : db) {
            if (o.getSchedule().getSpace().getSpaceName()
                    .equals(offering.getSchedule().getSpace().getSpaceName())) {
                o.setType(offering.getType());
                o.setSchedule(offering.getSchedule());
            }
        }
    }
}
