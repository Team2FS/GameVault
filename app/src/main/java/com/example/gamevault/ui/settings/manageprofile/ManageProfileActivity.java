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
import android.widget.EditText;
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
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import com.bumptech.glide.Glide;


public class ManageProfileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profileImage;
    private Button btnChangePicture, btnSaveProfile;
    private Uri imageUri; // To store the selected image URI
    private FirebaseFirestore firestore;
    private StorageReference storageReference;
    private String currentImageUrl = null; // Stores the existing image URL
    private EditText etBio, etPhone, etEmail;

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
        etBio = findViewById(R.id.etBio);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);

        // Initialize Firebase services
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("profile_pictures");

        setupButtonListeners();

        // Load the user's profile when the activity starts
        loadProfile();

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

        // Retrieve user inputs
        String bio = etBio.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        // Ensure either an image is selected or an existing image is available
        if (imageUri == null && currentImageUrl == null) {
            Toast.makeText(this, "Please select a profile picture before saving.", Toast.LENGTH_SHORT).show();
            return;
        }

        // If a new image is selected, upload it, otherwise just save text fields
        if (imageUri != null) {
            uploadImageToFirebase(user.getUid(), bio, phone, email);
        } else {
            saveProfileToFirestore(user.getUid(), currentImageUrl, bio, phone, email);
        }
    }

    private void uploadImageToFirebase(String userId, String bio, String phone, String email) {
        StorageReference fileReference = storageReference.child(userId + ".jpg");

        fileReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    saveProfileToFirestore(userId, imageUrl, bio, phone, email);
                }))
                .addOnFailureListener(e -> {
                    Log.e("ManageProfile", "Error uploading image", e);
                    Toast.makeText(this, "Failed to upload image.", Toast.LENGTH_SHORT).show();
                });
    }

    private void saveProfileToFirestore(String userId, String imageUrl, String bio, String phone, String email) {
        Map<String, Object> profileData = new HashMap<>();
        profileData.put("imageUrl", imageUrl);
        profileData.put("bio", bio);
        profileData.put("phone", phone);
        profileData.put("email", email);

        firestore.collection("users")
                .document(userId)
                .set(profileData)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Profile saved successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> {
                    Log.e("ManageProfile", "Error saving profile", e);
                    Toast.makeText(this, "Failed to save profile.", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "You are not signed in", Toast.LENGTH_SHORT).show();
            return;
        }

        firestore.collection("users")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        currentImageUrl = documentSnapshot.getString("imageUrl");
                        String bio = documentSnapshot.getString("bio");
                        String phone = documentSnapshot.getString("phone");
                        String email = documentSnapshot.getString("email");

                        if (currentImageUrl != null && !currentImageUrl.isEmpty()) {
                            Glide.with(this).load(currentImageUrl).into(profileImage);
                        }
                        if (bio != null) etBio.setText(bio);
                        if (phone != null) etPhone.setText(phone);
                        if (email != null) etEmail.setText(email);
                    }
                })
                .addOnFailureListener(e -> Log.e("ManageProfile", "Error loading profile", e));
    }

}

