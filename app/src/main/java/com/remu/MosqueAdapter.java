package com.remu;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.remu.POJO.Distance;
import com.remu.POJO.PlaceModel;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

class MosqueAdapter extends RecyclerView.Adapter<MosqueAdapter.ViewHolder> {

    private ArrayList<PlaceModel> mDataset;
    private Application app;

    MosqueAdapter(Application app, ArrayList<PlaceModel> mDataset) {
        this.app = app;
        this.mDataset = mDataset;
    }

    @NonNull
    @Override
    public MosqueAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_mosque, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MosqueAdapter.ViewHolder holder, int position) {
        try {
            holder.mosqueName.setText(mDataset.get(position).getPlaceName());
            holder.targetAddress.setText(mDataset.get(position).getPlaceAddress());
            holder.distance.setText(String.format("%.2f km", countDistance(mDataset.get(position).getPlaceLocation())));
            holder.rating.setText(String.format("%.1f", mDataset.get(position).getPlaceRating()));
        } catch (NullPointerException e) {

        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private double countDistance(LatLng latLng) {
        LatLng currentLatLng = new LatLng(Double.parseDouble(app.getSharedPreferences("location", MODE_PRIVATE).getString("Latitude", null)), Double.parseDouble(app.getSharedPreferences("location", MODE_PRIVATE).getString("Longitude", null)));

        return Distance.distance(currentLatLng.latitude, latLng.latitude, currentLatLng.longitude, latLng.longitude);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mosqueName, targetAddress, distance, rating;

        ViewHolder(View itemView) {
            super(itemView);
            mosqueName = itemView.findViewById(R.id.MosqueName);
            targetAddress = itemView.findViewById(R.id.TargetAddress);
            distance = itemView.findViewById(R.id.distance);
            rating = itemView.findViewById(R.id.rating);
        }
    }
}
