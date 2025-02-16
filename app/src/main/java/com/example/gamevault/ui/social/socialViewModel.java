package com.example.gamevault.ui.social;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class socialViewModel extends ViewModel {
    private final MutableLiveData<List<Post>> messageList;

    public socialViewModel() {
        messageList = new MutableLiveData<>(new ArrayList<>());
    }

    public LiveData<List<Post>> getMessages() {
        return messageList;
    }

    public void addMessage(String content) {
        List<Post> currentPosts = messageList.getValue();
        if (currentPosts == null) {
            currentPosts = new ArrayList<>(); // Initialize if null
        }
        messageList.setValue(currentPosts); // Update LiveData
    }
}