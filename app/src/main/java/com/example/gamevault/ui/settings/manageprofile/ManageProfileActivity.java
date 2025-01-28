package com.example.gamevault.ui.settings.manageprofile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;


public class ManageProfileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profileImage;
    private Button btnChangePicture;
    private Button btnSaveProfile;
    private Uri imageUri; // To store the selected image URI
    private FirebaseFirestore firestore;
    private StorageReference storageReference;

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

        // Initialize Firebase services
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("profile_pictures");

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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Toast.makeText(this, "You are not signed in", Toast.LENGTH_SHORT).show();
            return;
        }

        // Here you can implement the logic to save the profile information
        // For example, you can save the imageUri to Firebase Storage and user data to Firestore

        if (imageUri != null) {
            // upload image to Firebase Storage
            uploadImageToFirebase(user.getUid());
        } else {
            Toast.makeText(this, "Please select an image to upload", Toast.LENGTH_SHORT).show();
        }

    }

    private void uploadImageToFirebase(String userId) {
        StorageReference fileReference = storageReference.child(userId + ".jpg");

        fileReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get the download URL of the uploaded image
                    fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        saveProfileToFirestore(userId, imageUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error uploading image", e);
                    Toast.makeText(this, "Failed to upload image.", Toast.LENGTH_SHORT).show();
                });
    }

    private void saveProfileToFirestore(String userId, String imageUrl) {
        Map<String, Object> profileData = new HashMap<>();
        profileData.put("imageUrl", imageUrl);

        firestore.collection("users")
                .document(userId)
                .set(profileData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Profile saved successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error saving profile", e);
                    Toast.makeText(this, "Failed to save profile.", Toast.LENGTH_SHORT).show();
                });
    }
}

