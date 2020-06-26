package com.davidqq.sfmunitracker.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Stops.class, Coordinates.class, Directions.class, RouteTitle.class}, version = 1, exportSchema = false)
public abstract class RouteDatabase extends RoomDatabase {
    private static RouteDatabase dbInstance;

    public abstract StopsDao stopsDao();
    public abstract CoordinatesDao coordinatesDao();
    public abstract DirectionsDao directionsDao();
    public abstract RouteTitleDao routeTitleDao();

    public static synchronized RouteDatabase getInstance(Context context) {
        if(dbInstance == null) {
            dbInstance = Room.databaseBuilder(context.getApplicationContext(),
                    RouteDatabase.class, "route_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return dbInstance;
    }
}
