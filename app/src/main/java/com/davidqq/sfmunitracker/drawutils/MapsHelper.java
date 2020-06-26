package com.davidqq.sfmunitracker.drawutils;

import android.graphics.Color;

import android.util.Log;

import com.davidqq.sfmunitracker.db.Coordinates;
import com.davidqq.sfmunitracker.db.StopsWithDirection;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsHelper {
    // Draw route onto map (polyline) with the provided list of coordinates
    public static void drawRoutePath(GoogleMap mMap, List<Coordinates> listOfCoordinates) {
        //TODO - ArrayList<ArrayList<Coordinates>> - polyline for each List<Coordinates>
        // Each Coordinate is tagged with a number indicating which segment it belongs to
        // Initialize a List of List of LatLng

        if(listOfCoordinates == null || listOfCoordinates.size() <= 0) {
            Log.d("drawroutes", "No coordinates were provided to draw");
            return;
        }

        int segmentCount = listOfCoordinates.get(listOfCoordinates.size() - 1).getSection();
        ArrayList<ArrayList<LatLng>> listOfSegments = new ArrayList<ArrayList<LatLng>>();
        for(int i = 0; i <= segmentCount; ++i) {
            listOfSegments.add(new ArrayList<LatLng>());
        }

        // For each coordinate, pull the coordinates from them and put them into a new LatLng
        // object and append to the segment it belongs to in listOfSegments
        for(Coordinates coordinate : listOfCoordinates) {
            listOfSegments.get(coordinate.getSection())
                    .add(new LatLng(Double.parseDouble(coordinate.getLat()),
                                    Double.parseDouble(coordinate.getLon())));
        }

        // For each List (route segment) in listOfSegments, generate a polyline
        for(ArrayList<LatLng> segment : listOfSegments) {
            mMap.addPolyline(new PolylineOptions()
                    .addAll(segment)
                    .width((float) 10)
                    .color(0xff00b524)
                    .zIndex((float) 100));
        }
    }

    //Draw each stop on the route as a point
    public static void drawStopsOnMap(GoogleMap mMap, List<StopsWithDirection> stops) {
        if(stops == null) {
            Log.d("drawstops", "No stops were given");
            return;
        }

        Log.d("drawstops", "drawing stops on map");
        for(StopsWithDirection stop : stops) {
            mMap.addCircle(new CircleOptions()
                .center(new LatLng(Double.parseDouble(stop.getLat()), Double.parseDouble(stop.getLon())))
                .radius(30)
                .strokeWidth((float) 2)
                .strokeColor(Color.BLACK)
                .fillColor(setCircleColor(stop))
                .zIndex((float) 101));
        }
    }

    private static int setCircleColor(StopsWithDirection stop) {
        if(stop.getDirection().equals("Outbound")) {
            return 0xFFFFBFF0;
        } else {
            return 0xFFC2DCFF;
        }
    }
}
