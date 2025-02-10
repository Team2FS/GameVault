package com.example.gamevault.ui.social;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class socialViewModel extends ViewModel {
    private final MutableLiveData<List<Message>> messageList;

    public socialViewModel() {
        messageList = new MutableLiveData<>(new ArrayList<>());
    }

    public LiveData<List<Message>> getMessages(){
        return messageList;
    }

    public void addMessage(String content){
        List<Message> currentMessages = messageList.getValue();
        if(currentMessages == null){
            currentMessages.add(new Message(content));
            messageList.setValue(currentMessages);
        }
    }
}