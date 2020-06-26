package com.davidqq.sfmunitracker.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "directions")
public class Directions {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String routeTag;
    private String stopTag;
    private String title;
    private String direction;


    public Directions(String routeTag, String stopTag, String title, String direction) {
        this.stopTag = stopTag;
        this.routeTag = routeTag;
        this.title = title;
        this.direction = direction;
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

    public String getStopTag() {
        return stopTag;
    }

    public String getTitle() {
        return title;
    }

    public String getDirection() {
        return direction;
    }
}
