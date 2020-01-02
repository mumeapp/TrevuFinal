package com.remu.adapter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.remu.POJO.Distance;
import com.remu.POJO.PlaceModel;
import com.remu.POJO.TourPlace;
import com.remu.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class TourismAdapter extends RecyclerView.Adapter<TourismAdapter.ViewHolder> {

    Application app;
    Activity activity;
    private ArrayList<PlaceModel> mDataset;
    private ItemClickListener mClickListener;
    private LatLng currentLatLng;

    public TourismAdapter(Application app, Activity activity, ArrayList<PlaceModel> mDataset, LatLng currentLatLng) {
        this.app = app;
        this.activity = activity;
        this.mDataset = mDataset;
        this.currentLatLng = currentLatLng;
    }

    @NonNull
    @Override
    public TourismAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_tourism, parent, false);
//
//            @Override
//            public void setFixedHeight() {
//                //  magic happening here
//                ViewGroup.LayoutParams parentParams = parent.getLayoutParams();
//                parentParams.height =
//                        ((RecyclerView) parent).computeVerticalScrollRange()
//                                + parent.getPaddingTop()
//                                + parent.getPaddingBottom();
//                parent.setLayoutParams(parentParams);
//            }
//        };
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TourismAdapter.ViewHolder holder, int position) {
        holder.title.setText(mDataset.get(position).getPlaceName());

        if (mDataset.get(position).getPlaceRating() == 0) {
            holder.rating.setText("-");
        } else {
            holder.rating.setText(String.format("%.1f", mDataset.get(position).getPlaceRating()));
        }

        holder.distance.setText(String.format("%.2f km", countDistance(mDataset.get(position).getPlaceLocation())));

        if (mDataset.get(position).getPlacePhotoUri() != null) {
            Picasso.get().load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=500&photoreference=" + mDataset.get(position).getPlacePhotoUri()
                    + "&key=AIzaSyA2yW_s0jqKnavh2AxISXB272VuSE56WI8")
                    .error(R.drawable.bg_loading)
                    .placeholder(R.drawable.bg_loading)
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .into(holder.image);
        } else {
            LatLng location = mDataset.get(position).getPlaceLocation();
            Picasso.get().load("https://maps.googleapis.com/maps/api/streetview?size=500x300&location=" + location.latitude + "," + location.longitude
                    + "&fov=120&pitch=10&key=AIzaSyA2yW_s0jqKnavh2AxISXB272VuSE56WI8")
                    .error(R.drawable.bg_loading)
                    .placeholder(R.drawable.bg_loading)
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cardView;
        ImageView image;
        TextView title, rating, distance;

        ViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.tourism_card);
            image = itemView.findViewById(R.id.img_tour_place);
            title = itemView.findViewById(R.id.title_tour_place);
            rating = itemView.findViewById(R.id.rating_tour_place);
            distance = itemView.findViewById(R.id.distance_tour_place);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

//        public abstract void setFixedHeight();
    }

    public PlaceModel getItem(int id) {
        return mDataset.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    private double countDistance(LatLng latLng) {
        LatLng currentLatLng = new LatLng(Double.parseDouble(app.getSharedPreferences("location", MODE_PRIVATE).getString("Latitude", null)), Double.parseDouble(app.getSharedPreferences("location", MODE_PRIVATE).getString("Longitude", null)));
        return Distance.distance(currentLatLng.latitude, latLng.latitude, currentLatLng.longitude, latLng.longitude);
    }

}
