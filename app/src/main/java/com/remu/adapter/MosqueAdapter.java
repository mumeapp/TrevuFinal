package com.remu.adapter;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.remu.POJO.Distance;
import com.remu.POJO.PlaceModel;
import com.remu.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class MosqueAdapter extends RecyclerView.Adapter<MosqueAdapter.ViewHolder> {

    private ArrayList<PlaceModel> mDataset;
    private Application app;

    public MosqueAdapter(Application app, ArrayList<PlaceModel> mDataset) {
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
        holder.mosqueName.setText(mDataset.get(position).getPlaceName());
        holder.targetAddress.setText(mDataset.get(position).getPlaceAddress());
        holder.distance.setText(String.format("%.2f km", countDistance(mDataset.get(position).getPlaceLocation())));

        if (mDataset.get(position).getPlaceRating() == 0) {
            holder.rating.setText("-");
        } else {
            holder.rating.setText(String.format("%.1f", mDataset.get(position).getPlaceRating()));
        }

        holder.discoverbutton.setOnClickListener((view) -> {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("google.navigation:q=" +
                            mDataset.get(position).getPlaceLocation().latitude
                            + "," + mDataset.get(position).getPlaceLocation().longitude));
            app.startActivity(intent);
        });
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

        Button discoverbutton;
        TextView mosqueName, targetAddress, distance, rating;

        ViewHolder(View itemView) {
            super(itemView);
            discoverbutton = itemView.findViewById(R.id.discover_mosque);
            mosqueName = itemView.findViewById(R.id.MosqueName);
            targetAddress = itemView.findViewById(R.id.TargetAddress);
            distance = itemView.findViewById(R.id.distance);
            rating = itemView.findViewById(R.id.rating);
        }
    }
}
