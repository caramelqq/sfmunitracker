package com.davidqq.sfmunitracker.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RouteTitleDao {
    @Insert
    void insertRouteTitle(RouteTitle routeTitle);

    @Query("SELECT * FROM routes")
    List<RouteTitle> getAllRouteTitles();

    @Query("DELETE FROM routes")
    void deleteAllRouteTitles();
}
