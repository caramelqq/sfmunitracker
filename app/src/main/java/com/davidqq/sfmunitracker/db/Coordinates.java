package com.davidqq.sfmunitracker.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "coordinates")
public class Coordinates {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String routeTag;
    private String lat;
    private String lon;
    private int section;

    public Coordinates(String routeTag, String lat, String lon, int section) {
        this.routeTag = routeTag;
        this.lat = lat;
        this.lon = lon;
        this.section = section;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRouteTag() {
        return routeTag;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public int getSection() {
        return section;
    }
}
