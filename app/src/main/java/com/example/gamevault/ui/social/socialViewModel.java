package com.example.gamevault.ui.social;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class socialViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public socialViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText(){return mText;}
}