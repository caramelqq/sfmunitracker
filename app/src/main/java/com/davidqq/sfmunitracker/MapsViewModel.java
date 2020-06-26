package com.davidqq.sfmunitracker;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.davidqq.sfmunitracker.db.Coordinates;
import com.davidqq.sfmunitracker.db.RouteTitle;
import com.davidqq.sfmunitracker.db.StopsWithDirection;
import com.davidqq.sfmunitracker.drawutils.DrawVehiclesOnMap;
import com.davidqq.sfmunitracker.drawutils.MapsHelper;
import com.davidqq.sfmunitracker.remoteapi.xmlelements.VehicleLocations;
import com.google.android.gms.maps.GoogleMap;

import java.util.List;

public class MapsViewModel extends AndroidViewModel {
    private MutableLiveData<VehicleLocations> locationsLiveData;
    private MutableLiveData<List<Coordinates>> listOfCoordinates;
    private MutableLiveData<List<StopsWithDirection>> listOfStops;
    private MutableLiveData<List<RouteTitle>> routeTitle;
    private MutableLiveData<String> actionBarTitle;

    private Repository repository;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private DrawVehiclesOnMap draw;
    private Thread t;
    private GoogleMap mMap;

    public MapsViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        locationsLiveData = repository.getLocationsLiveData();
        listOfCoordinates = repository.getCoordinatesLiveData();
        listOfStops = repository.getStopsDataLiveData();
        routeTitle = repository.getRouteTitleLiveData();
        actionBarTitle = new MutableLiveData<String>();

        pref = application.getSharedPreferences("routes", Context.MODE_PRIVATE);
        draw = new DrawVehiclesOnMap(application);
        getAllBusRoutes();
    }

    // Get all bus routes and coordinates and associated stop data
    private void getAllBusRoutes() {
        editor = pref.edit();
        if(!pref.getBoolean("exists", false)) {
            repository.getAllBusRoutes();
            repository.getRouteNames();
            editor.putBoolean("exists", true);

            Log.d("getallbusroutes", "got new bus route info");
        }
        editor.apply();
        Log.d("getallbusroutes", "exiting getallbusroutes");
    }

    /**
     * Get Coordinate data from DB
     * Draw polyline onto map
     */

    // Return route coordinate LiveData
    public MutableLiveData<List<Coordinates>> getListOfCoordinates() {
        return listOfCoordinates;
    }

    // Load route coordinates into listOfCoordinates
    public void loadRouteCoordinatesFromDB(String routeTag) {
        Log.d("drawroutes", "getting coords from db");
        repository.getRouteCoordinatesFromDB(routeTag);
    }

    // Draw route onto map
    public void drawRoutePath(GoogleMap mMap, List<Coordinates> listOfCoordinates) {
        this.mMap = mMap;
        MapsHelper.drawRoutePath(mMap, listOfCoordinates);
    }


    /**
     *  Get Stops data from DB
     *  Draw points onto map
     */

    public void loadStopsDataFromDB(String routeTag) {
        Log.d("stops", "getting stops data from db");
        repository.getStopsDataFromDB(routeTag);
    }

    public MutableLiveData<List<StopsWithDirection>> getListOfStops() {
        return listOfStops;
    }

    public void drawStopsOnMap(GoogleMap mMap, List<StopsWithDirection> stops) {
        this.mMap = mMap;
        MapsHelper.drawStopsOnMap(mMap, stops);
    }

    /**
     *  Get vehicle locations
     *  Draw vehicles on map
     */

    // Return vehicle location LiveData
    public MutableLiveData<VehicleLocations> getVehicleLocations() {
        return locationsLiveData;
    }

    // Creates a thread which gets new vehicle location data every 10 seconds
    public void loadVehicleLocations(String routeTag, String time) {
        draw.clearListOfVehicles();

        if(t != null) {
            t.interrupt();
        }

        t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(true) {
                        repository.getVehicleLocations(routeTag, time);
                        draw.removeOldVehicles();
                        Log.d("Thread t", "Got new vehicle locations");
                        Thread.sleep(10000);
                    }
                } catch (InterruptedException e) {
                    Log.d("Thread t", "Interrupted thread getting routeTag " + routeTag);
                }
            }
        });
        t.start();
    }

    public void drawVehiclesOnMap(GoogleMap mMap, VehicleLocations vehicleLocations) {
        this.mMap = mMap;
        draw.drawVehiclesOnMap(mMap, vehicleLocations);
    }

    /**
     * Get Route Tag and Title
     */
    public void loadRouteTitles() {
        repository.getRouteTitleFromDB();
    }

    public MutableLiveData<List<RouteTitle>> getRouteTitles() {
        return routeTitle;
    }

    /**
     * Set RouteTag for map to draw
     */
    public void setCurrentRoute(String routeTag) {
        if(mMap != null) {
            mMap.clear();
        }
        draw.clearListOfVehicles();
        loadRouteCoordinatesFromDB(routeTag);
        loadStopsDataFromDB(routeTag);
        loadVehicleLocations(routeTag, String.valueOf(System.currentTimeMillis()-10000));
    }

    /**
     * Set action bar title
     */
    public void setActionBarTitle(String title) {
        actionBarTitle.setValue(title);
    }

    public MutableLiveData<String> getActionBarTitle() {
        return actionBarTitle;
    }
}
