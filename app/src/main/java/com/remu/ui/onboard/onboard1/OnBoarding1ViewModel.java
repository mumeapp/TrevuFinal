package com.remu.ui.onboard.onboard1;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OnBoarding1ViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public OnBoarding1ViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }

}
