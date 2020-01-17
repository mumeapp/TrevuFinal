package com.remu.adapter;

import android.annotation.SuppressLint;
import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.remu.POJO.User;
import com.remu.R;

import java.util.ArrayList;
import java.util.Calendar;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    private ArrayList<User> mDataset;
    private Application app;

    public FriendAdapter(Application app, ArrayList<User> mDataset) {
        this.app = app;
        this.mDataset = mDataset;
    }

    @NonNull
    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_friend, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FriendAdapter.ViewHolder holder, int position) {
        holder.name.setText(mDataset.get(position).getNama());

        Glide.with(app.getApplicationContext())
                .load(mDataset.get(position).getFoto())
                .placeholder(R.drawable.ic_default_avatar)
                .into(holder.image);
        String[] birthDate = mDataset.get(position).getTanggal().split(" ");
        switch (birthDate[1]) {
            case "January":
                birthDate[1] = "0";
                break;
            case "February":
                birthDate[1] = "1";
                break;
            case "March":
                birthDate[1] = "2";
                break;
            case "April":
                birthDate[1] = "3";
                break;
            case "Mey":
                birthDate[1] = "4";
                break;
            case "June":
                birthDate[1] = "5";
                break;
            case "July":
                birthDate[1] = "6";
                break;
            case "August":
                birthDate[1] = "7";
                break;
            case "September":
                birthDate[1] = "8";
                break;
            case "October":
                birthDate[1] = "9";
                break;
            case "November":
                birthDate[1] = "10";
                break;
            case "December":
                birthDate[1] = "11";
                break;
        }
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        int age;
        if (month > Integer.parseInt(birthDate[1])) {
            age = year - Integer.parseInt(birthDate[2]);
        } else if (month == Integer.parseInt(birthDate[1]) && day >= Integer.parseInt(birthDate[0])) {
            age = year - Integer.parseInt(birthDate[2]);
        } else {
            age = year - Integer.parseInt(birthDate[2]) - 1;
        }
        holder.age.setText(Integer.toString(age));
        holder.gender.setText(mDataset.get(position).getGender());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name, gender, age, addFriend;

        ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.friend_photo);
            name = itemView.findViewById(R.id.friend_name);
            gender = itemView.findViewById(R.id.friend_gender);
            age = itemView.findViewById(R.id.friend_age);
            addFriend = itemView.findViewById(R.id.button_add_friend);
        }
    }

}
