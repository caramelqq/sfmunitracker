package com.davidqq.sfmunitracker.db;

public class StopsWithDirection {
    String routeTag;
    String stopTag;
    String routeTitle;
    String lat;
    String lon;
    String stopTitle;
    String direction;

    public StopsWithDirection(String routeTag, String stopTag, String routeTitle,
                              String lat, String lon, String stopTitle, String direction) {
        this.routeTag = routeTag;
        this.stopTag = stopTag;
        this.routeTitle = routeTitle;
        this.lat = lat;
        this.lon = lon;
        this.stopTitle = stopTitle;
        this.direction = direction;
    }

    public String getRouteTag() {
        return routeTag;
    }

    public String getStopTag() {
        return stopTag;
    }

    public String getRouteTitle() {
        return routeTitle;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getStopTitle() {
        return stopTitle;
    }

    public String getDirection() {
        return direction;
    }
}
