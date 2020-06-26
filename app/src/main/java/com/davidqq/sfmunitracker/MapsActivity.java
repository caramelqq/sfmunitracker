package com.davidqq.sfmunitracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.davidqq.sfmunitracker.db.Coordinates;
import com.davidqq.sfmunitracker.db.StopsWithDirection;

import com.davidqq.sfmunitracker.remoteapi.xmlelements.VehicleLocations;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private MapsViewModel mapsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Mainactivity", "Oncreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Get viewmodel
        mapsViewModel = new ViewModelProvider(this).get(MapsViewModel.class);

        // Observe changes to the action bar's title
        mapsViewModel.getActionBarTitle().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s == null) {
                    s = getResources().getString(R.string.title_activity_maps);
                }
                getSupportActionBar().setTitle(s);
            }
        });
//
//        ActionBar bar = getSupportActionBar();
//        bar.setBackgroundDrawable(new ColorDrawable(0xffa8ffb8));
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("Mainactivity", "onMapReady");
        mMap = googleMap;

        // Move camera to SF
        LatLng sanFranciscoMarker = new LatLng(37.740127,-122.435803);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sanFranciscoMarker, (float) 11.7));

        // Draw vehicles onto map
        mapsViewModel.getVehicleLocations().observe(this, new Observer<VehicleLocations>() {
            @Override
            public void onChanged(VehicleLocations vehicleLocations) {
                Log.d("getVehicleLocations ", "Observer vehicleLocations change");
                mapsViewModel.drawVehiclesOnMap(mMap, vehicleLocations);
            }
        });

        // Draw Route Polyline onto map
        mapsViewModel.getListOfCoordinates().observe(this, new Observer<List<Coordinates>>() {
            @Override
            public void onChanged(List<Coordinates> coordinates) {
                Log.d("drawroutes", "Observer getListOfCoordinates change");
                mapsViewModel.drawRoutePath(mMap, coordinates);
            }
        });

        // Draw Stops onto map
        mapsViewModel.getListOfStops().observe(this, new Observer<List<StopsWithDirection>>() {
            @Override
            public void onChanged(List<StopsWithDirection> stops) {
                Log.d("drawstops", "Observer getListOfStops change");
                mapsViewModel.drawStopsOnMap(mMap, stops);
            }
        });
    }

    /**
     * Inflate menu
     * @param menu
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.primary_menu, menu);
        return true;
    }

    /**
     * Handle menu options selections
     * @param item MenuItem
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch(item.getItemId()) {
            case R.id.item_map:
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    FragmentManager.BackStackEntry first = fragmentManager.getBackStackEntryAt(0);
                    fragmentManager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                return true;

            case R.id.item_route_list:
                Log.d("routeslist", "selected routeslist option");
                if(fragmentManager.getFragments().size() > 1) {
                    return true;
                }

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                RoutesList routesList = new RoutesList();
                fragmentTransaction.add(R.id.fragment_holder, routesList);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.show(routesList);
                fragmentTransaction.commit();
                Log.d("routeslist", "finished routeslist option");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
