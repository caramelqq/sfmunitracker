package com.davidqq.sfmunitracker.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "routes")
public class RouteTitle {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String routeTag;
    private String routeTitle;

    public RouteTitle(String routeTag, String routeTitle) {
        this.routeTag = routeTag;
        this.routeTitle = routeTitle;
    }

    public String getRouteTag() {
        return routeTag;
    }

    public String getRouteTitle() {
        return routeTitle;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
}
