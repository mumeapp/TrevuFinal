package com.remu.ui.onboard.onboard2;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OnBoarding2ViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public OnBoarding2ViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }

}
