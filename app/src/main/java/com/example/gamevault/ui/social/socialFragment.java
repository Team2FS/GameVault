package com.example.gamevault.ui.social;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;
import com.example.gamevault.R;


public class socialFragment extends Fragment {
    private ImageView profileImageView;
    private RecyclerView chatRecyclerView;
    private EditText messageInput;
    private Button sendButton;
    private  ChatAdapter chatAdapter;
    private List<Message> messageList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_social, container, false);
        chatRecyclerView = view.findViewById(R.id.chatRecyclerView);
        messageInput = view.findViewById(R.id.messageInput);
        sendButton = view.findViewById(R.id.sendButton);

        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList);
        chatRecyclerView.setAdapter(chatAdapter);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

       // loadProfileImage << ur gonna forget to do this DONT FORGET TO DO THIS
         loadMessages();

        sendButton.setOnClickListener(v -> sendMessage());
        return view;
    }

    private void loadMessages() {
        //idk what im doing with this lol
    }

    private void sendMessage() {
        String message = messageInput.getText().toString();


    }

}