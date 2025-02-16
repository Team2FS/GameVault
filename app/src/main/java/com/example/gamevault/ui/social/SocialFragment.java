package com.example.gamevault.ui.social;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gamevault.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SocialFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;

    // Firebase instances
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    // UI elements
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private EditText postEditText;
    private Button postButton;
    private ImageButton attachImageButton;
    private ImageView imagePreview;
    private ImageView profilePicture;
    private Uri imageUri;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_social, container, false);

        // Initialize Firebase instances
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        // Initialize UI elements
        recyclerView = root.findViewById(R.id.postRecyclerView);
        postEditText = root.findViewById(R.id.postEditText);
        postButton = root.findViewById(R.id.postButton);
        attachImageButton = root.findViewById(R.id.attachImageButton);
        imagePreview = root.findViewById(R.id.imagePreview);
        profilePicture = root.findViewById(R.id.profileImageView);

        // Set up RecyclerView
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), postList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(postAdapter);

        // Load posts from Firestore
        loadPosts();

        // Load profile picture
        loadProfilePicture();

        // Set up button click listeners
        attachImageButton.setOnClickListener(v -> openGallery());
        postButton.setOnClickListener(v -> createPost());

        return root;
    }

    private void loadProfilePicture() {
        FirebaseUser  user = FirebaseAuth.getInstance().getCurrentUser ();
        if (user != null) {
            db.collection("users").document(user.getUid()).get().addOnSuccessListener(document -> {
                if (document.exists() && document.contains("imageUrl")) {
                    String imageUrl = document.getString("imageUrl");

                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Glide.with(this)
                                .load(imageUrl)
                                .placeholder(android.R.drawable.ic_menu_gallery)
                                .error(android.R.drawable.ic_menu_report_image)
                                .circleCrop() // Ensure circular crop
                                .into(profilePicture);
                    }
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(requireContext(), "Failed to load profile picture", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
                imagePreview.setImageBitmap(bitmap);
                imagePreview.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadPosts() {
        db.collection("posts").orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error == null && value != null) {
                        postList.clear();
                        for (var doc : value) {
                            Post post = doc.toObject(Post.class);
                            postList.add(post);
                        }
                        postAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void createPost() {
        String content = postEditText.getText().toString().trim();
        if (content.isEmpty()) {
            Toast.makeText(getContext(), "Post content cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the current user's ID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();

        // Get the profile picture URL using the callback
        getCurrentUserProfilePictureUrl(userId, new ProfilePictureCallback() {

            @Override
            public void onProfilePictureUrlLoaded(String profilePictureUrl, String username) {
                // Create a post with the profile picture URL
                if (imageUri != null) {
                    uploadImage(content, userId, profilePictureUrl, username);
                } else {
                    Post newPost = new Post(
                            null,
                            userId,
                            username,
                            content,
                            "",
                            profilePictureUrl,
                            new Date(),
                            0
                    );

                    // Add the post to Firestore
                    db.collection("posts").add(newPost).addOnSuccessListener(documentReference -> {
                        postEditText.setText("");
                        imagePreview.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Post added!", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to add post: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCurrentUserProfilePictureUrl(String userId, ProfilePictureCallback callback) {
    DocumentReference userRef = db.collection("users").document(userId);
    userRef.get().addOnSuccessListener(documentSnapshot -> {
        if (documentSnapshot.exists()) {
            String profilePictureUrl = documentSnapshot.getString("imageUrl");
            String username = documentSnapshot.getString("username"); // Assuming you have a username field
            if (profilePictureUrl != null && username != null) {
                callback.onProfilePictureUrlLoaded(profilePictureUrl, username);
            } else {
                callback.onFailure("Profile picture or username not found");
            }
        } else {
            callback.onFailure("User document not found");
        }
    }).addOnFailureListener(e -> callback.onFailure("Failed to fetch profile data: " + e.getMessage()));
}

    private void uploadImage(String content, String userId, String profilePictureUrl, String username) {
            // Create a reference to the image file in Firebase Storage
            StorageReference storageRef = storage.getReference();
            StorageReference imageRef = storageRef.child("post_images/" + UUID.randomUUID().toString());

            // Upload the image to Firebase Storage
            UploadTask uploadTask = imageRef.putFile(imageUri);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();

                    // Create a post with the image URL
                    Post newPost = new Post(
                            null,
                            userId,
                            username,
                            content,
                            imageUrl,
                            profilePictureUrl,
                            new Date(),
                            0
                    );

                    // Add the post to Firestore
                    db.collection("posts").add(newPost).addOnSuccessListener(documentReference -> {
                        postEditText.setText("");
                        imagePreview.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Post added!", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to add post: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(getContext(), "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }

    public interface ProfilePictureCallback {
        void onProfilePictureUrlLoaded(String profilePictureUrl, String username);
        void onFailure(String errorMessage);
    }

}