package com.remu.ui.onboard.onboard1;

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

public class OnBoarding1Fragment extends Fragment {

    private OnBoarding1ViewModel onBoarding1ViewModel;
    private TextView skipOption;
    private Button continueTo2;

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
        onBoarding1ViewModel = ViewModelProviders.of(this).get(OnBoarding1ViewModel.class);
        View root = inflater.inflate(R.layout.fragment_onboard1, container, false);

        initializeUI(root);

        skipOption.setOnClickListener(v -> {
            FragmentChangeListener fragmentChangeListener = (FragmentChangeListener) getActivity();
            fragmentChangeListener.replaceFragment(3);
        });

        continueTo2.setOnClickListener(v -> {
            FragmentChangeListener fragmentChangeListener = (FragmentChangeListener) getActivity();
            fragmentChangeListener.replaceFragment(2);
        });
        return root;
    }

    private void initializeUI(View root) {
        skipOption = root.findViewById(R.id.option_skip);
        continueTo2 = root.findViewById(R.id.button_1_to_2);
    }

}
