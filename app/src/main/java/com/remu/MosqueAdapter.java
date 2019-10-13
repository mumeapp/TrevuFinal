package com.remu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class MosqueAdapter extends RecyclerView.Adapter<MosqueAdapter.ViewHolder> {

    private ArrayList<String> mDataset;

    MosqueAdapter(ArrayList<String> mDataset) {
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
