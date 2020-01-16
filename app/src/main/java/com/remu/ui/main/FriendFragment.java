package com.remu.ui.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.remu.FindFriendActivity;
import com.remu.R;

public class FriendFragment extends Fragment {
    private Button btnFindFriend;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_friend, container, false);

        initializeUI(root);

        btnFindFriend.setOnClickListener(view -> {
            Intent viewFindFriend = new Intent(getActivity(), FindFriendActivity.class);
            startActivity(viewFindFriend);
        });

        return root;
    }

    private void initializeUI(View root){
        btnFindFriend = root.findViewById(R.id.btn_find_more);
    }

}
