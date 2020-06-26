package com.davidqq.sfmunitracker;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.davidqq.sfmunitracker.db.RouteTitle;

import java.util.List;

public class RoutesList extends Fragment implements RoutesListAdapter.OnItemClickListener {
    private MapsViewModel mapsViewModel;
    private RecyclerView recyclerView;
    private RoutesListAdapter routesListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("routeslist", "inflating routeslist");
        View v  = inflater.inflate(R.layout.route_list_fragment, container, false);
        recyclerView = v.findViewById(R.id.route_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        routesListAdapter = new RoutesListAdapter(this);
        recyclerView.setAdapter(routesListAdapter);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("routeslist", "getting route titles");
        mapsViewModel = new ViewModelProvider(requireActivity()).get(MapsViewModel.class);
        mapsViewModel.loadRouteTitles();

        mapsViewModel.getRouteTitles().observe(getViewLifecycleOwner(), new Observer<List<RouteTitle>>() {
            @Override
            public void onChanged(List<RouteTitle> routeTitle) {
                routesListAdapter.setRouteNames(routeTitle);
                // Tell adapter that dataset has changed and to load the new data
                routesListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onItemClick(int position, List<RouteTitle> routeTitleList) {
        if(position >= routeTitleList.size()) {
            return;
        }
        Log.d("recyclerview", "onItemClick: " + routeTitleList.get(position).getRouteTag());
        mapsViewModel.setCurrentRoute(routeTitleList.get(position).getRouteTag());
        Toast.makeText(getActivity(), "Selected route " + routeTitleList.get(position).getRouteTag(), Toast.LENGTH_SHORT).show();
        mapsViewModel.setActionBarTitle(routeTitleList.get(position).getRouteTitle());
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }
}
