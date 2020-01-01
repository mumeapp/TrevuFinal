package com.remu;

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
import com.remu.POJO.TourPlace;

import java.util.ArrayList;

public class TourismAdapter extends RecyclerView.Adapter<TourismAdapter.ViewHolder> {

    private ArrayList<TourPlace> mDataset;
    private ItemClickListener mClickListener;
    private LatLng currentLatLng;
    private double dpWidth;

    TourismAdapter(Context context, ArrayList<TourPlace> mDataset, LatLng currentLatLng) {
        this.mDataset = mDataset;
        this.currentLatLng = currentLatLng;

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        dpWidth = displayMetrics.widthPixels / displayMetrics.density;
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
//        holder.setFixedHeight();
        holder.cardView.setMinimumWidth(/*(int) ((dpWidth - 40) / 2)*/300);
        holder.image.setImageDrawable(mDataset.get(position).getImage());
        holder.title.setText(mDataset.get(position).getTitle());
        holder.rating.setText(String.format("%.2f", mDataset.get(position).getRating()));
        holder.distance.setText(String.format("%.2f km", getJarak(currentLatLng, mDataset.get(position).getPosition())));
    }

    @Override
    public int getItemCount() { return mDataset.size(); }

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

    public TourPlace getItem(int id) {
        return mDataset.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    private double getJarak(LatLng currentLatLng, LatLng destinationLatlng){
        double lat1 = currentLatLng.latitude;
        double lat2 = destinationLatlng.latitude;
        double long1 = currentLatLng.latitude;
        double long2 = destinationLatlng.longitude;
        return Distance.distance(lat1, lat2, long1, long2);
    }

}
