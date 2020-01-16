package com.remu.ui.findfriends;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.annotations.NotNull;
import com.remu.FindFriendResultActivity;
import com.remu.R;
import com.skyfishjy.library.RippleBackground;

public class FindFriend3Fragment extends Fragment {

    private RippleBackground rippleBackground;

    private FragmentActivity mActivity;

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            mActivity = (FragmentActivity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_find_friend3, container, false);

        rippleBackground = root.findViewById(R.id.content);
        rippleBackground.startRippleAnimation();

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(mActivity, FindFriendResultActivity.class);
            startActivity(intent);
            mActivity.finish();
        }, 5000);

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();

        rippleBackground.stopRippleAnimation();
    }

    @Override
    public void onResume() {
        super.onResume();

        rippleBackground.startRippleAnimation();
    }
}
