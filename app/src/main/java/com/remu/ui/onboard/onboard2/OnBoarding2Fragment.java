package com.remu.ui.onboard.onboard2;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.remu.POJO.FragmentChangeListener;
import com.remu.R;

public class OnBoarding2Fragment extends Fragment {

    private OnBoarding2ViewModel onBoarding2ViewModel;
    private Button continueTo3;

    private FragmentActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            mActivity = (FragmentActivity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        onBoarding2ViewModel = ViewModelProviders.of(this).get(OnBoarding2ViewModel.class);
        View root = inflater.inflate(R.layout.fragment_onboard2, container, false);

        initializeUI(root);

        continueTo3.setOnClickListener(v -> {
            FragmentChangeListener fragmentChangeListener = (FragmentChangeListener) getActivity();
            fragmentChangeListener.replaceFragment(3);
        });
        return root;
    }

    private void initializeUI(View root) {
        continueTo3 = root.findViewById(R.id.button_2_to_3);
    }

}
