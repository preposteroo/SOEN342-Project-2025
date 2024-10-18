package com.soen342HB.coursecompass.core;

public interface IDAO<T> {
    public void addtoDb(T t);

    public void removeFromDb(T t);

    public T fetchFromDb(String id);

    public void updateDb(T t);
}
