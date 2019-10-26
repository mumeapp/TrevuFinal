package com.remu;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.model.LatLng;
import com.remu.POJO.Distance;
import com.remu.POJO.Mosque;

import org.json.JSONException;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

class MosqueAdapter extends RecyclerView.Adapter<MosqueAdapter.ViewHolder> {

    private ArrayList<Mosque> mDataset;
    private Application app;

    MosqueAdapter(Application app, ArrayList<Mosque> mDataset) {
        this.app = app;
        this.mDataset = mDataset;
    }

    @NonNull
    @Override
    public MosqueAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_mosque, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MosqueAdapter.ViewHolder holder, int position) {
        //ntr ganti
        try {
            String url = null;
            try {
                url = "https://maps.googleapis.com/maps/api/place/photo?" +
                        "maxwidth=" + mDataset.get(position).getPhotos().getJSONObject(0).getString("width") +
                        "&photoreference=" + mDataset.get(position).getPhotos().getJSONObject(0).getString("photo_reference") +
                        "&key=" + app.getApplicationInfo().metaData.getString("com.google.android.geo.API_KEY");
            } catch (JSONException e) {
                Log.e(MosqueActivity.getTAG(), "Json parsing error: " + e.getMessage());
            }

            Glide.with(app)
                    .load(url)
                    .placeholder(R.drawable.bg_loading)
                    .into(holder.imageView);
            holder.textTitle.setText(mDataset.get(position).getName());
            holder.textRating.setText(mDataset.get(position).getRating());
            holder.textDistance.setText(String.format("%.2f km", countDistance(mDataset.get(position).getGeoLocation())));
        } catch (NullPointerException e) {

        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private double countDistance(LatLng latLng) {
        LatLng currentLatLng = new LatLng(Double.parseDouble(app.getSharedPreferences("location", MODE_PRIVATE).getString("Latitude", null)), Double.parseDouble(app.getSharedPreferences("location", MODE_PRIVATE).getString("Longitude", null)));
        Distance distance = new Distance();

        return distance.distance(currentLatLng.latitude, latLng.latitude, currentLatLng.longitude, latLng.longitude);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textTitle, textRating, textDistance;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.gbr1);
            textTitle = itemView.findViewById(R.id.tul1);
            textRating = itemView.findViewById(R.id.tul2);
            textDistance = itemView.findViewById(R.id.tul3);
        }
    }
}
