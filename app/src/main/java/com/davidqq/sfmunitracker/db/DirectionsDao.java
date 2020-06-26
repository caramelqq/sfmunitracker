package com.davidqq.sfmunitracker.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DirectionsDao {
    @Insert
    void insertDirections(Directions directions);

    @Query("SELECT * FROM directions WHERE routeTag == :routeTag")
    List<Directions> getDirections(String routeTag);

    @Query("DELETE FROM directions")
    void deleteAllDirections();
}
