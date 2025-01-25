package com.example.gamevault.ui.settings.manageprofile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.gamevault.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ManageProfileActivity extends AppCompatActivity {
        private static final int PICK_IMAGE_REQUEST = 1;
        private ImageView profileImage;
        private Button btnChangePicture;
        private Button btnSaveProfile;
        private Uri imageUri; // To store the selected image URI

        @SuppressLint("MissingInflatedId")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_manage_profile);

            // Set up window insets for edge-to-edge display
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            // Initialize views
            profileImage = findViewById(R.id.profileImageView);
            btnChangePicture = findViewById(R.id.btnChangePicture);
            btnSaveProfile = findViewById(R.id.btnSaveProfile);

            setupButtonListeners();
        }

        private void setupButtonListeners() {
            btnChangePicture.setOnClickListener(v -> openFileChooser());
            btnSaveProfile.setOnClickListener(v -> saveProfile());
        }

        private void openFileChooser() {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                imageUri = data.getData();
                profileImage.setImageURI(imageUri); // Set the selected image to the ImageView
            }
        }

        private void saveProfile() {

            // Here you can implement the logic to save the profile information
            // For example, you can save the imageUri to Firebase Storage and user data to Firestore

            if (imageUri != null) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null) {

                }
                // Upload the image to Firebase Storage and save user data
                // Example: uploadImageToFirebase(imageUri);
                Toast.makeText(this, "Profile saved!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please select a profile picture.", Toast.LENGTH_SHORT).show();
            }
        }

        // Implement the method to upload the image to Firebase Storage if needed
        // private void uploadImageToFirebase(Uri imageUri) {
        //     // Your Firebase upload logic here
        // }
    }

