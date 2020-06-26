package com.davidqq.sfmunitracker.remoteapi;

import com.davidqq.sfmunitracker.remoteapi.xmlelements.BusRoute;
import com.davidqq.sfmunitracker.remoteapi.xmlelements.RouteNames;
import com.davidqq.sfmunitracker.remoteapi.xmlelements.VehicleLocations;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface XmlApi {
    @GET("service/publicXMLFeed?command=routeConfig&a=sf-muni")
    Call<BusRoute> getAllRoutes();

    @GET("service/publicXMLFeed?command=vehicleLocations&a=sf-muni")
    Call<VehicleLocations> getVehicleLocation(@Query("r") String routeTag, @Query("t") String timeInMilliseconds);

    @GET("service/publicXMLFeed?command=routeList&a=sf-muni")
    Call<RouteNames> getRouteNames();
}
