package com.davidqq.sfmunitracker;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.davidqq.sfmunitracker.db.RouteTitle;

import java.util.ArrayList;
import java.util.List;

public class RoutesListAdapter extends RecyclerView.Adapter<RoutesListAdapter.RouteListHolder> {
    public ArrayList<RouteTitle> routeTitleList = new ArrayList<RouteTitle>();
    private OnItemClickListener listener;

    public RoutesListAdapter(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    @NonNull
    @Override
    public RouteListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_list_item, parent, false);
        return new RouteListHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteListHolder holder, int position) {
        RouteTitle r = routeTitleList.get(position);
        holder.tvRouteTag.setText(r.getRouteTag());
        holder.tvRouteTitle.setText(r.getRouteTitle());
    }

    @Override
    public int getItemCount() {
        return routeTitleList.size();
    }

    class RouteListHolder extends RecyclerView.ViewHolder {
        public TextView tvRouteTag;
        public TextView tvRouteTitle;

        public RouteListHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            tvRouteTag = itemView.findViewById(R.id.route_list_tag);
            tvRouteTitle = itemView.findViewById(R.id.route_list_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Log.d("recyclerview", "onClick: " + getAdapterPosition());
                    if(listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getAdapterPosition(), routeTitleList);
                    }
                }
            });
        }
    }

    public void setRouteNames(List<RouteTitle> routeTitleList) {
        this.routeTitleList = (ArrayList<RouteTitle>) routeTitleList;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, List<RouteTitle> routeTitleList);
    }
}
