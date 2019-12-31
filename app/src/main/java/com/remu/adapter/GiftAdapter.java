package com.remu.adapter;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.model.LatLng;
import com.remu.HalalGiftDetail;
import com.remu.POJO.Distance;
import com.remu.POJO.PlaceModel;
import com.remu.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.ViewHolder> {

    Application app;
    Activity activity;
    ArrayList<PlaceModel> mDataset;

    public GiftAdapter(Application app, Activity activity, ArrayList<PlaceModel> mDataset) {
        this.app = app;
        this.activity = activity;
        this.mDataset = mDataset;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_toko, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.giftName.setText(mDataset.get(position).getPlaceName());
        holder.giftRating.setText(String.format("%.1f", mDataset.get(position).getPlaceRating()));
        holder.giftDistance.setText(String.format("%.1f", countDistance(mDataset.get(position).getPlaceLocation())));

        if (mDataset.get(position).getPlacePhotoUri() != null) {
            Picasso.get().load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=500&photoreference=" + mDataset.get(position).getPlacePhotoUri()
                    + "&key=AIzaSyA2yW_s0jqKnavh2AxISXB272VuSE56WI8")
                    .error(R.drawable.bg_loading)
                    .placeholder(R.drawable.bg_loading)
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .into(holder.giftImage);
        } else {
            holder.giftImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_places_book_store));
        }

        holder.giftCard.setOnClickListener((v) -> {
            Intent intent = new Intent(activity.getBaseContext(), HalalGiftDetail.class);
            intent.putExtra("place_id", mDataset.get(position).getPlaceId());
            activity.startActivity(intent);
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

        CardView giftCard;
        ImageView giftImage;
        TextView giftName, giftRating, giftDistance;

        ViewHolder(View itemView) {
            super(itemView);

            giftCard = itemView.findViewById(R.id.gift_card);
            giftImage = itemView.findViewById(R.id.gift_image);
            giftName = itemView.findViewById(R.id.gift_name);
            giftRating = itemView.findViewById(R.id.gift_rating);
            giftDistance = itemView.findViewById(R.id.gift_distance);
        }
    }

}
