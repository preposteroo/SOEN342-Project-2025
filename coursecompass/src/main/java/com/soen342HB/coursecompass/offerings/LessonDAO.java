package com.soen342HB.coursecompass.offerings;

import java.util.ArrayList;
import java.util.List;
import com.soen342HB.coursecompass.core.IDAO;

public class LessonDAO implements IDAO<Lesson> {
    // TEMPORARY DB
    List<Lesson> lessons = new ArrayList<Lesson>();

    public LessonDAO() {}

    public void addtoDb(Lesson lesson) {
        lessons.add(lesson);
    }

    public Lesson fetchFromDb(String id) {
        for (Lesson lesson : lessons) {
            if (lesson.getId().equals(id)) {
                return lesson;
            }
        }
        return null;
    }

    public Lesson[] fetchAllFromDb() {
        return lessons.toArray(new Lesson[lessons.size()]);
    }

    public void updateDb(Lesson lesson) {
        for (Lesson l : lessons) {
            if (l.getId().equals(lesson.getId())) {
                l.setInstructor(lesson.getInstructor());
                l.setOffering(lesson.getOffering());
            }
        }
    }

    public void removeFromDb(Lesson lesson) {
        lessons.remove(lesson);
    }

}
