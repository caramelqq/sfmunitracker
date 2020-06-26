package com.davidqq.sfmunitracker.drawutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.Pair;

import androidx.core.content.ContextCompat;

import com.davidqq.sfmunitracker.R;
import com.davidqq.sfmunitracker.remoteapi.xmlelements.Vehicle;
import com.davidqq.sfmunitracker.remoteapi.xmlelements.VehicleLocations;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

public class DrawVehiclesOnMap {
    private ConcurrentHashMap<String, Pair<Marker, Instant>> m;
    private GoogleMap mMap;
    private Context context;
    private BitmapDescriptor inboundArrow;
    private BitmapDescriptor outboundArrow;
    private int vehicleTimeout = 5;

    public DrawVehiclesOnMap(Context context) {
        this.context = context;
        m = new ConcurrentHashMap<String, Pair<Marker, Instant>>();
        inboundArrow = bitmapDescriptorFromVector(R.drawable.inbound_arrow_24dp);
        outboundArrow = bitmapDescriptorFromVector(R.drawable.outbound_arrow_24dp);
    }

    // Draw vehicles onto map
    public void drawVehiclesOnMap(GoogleMap mMap, VehicleLocations vehicleLocations) {
        // If no vehicles are found, stop execution
        if (vehicleLocations.vehicle == null) {
            Log.d("getVehicleLocations", "drawVehiclesOnMap : vehicleLocations.vehicle is null");
            return;
        }

        // Check if instance of (GoogleMap) mMap is the same as what's on the screen (Check for configuration changes)
        if(this.mMap != mMap) {
            clearListOfVehicles();
            this.mMap = mMap;
        }

        Log.d("getVehicleLocations", "drawing vehicles now");
        // For each vehicle
        for (Vehicle v : vehicleLocations.vehicle) {
            // If vehicle does not exist in hashmap, then add it as a tuple <VehicleID, <Marker,Instant>>
            if (m.get(v.id) == null) {
                m.put(v.id, new Pair<Marker, Instant>(mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(v.lat), Double.parseDouble(v.lon)))
                        .title(v.routeTag)
                        .icon(getArrowDirection(v.dirTag))
                        .rotation(Float.parseFloat(v.heading) + 275)
                        .anchor((float) 0.5, (float) 0.5)
                        .zIndex((float) 102)), Instant.now()));
            } else {
                // Otherwise update the position and heading of the vehicle
                m.get(v.id).first.setPosition(new LatLng(Double.parseDouble(v.lat), Double.parseDouble(v.lon)));
                m.get(v.id).first.setIcon(getArrowDirection(v.dirTag));
                m.get(v.id).first.setRotation(Float.parseFloat(v.heading) + 275);
            }
        }
    }

    // Empty out the hashmap of vehicles
    public void clearListOfVehicles() {
        m.clear();
    }

    // Remove vehicles if no update for the vehicle has been received for the past 5 minutes
    public void removeOldVehicles() {
        for(String key : m.keySet()) {
            // If a key has not been updated for 5+ minutes, remove from googlemap and remove from hashmap.
            if(Duration.between(m.get(key).second, Instant.now()).toMinutes() > vehicleTimeout) {
                // remove marker from the googlemap
                m.get(key).first.remove();
                // remove key associated with the above marker from hashmap
                m.remove(key);
            }
        }
    }

    // Determine if vehicle is inbound or outbound
    private BitmapDescriptor getArrowDirection(String direction) {
        // Get the direction based on the 5th character of the direction field
        if(direction == null || direction.charAt(5) == 'I') {
            return inboundArrow;
        } else {
            return outboundArrow;
        }
    }

    // Convert vector to bmp since googlemap markers can only be bmp
    private BitmapDescriptor bitmapDescriptorFromVector(int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
