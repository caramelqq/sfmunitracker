package com.davidqq.sfmunitracker;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.davidqq.sfmunitracker.db.Coordinates;
import com.davidqq.sfmunitracker.db.CoordinatesDao;
import com.davidqq.sfmunitracker.db.Directions;
import com.davidqq.sfmunitracker.db.DirectionsDao;
import com.davidqq.sfmunitracker.db.RouteDatabase;
import com.davidqq.sfmunitracker.db.RouteTitle;
import com.davidqq.sfmunitracker.db.RouteTitleDao;
import com.davidqq.sfmunitracker.db.Stops;
import com.davidqq.sfmunitracker.db.StopsDao;
import com.davidqq.sfmunitracker.db.StopsWithDirection;

import com.davidqq.sfmunitracker.remoteapi.xmlelements.BusRoute;
import com.davidqq.sfmunitracker.remoteapi.xmlelements.Direction;
import com.davidqq.sfmunitracker.remoteapi.xmlelements.Path;
import com.davidqq.sfmunitracker.remoteapi.xmlelements.Point;
import com.davidqq.sfmunitracker.remoteapi.xmlelements.Route;
import com.davidqq.sfmunitracker.remoteapi.xmlelements.RouteName;
import com.davidqq.sfmunitracker.remoteapi.xmlelements.RouteNames;
import com.davidqq.sfmunitracker.remoteapi.xmlelements.Stop;
import com.davidqq.sfmunitracker.remoteapi.xmlelements.VehicleLocations;

import com.davidqq.sfmunitracker.remoteapi.XmlApi;
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Repository {
    private CoordinatesDao coordinatesDao;
    private StopsDao stopsDao;
    private DirectionsDao directionsDao;
    private RouteTitleDao routeTitleDao;
    private RouteDatabase routeDatabase;
    private Retrofit retrofit;
    private XmlApi xmlApi;

    private MutableLiveData<VehicleLocations> locationsLiveData;
    private MutableLiveData<List<Coordinates>> listOfCoordinates;
    private MutableLiveData<List<StopsWithDirection>> listOfStops;
    private MutableLiveData<List<RouteTitle>> routeTitles;

    public Repository(Application application) {
        routeDatabase = RouteDatabase.getInstance(application);

        stopsDao = routeDatabase.stopsDao();
        coordinatesDao = routeDatabase.coordinatesDao();
        directionsDao = routeDatabase.directionsDao();
        routeTitleDao = routeDatabase.routeTitleDao();

        locationsLiveData = new MutableLiveData<>();
        listOfCoordinates = new MutableLiveData<>();
        listOfStops = new MutableLiveData<>();
        routeTitles = new MutableLiveData<>();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://webservices.nextbus.com/")
                .addConverterFactory(TikXmlConverterFactory.create())
                .build();
        xmlApi = retrofit.create(XmlApi.class);
    }

    /**
     * Table of contents
     * COORDINATE FUNCTIONS
     * ROUTE/STOP FUNCTIONS
     * DIRECTIONS FUNCTIONS
     * ROUTE TITLE FUNCTIONS
     * NETWORK API FUNCTIONS
     * LiveData getter functions
     */

    /**
     * COORDINATE FUNCTIONS
     * Insert coordinate into DB
     */
    public void insertCoordinate(Coordinates coordinates) {
        new AsyncTask<Coordinates, Void, Void>() {
            @Override
            protected Void doInBackground(Coordinates... coordinate) {
                coordinatesDao.insertCoordinate(coordinate[0]);
                return null;
            }
        }.execute(coordinates);
    }

    /**
     * COORDINATE FUNCTIONS
     * Wrapper function to execute asynctask GetRouteCoordinatesFromDBHelper
     */
    public void getRouteCoordinatesFromDB(String routeTag) {
        Log.d("drawroutes", "getting coordinates for line: " + routeTag);
        GetRouteCoordinatesFromDBHelper t = new GetRouteCoordinatesFromDBHelper(coordinatesDao);
        t.execute(routeTag);
    }

    /**
     * COORDINATE FUNCTIONS
     * Queries route coordinates from DB
     */
    private class GetRouteCoordinatesFromDBHelper extends AsyncTask<String, Void, List<Coordinates>> {
        private CoordinatesDao coordinatesDao;

        public GetRouteCoordinatesFromDBHelper(CoordinatesDao coordinatesDao) {
            this.coordinatesDao = coordinatesDao;
        }

        @Override
        protected List<Coordinates> doInBackground(String... string) {
            return coordinatesDao.getRouteCoordinates(string[0]);
        }

        @Override
        protected void onPostExecute(List<Coordinates> coordinates) {
            Log.d("drawroutes", "Repository got coords, setting into listOfCoordinates");
            listOfCoordinates.setValue(coordinates);
        }
    }
    /**
     * COORDINATE FUNCTIONS
     * Deletes all coordinates in DB
     */
    public void deleteAllCoordinates() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                coordinatesDao.deleteAllRouteCoordinates();
                return null;
            }
        }.execute();
    }

    /**
     * ROUTE/STOP FUNCTIONS
     * Insert stop into DB
     */
    public void insertStop(Stops stops) {
        new AsyncTask<Stops, Void, Void>() {
            @Override
            protected Void doInBackground(Stops... stops) {
                stopsDao.insertStop(stops[0]);
                return null;
            }
        }.execute(stops);
    }

    /**
     * ROUTE/STOP FUNCTIONS
     * Wrapper function to execute asynctask GetStopsDataFromDBHelper
     */
    public void getStopsDataFromDB(String routeTag) {
        Log.d("stops", "repository - getstopsdatafromdb");
        GetStopsDataFromDBHelper t = new GetStopsDataFromDBHelper(stopsDao);
        t.execute(routeTag);
    }

    /**
     * ROUTE/STOP FUNCTIONS
     * Queries stop data from DB
     */
    private class GetStopsDataFromDBHelper extends AsyncTask<String, Void, List<StopsWithDirection>> {
        StopsDao stopsDao;

        public GetStopsDataFromDBHelper(StopsDao stopsDao) {
            this.stopsDao = stopsDao;
        }

        @Override
        protected List<StopsWithDirection> doInBackground(String... strings) {
            return stopsDao.getCombinedStopData(strings[0]);
        }

        @Override
        protected void onPostExecute(List<StopsWithDirection> stops) {
            listOfStops.setValue(stops);
        }
    }

    /**
     * ROUTE/STOP FUNCTIONS
     * Deletes all stop information from DB
     */
    public void deleteAllStops() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                stopsDao.deleteAllStops();
                return null;
            }
        }.execute();
    }

    /**
     * DIRECTIONS FUNCTIONS
     * Insert directions into DB
     */
    public void insertDirections(Directions directions) {
        new AsyncTask<Directions, Void, Void>() {
            @Override
            protected Void doInBackground(Directions... directions) {
                directionsDao.insertDirections(directions[0]);
                return null;
            }
        }.execute(directions);
    }

    /**
     * DIRECTIONS FUNCTIONS
     * Delete all directions from DB
     */
    public void deleteAllDirections() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                directionsDao.deleteAllDirections();
                return null;
            }
        }.execute();
    }

    /**
     * ROUTE TITLE FUNCTIONS
     * Insert route titles into DB
     */
    public void insertRouteTitle(RouteTitle routeTitle) {
        new AsyncTask<RouteTitle, Void, Void>() {
            @Override
            protected Void doInBackground(RouteTitle... routeTitle) {
                routeTitleDao.insertRouteTitle(routeTitle[0]);
                return null;
            }
        }.execute(routeTitle);
    }

    /**
     * ROUTE TITLE FUNCTIONS
     * Wrapper to execute asynctask GetRouteTitleFromDBHelper
     */
    public void getRouteTitleFromDB() {
        Log.d("routeTitle", "repository - getRouteTitleFromDB");
        GetRouteTitleFromDBHelper t = new GetRouteTitleFromDBHelper(routeTitleDao);
        t.execute();
    }
    /**
     * ROUTE TITLE FUNCTIONS
     * Queries database for route titles
     */
    private class GetRouteTitleFromDBHelper extends AsyncTask<Void, Void, List<RouteTitle>> {
        RouteTitleDao routeTitleDao;

        public GetRouteTitleFromDBHelper(RouteTitleDao routeTitleDao) {
            this.routeTitleDao = routeTitleDao;
        }

        @Override
        protected List<RouteTitle> doInBackground(Void... voids) {
            return routeTitleDao.getAllRouteTitles();
        }

        @Override
        protected void onPostExecute(List<RouteTitle> routes) {
            routeTitles.setValue(routes);
        }
    }

    /**
     * ROUTE TITLE FUNCTIONS
     * Delete all route title information
     */
    public void deleteAllRouteTitles() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                routeTitleDao.deleteAllRouteTitles();
                return null;
            }
        }.execute();
    }

    /**
     * NETWORK API FUNCTIONS
     * Gets vehicle locations from nextbus api
     */
    public void getVehicleLocations(String routeTag, String time) {
        Call<VehicleLocations> call = xmlApi.getVehicleLocation(routeTag, time);

        call.enqueue(new Callback<VehicleLocations>() {
            @Override
            public void onResponse(Call<VehicleLocations> call, Response<VehicleLocations> response) {
                Log.d("getVehicleLocations ", "Repository getVehicleLocations onResponse: Success " + response.body());
                locationsLiveData.setValue(response.body());
                Log.d("getVehicleLocations ", "locationsLiveData set");
            }

            @Override
            public void onFailure(Call<VehicleLocations> call, Throwable t) {
                Log.e("getVehicleLocations", "Repository getVehicleLocations onFailure: Unable to retrieve xml: " + t.getMessage());
            }
        });
    }

    /**
     * NETWORK API FUNCTIONS
     * Gets route name information from nextbus api
     */
    public void getRouteNames() {
        Call<RouteNames> call = xmlApi.getRouteNames();

        call.enqueue(new Callback<RouteNames>() {
            @Override
            public void onResponse(Call<RouteNames> call, Response<RouteNames> response) {
                if(response.isSuccessful()) {
                    RouteNames r = response.body();

                    deleteAllRouteTitles();

                    for(RouteName routeName : r.routes) {
                        insertRouteTitle(new RouteTitle(routeName.routeTag, routeName.routeTitle));
                    }
                }
            }

            @Override
            public void onFailure(Call<RouteNames> call, Throwable t) {
                Log.e("getRouteNames", "failed to get response : getRouteNames");
            }
        });
    }

    /**
     * NETWORK API FUNCTIONS
     * Get all bus route coordinates and information from nextbus api
     * On successful query, dump out old data and insert new data into DB
     */
    public void getAllBusRoutes() {
        Call<BusRoute> call = xmlApi.getAllRoutes();

        call.enqueue(new Callback<BusRoute>() {
            @Override
            public void onResponse(Call<BusRoute> call, Response<BusRoute> response) {
                if(response.isSuccessful()) {
                    Log.d("getallbusroutes", "onResponse success");

                    // Clear out route/direction/coordinates tables
                    deleteAllCoordinates();
                    deleteAllStops();
                    deleteAllDirections();

                    BusRoute busRoutes = response.body();

                    // Insert into route
                    for(Route r : busRoutes.route) {
                        for(Stop s : r.stop) {
                            insertStop(new Stops(r.tag,
                                    s.tag,
                                    s.title,
                                    s.lat,
                                    s.lon,
                                    s.stopId
                            ));
                        }
                    }

                    // Insert into directions
                    for(Route r : busRoutes.route) {
                        for(Direction d : r.direction) {
                            for(Stop s : d.stop)
                            insertDirections(new Directions(r.tag,
                                    s.tag,
                                    d.title,
                                    d.name
                            ));
                        }
                    }

                    // Insert into coordinates
                    for (Route r : busRoutes.route) {
                        int sectionCounter = 0;
                        for (Path p : r.path) {
                            for(Point point : p.point) {
                                insertCoordinate(new Coordinates(r.tag,
                                        point.lat,
                                        point.lon,
                                        sectionCounter
                                    ));
                            }
                            sectionCounter++;
                        }
                        Log.d("section", Integer.toString(sectionCounter));
                    }
                    Log.d("getallbusroutes", "Finished adding data to db.");
                } else {
                    Log.e("getallbusroutes", "onResponse: HTTP error getting route data?");
                }
            }

            @Override
            public void onFailure(Call<BusRoute> call, Throwable t) {
                Log.e("getallbusroutes", "onFailure: Unable to retrieve xml: " + t.getMessage());
            }
        });
    }

    /**
     * LiveData getter functions
     */

    public MutableLiveData<VehicleLocations> getLocationsLiveData() {
        return locationsLiveData;
    }

    public MutableLiveData<List<Coordinates>> getCoordinatesLiveData() {
        return listOfCoordinates;
    }

    public MutableLiveData<List<StopsWithDirection>> getStopsDataLiveData() {
        return listOfStops;
    }

    public MutableLiveData<List<RouteTitle>> getRouteTitleLiveData() {
        return routeTitles;
    }
}
