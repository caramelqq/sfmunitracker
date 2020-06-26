package com.davidqq.sfmunitracker.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "stops")
public class Stops {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String routeTag;
    private String stopTag;
    private String title;
    private String lat;
    private String lon;
    private String stopId;

    public Stops(String routeTag, String stopTag, String title, String lat, String lon, String stopId) {
        this.routeTag = routeTag;
        this.stopTag = stopTag;
        this.title = title;
        this.lat = lat;
        this.lon = lon;
        this.stopId = stopId;
    }

    public int getId() {
        return id;
    }

    public String getRouteTag() {
        return routeTag;
    }

    public String getStopTag() {
        return stopTag;
    }

    public String getTitle() {
        return title;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getStopId() {
        return stopId;
    }

    public void setId(int id) {
        this.id = id;
    }
}
