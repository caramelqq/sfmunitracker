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

    /**
     * Constructor:
     * Initialize repository
     * Initialize Livedata:
     *      locationsLiveData
     *      listOfCoordinates
     *      listOfStops
     *      routeTitle
     *      actionBarTitle
     *  Initialize draw object to draw vehicles on map
     *  Get sharedpreferences
     *  Load bus routes into DB if needed
     * @param application
     */
    public MapsViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        locationsLiveData = repository.getLocationsLiveData();
        listOfCoordinates = repository.getCoordinatesLiveData();
        listOfStops = repository.getStopsDataLiveData();
        routeTitle = repository.getRouteTitleLiveData();
        actionBarTitle = new MutableLiveData<String>();

        draw = new DrawVehiclesOnMap(application);

        pref = application.getSharedPreferences("routes", Context.MODE_PRIVATE);
        getAllBusRoutes();
    }

    /**
     * Loads all route information from nextbus
     * Get all bus routes and coordinates and associated stop data
     * Checks to see data was previously loaded to prevent subsequent requests to nextbus on future executions
     */
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
     * Route Coordinate Data
     * Get coordinate livedata
     */
    public MutableLiveData<List<Coordinates>> getListOfCoordinates() {
        return listOfCoordinates;
    }

    /**
     * Route Coordinate Data
     * Get route coordinate data from DB
     */
    public void loadRouteCoordinatesFromDB(String routeTag) {
        Log.d("drawroutes", "getting coords from db");
        repository.getRouteCoordinatesFromDB(routeTag);
    }

    /**
     * Route Coordinate Data
     * Draw polyline onto map
     */
    public void drawRoutePath(GoogleMap mMap, List<Coordinates> listOfCoordinates) {
        updateGoogleMap(mMap);
        MapsHelper.drawRoutePath(mMap, listOfCoordinates);
    }

    /**
     *  Stops data
     *  Query stop data from DB
     */
    public void loadStopsDataFromDB(String routeTag) {
        Log.d("stops", "getting stops data from db");
        repository.getStopsDataFromDB(routeTag);
    }

    /**
     *  Stops data
     *  Gets list of stops
     */
    public MutableLiveData<List<StopsWithDirection>> getListOfStops() {
        return listOfStops;
    }

    /**
     *  Stops data
     *  Draw points onto map
     */
    public void drawStopsOnMap(GoogleMap mMap, List<StopsWithDirection> stops) {
        updateGoogleMap(mMap);
        MapsHelper.drawStopsOnMap(mMap, stops);
    }

    /**
     * Vehicle Locations
     * Get vehicle locations
     */
    public MutableLiveData<VehicleLocations> getVehicleLocations() {
        return locationsLiveData;
    }

    /**
     * Vehicle Locations
     * Creates a thread that queries NextBus for bus data every 10 seconds
     */
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

    /**
     * Vehicle Locations
     * Draw vehicles on map
     */
    public void drawVehiclesOnMap(GoogleMap mMap, VehicleLocations vehicleLocations) {
        updateGoogleMap(mMap);
        draw.drawVehiclesOnMap(mMap, vehicleLocations);
    }

    /**
     * Route Data
     * Query route tag and title from DB
     */
    public void loadRouteTitles() {
        repository.getRouteTitleFromDB();
    }

    /**
     * Route Data
     * Get Route Tag and Title
     */
    public MutableLiveData<List<RouteTitle>> getRouteTitles() {
        return routeTitle;
    }

    /**
     * Route Data
     * Set RouteTag for map to draw
     */
    public void setCurrentRoute(String routeTag) {
        if(mMap != null) {
            mMap.clear();
        }
        draw.clearListOfVehicles();
        loadRouteCoordinatesFromDB(routeTag);
        loadStopsDataFromDB(routeTag);
        loadVehicleLocations(routeTag, String.valueOf(System.currentTimeMillis()-30000));
    }

    /**
     * Action bar
     * Set action bar title
     */
    public void setActionBarTitle(String title) {
        actionBarTitle.setValue(title);
    }

    /**
     * Action bar
     * Get action bar title
     */
    public MutableLiveData<String> getActionBarTitle() {
        return actionBarTitle;
    }

    /**
     * Update googlemap reference to the one drawn on screen
     */
    private void updateGoogleMap(GoogleMap mMap) {
        this.mMap = mMap;
    }
}
