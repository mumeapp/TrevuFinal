package com.remu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class HalalFoodCategoriesAdapter extends RecyclerView.Adapter<HalalFoodCategoriesAdapter.ViewHolder> {

    private ArrayList<String> mDataset;

    HalalFoodCategoriesAdapter(ArrayList<String> mDataset) {
        this.mDataset = mDataset;
    }

    @NonNull
    @Override
    public HalalFoodCategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_mosque, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HalalFoodCategoriesAdapter.ViewHolder holder, int position) {
        holder.mTitle.setText(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTitle;

        ViewHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.title);
        }
    }
}
