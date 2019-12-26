package com.remu.ui.onboard.onboard3;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OnBoarding3ViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public OnBoarding3ViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }

}
