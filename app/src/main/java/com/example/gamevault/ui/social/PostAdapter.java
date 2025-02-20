package com.example.gamevault.ui.social;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gamevault.R;


import java.text.SimpleDateFormat;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private Context context;
    private List<Post> postList;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.postContent.setText(post.getContent());

        // Format and display date
        if (post.getTimestamp() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm");
            holder.timestamp.setText(sdf.format(post.getTimestamp()));
        } else {
            holder.timestamp.setText("Unknown Date");
        }

        // Load profile picture with Glide and apply circular transform
        if (post.getProfilePictureUrl() != null && !post.getProfilePictureUrl().isEmpty()) {
            Glide.with(context)
                    .load(post.getProfilePictureUrl())
                    .circleCrop()
                    .into(holder.profilePicture);
        } else {
            holder.profilePicture.setImageResource(R.drawable.ic_profile_placeholder);
        }

        // Set the username
        if (post.getUsername() != null && !post.getUsername().isEmpty()) {
            holder.username.setText(post.getUsername());
        } else {
            holder.username.setText("Unknown User");
        }

        //click listener for username textView
        holder.username.setOnClickListener(view -> {
            Intent intent = new Intent(context, UserProfileActivity.class);
            Log.d("PostDebug", "Username: " + post.getUsername());
            intent.putExtra("userId", post.getUserId());
            context.startActivity(intent);

        });

        // Load post image if exists
        if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
            holder.postImage.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(post.getImageUrl())
                    .into(holder.postImage);
        } else {
            holder.postImage.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView postContent, timestamp, username;
        ImageView postImage, profilePicture;

        public PostViewHolder(View itemView) {
            super(itemView);
            postContent = itemView.findViewById(R.id.postContent);
            timestamp = itemView.findViewById(R.id.postTimestamp);
            postImage = itemView.findViewById(R.id.postImage);
            profilePicture = itemView.findViewById(R.id.profileImageView);
            username = itemView.findViewById(R.id.username);
        }
    }
}
