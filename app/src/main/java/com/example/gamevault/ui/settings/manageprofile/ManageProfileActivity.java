package com.example.gamevault.ui.settings.manageprofile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

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

import java.util.HashMap;
import java.util.Map;

import com.bumptech.glide.Glide;


public class ManageProfileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profileImage;
    private Button btnChangePicture, btnSaveProfile;
    private Uri imageUri; // To store the selected image URI
    private FirebaseFirestore firestore;
    private StorageReference storageReference;
    private String currentImageUrl = null; // Stores the existing image URL
    private EditText etUsername, etPhone, etEmail, etBio;

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
        etUsername = findViewById(R.id.etUsername);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etBio = findViewById(R.id.etBio);

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
        String username = etUsername.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String bio = etBio.getText().toString().trim();

        // Ensure either an image is selected or an existing image is available
        if (imageUri == null && currentImageUrl == null) {
            Toast.makeText(this, "Please select a profile picture before saving.", Toast.LENGTH_SHORT).show();
            return;
        }

        // If a new image is selected, upload it, otherwise just save text fields
        if (imageUri != null) {
            uploadImageToFirebase(user.getUid(), username, phone, email, bio);
        } else {
            saveProfileToFirestore(user.getUid(), currentImageUrl, username, phone, email, bio);
        }
    }

    private void uploadImageToFirebase(String userId, String username, String phone, String email, String bio) {
        StorageReference fileReference = storageReference.child(userId + ".jpg");

        fileReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    saveProfileToFirestore(userId, imageUrl, username, phone, email, bio);
                }))
                .addOnFailureListener(e -> {
                    Log.e("ManageProfile", "Error uploading image", e);
                    Toast.makeText(this, "Failed to upload image.", Toast.LENGTH_SHORT).show();
                });
    }

    private void saveProfileToFirestore(String userId, String imageUrl, String username, String phone, String email, String bio) {
        Map<String, Object> profileData = new HashMap<>();
        profileData.put("imageUrl", imageUrl);
        profileData.put("username", username);
        profileData.put("phone", phone);
        profileData.put("email", email);
        profileData.put("bio", bio);

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
                        String username = documentSnapshot.getString("username");
                        String phone = documentSnapshot.getString("phone");
                        String email = documentSnapshot.getString("email");
                        String bio = documentSnapshot.getString("bio");

                        if (currentImageUrl != null && !currentImageUrl.isEmpty()) {
                            Glide.with(this).load(currentImageUrl).into(profileImage);
                        }
                        if (username != null) etUsername.setText(username);
                        if (phone != null) etPhone.setText(phone);
                        if (email != null) etEmail.setText(email);
                        if (bio != null) etBio.setText(bio);
                    }
                })
                .addOnFailureListener(e -> Log.e("ManageProfile", "Error loading profile", e));
    }

}

