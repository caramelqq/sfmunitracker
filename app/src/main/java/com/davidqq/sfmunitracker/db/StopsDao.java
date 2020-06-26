package com.davidqq.sfmunitracker.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface StopsDao {
    @Insert
    void insertStop(Stops stops);

    @Query("SELECT * FROM stops WHERE routeTag == :routeTag")
    List<Stops> getStopsData(String routeTag);

    @Query("DELETE FROM stops")
    void deleteAllStops();

    @Query("SELECT stops.routeTag, stops.stopTag, stops.title, stops.lat, stops.lon, " +
            "directions.title, directions.direction " +
            "FROM stops " +
            "INNER JOIN directions " +
            "ON stops.stopTag == directions.stopTag " +
            "WHERE stops.routeTag == :routeTag " +
            "AND directions.routeTag == :routeTag")
    List<StopsWithDirection> getCombinedStopData(String routeTag);
}
