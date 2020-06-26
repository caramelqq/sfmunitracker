package com.davidqq.sfmunitracker.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CoordinatesDao {
    @Insert
    void insertCoordinate(Coordinates coordinates);

    @Query("SELECT * FROM coordinates WHERE routeTag == :routeTag")
    List<Coordinates> getRouteCoordinates(String routeTag);

    @Query("DELETE FROM coordinates")
    void deleteAllRouteCoordinates();
}
