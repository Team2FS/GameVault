package com.example.gamevault.ui.social;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.gamevault.R;
import android.widget.Toast;



public class UserProfileActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private ImageView profileImageView;
    private TextView usernameTextView;
    private TextView bioTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //intialization of firebase
        db = FirebaseFirestore.getInstance();

        profileImageView = findViewById(R.id.profileImageView);
        usernameTextView = findViewById(R.id.usernameTextView);
        bioTextView = findViewById(R.id.bioTextView);

        String userId = getIntent().getStringExtra("userId");

        loadUser (userId);
    }

    @SuppressLint("CheckResult")
    private void loadUser(String userId) {
        db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String username = documentSnapshot.getString("username");
                String bio = documentSnapshot.getString("bio");
                String imageUrl = documentSnapshot.getString("imageUrl");

                usernameTextView.setText(username);
                bioTextView.setText(bio);

                if (imageUrl != null && !imageUrl.isEmpty()) {
                    Glide.with(this)
                            .load(imageUrl)
                            .placeholder(android.R.drawable.ic_menu_gallery)
                            .error(android.R.drawable.ic_menu_report_image)
                            .circleCrop()
                            .into(profileImageView);
                }
            } else {
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to load profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

}
