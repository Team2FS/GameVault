package com.example.gamevault.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.gamevault.databinding.FragmentSettingsBinding;
import com.example.gamevault.ui.login.loginActivity;
import com.example.gamevault.ui.settings.BuyUsACoffee.BuyUsACoffee;
import com.example.gamevault.ui.settings.camosync.CamoSync;
import com.example.gamevault.ui.settings.manageprofile.ManageProfileActivity;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.example.gamevault.ui.settings.credits.Credits;
import com.example.gamevault.ui.settings.suggestfeature.SuggestFeatureActivity;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout using View Binding
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize ViewModel
        SettingsViewModel settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        // Handle Button click using View Binding
//        binding.ToggleTheme.setOnClickListener(v -> {
//            Log.d("SettingsFragment", "Toggle Theme Clicked");
//
//            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("Profile", Context.MODE_PRIVATE);
//            boolean isDarkModeEnabled = sharedPreferences.getBoolean("isDarkModeEnabled", true);
//
//            if (isDarkModeEnabled) {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putBoolean("isDarkModeEnabled", false);
//                editor.apply();
//            }
//            else {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putBoolean("isDarkModeEnabled", true);
//                editor.apply();
//            }
//        });
        // Handle manage profile button
        binding.ManageProfile.setOnClickListener(v -> {
            Log.d("SettingsFragment", "Manage Profile Clicked");
            Intent intent = new Intent(getActivity(), ManageProfileActivity.class);
            startActivity(intent);

        });
//        // Handle manage linked accounts button
//        binding.ManageLinkedAccounts.setOnClickListener(v -> {
//            Log.d("SettingsFragment", "Manage Linked Accounts Clicked");
//        });
        // Handle manage camo progress button
        binding.ManageCamoProgress.setOnClickListener(v -> {
            Log.d("SettingsFragment", "Manage Camo Progress Clicked");
            Intent intent = new Intent(getActivity(), CamoSync.class);
            startActivity(intent);
        });
        // Handle Suggest Feature button
        binding.SuggestFeature.setOnClickListener(v -> {
            Log.d("SettingsFragment", "Suggest Feature Clicked");
            Intent intent = new Intent(getActivity(), SuggestFeatureActivity.class);
            startActivity(intent);
        });
        // Handle Donate button
        binding.Donate.setOnClickListener(v -> {
            Log.d("SettingsFragment", "Donate Clicked");
            Intent intent = new Intent(getActivity(), BuyUsACoffee.class);
            startActivity(intent);
        });
        // Handle Credits button
        binding.Credits.setOnClickListener(v -> {
            Log.d("SettingsFragment", "Credit Clicked");
            Intent intent = new Intent(requireContext(), Credits.class);
            startActivity(intent);
        });
        // Handle Delete button
        binding.DeleteAccount.setOnClickListener(v -> {
            Log.d("SettingsFragment", "Delete Clicked");

            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            FirebaseUser user = auth.getCurrentUser();

            if (user == null) return;

            // Prompt user for password input
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Reauthenticate");
            builder.setMessage("Please enter your password to delete your account.");

            final EditText passwordInput = new EditText(requireContext());
            passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordInput.setHint("Enter password");
            builder.setView(passwordInput);

            builder.setPositiveButton("Confirm", (dialog, which) -> {
                String password = passwordInput.getText().toString().trim();

                if (password.isEmpty()) {
                    Toast.makeText(requireContext(), "Password cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Reauthenticate user
                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);
                user.reauthenticate(credential).addOnCompleteListener(reauthTask -> {
                    if (!reauthTask.isSuccessful()) {
                        Log.e("Firebase", "Reauthentication failed: " + reauthTask.getException().getMessage());
                        Toast.makeText(requireContext(), "Reauthentication failed. Incorrect password.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Log.d("Firebase", "Reauthentication successful.");

                    // Retrieve the profile picture URL from Firestore
                    db.collection("users").document(user.getUid()).get().addOnSuccessListener(document -> {
                        if (document.exists() && document.contains("imageUrl")) {
                            String imageUrl = document.getString("imageUrl");

                            if (imageUrl != null && !imageUrl.isEmpty()) {
                                Log.d("Storage", "Deleting Profile Picture from Cloud Storage: " + imageUrl);

                                // Extract file path manually from URL
                                String filePath = extractStoragePath(imageUrl);
                                if (filePath != null) {
                                    StorageReference imageRef = storage.getReference().child(filePath);
                                    Log.d("Storage", "Extracted path: " + extractStoragePath(imageUrl));


                                    // Delete the profile picture from Storage
                                    imageRef.delete().addOnCompleteListener(deletePicTask -> {
                                        if (deletePicTask.isSuccessful()) {
                                            Log.d("Storage", "Profile picture deleted successfully.");
                                        } else {
                                            Log.e("Storage", "Failed to delete profile picture: " + deletePicTask.getException().getMessage());
                                        }
                                        // Proceed to delete user Firestore data
                                        deleteUserFromFirestore(user, db, auth);
                                    });
                                } else {
                                    Log.e("Storage", "Invalid image URL. Proceeding with account deletion.");
                                    deleteUserFromFirestore(user, db, auth);
                                }
                            } else {
                                Log.d("Storage", "No profile picture found. Proceeding with account deletion.");
                                deleteUserFromFirestore(user, db, auth);
                            }
                        } else {
                            Log.d("Firestore", "No user data found in Firestore. Proceeding with account deletion.");
                            deleteUserFromFirestore(user, db, auth);
                        }
                    }).addOnFailureListener(e -> {
                        Log.e("Firestore", "Failed to retrieve user data: " + e.getMessage());
                        Toast.makeText(requireContext(), "Error fetching user data", Toast.LENGTH_SHORT).show();
                    });
                });
            });

            // "Cancel" button
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            // Show the dialog
            builder.show();
        });
        // Handle Log out button
        binding.LogOut.setOnClickListener(v -> {
            Log.d("SettingsFragment", "Log Out Clicked");

            // Sign out from Firebase
            FirebaseAuth.getInstance().signOut();

            // Clear the shared Preferences
            SharedPreferences sharedpreferences = requireContext().getSharedPreferences("Profile", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean("isLoggedIn", false);
            editor.apply();

            // Navigate to the login Activity
            Intent intent = new Intent(requireContext(), loginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        });

        return root;
    }
    // Delete users data
    private void deleteUserFromFirestore(FirebaseUser user, FirebaseFirestore db, FirebaseAuth auth) {
        db.collection("users").document(user.getUid()).delete().addOnCompleteListener(deleteFirestoreTask -> {
            if (!deleteFirestoreTask.isSuccessful()) {
                Log.e("Firestore", "Failed to delete Firestore data: " + deleteFirestoreTask.getException().getMessage());
                Toast.makeText(requireContext(), "Error deleting Firestore data", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d("Firestore", "User data deleted from Firestore.");

            // Now delete the user account
            user.delete().addOnCompleteListener(deleteTask -> {
                if (!deleteTask.isSuccessful()) {
                    Log.e("Firebase", "Failed to delete user: " + deleteTask.getException().getMessage());
                    Toast.makeText(requireContext(), "Error: " + deleteTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.d("Firebase", "User account deleted.");
                Toast.makeText(requireContext(), "Account deleted successfully", Toast.LENGTH_SHORT).show();

                // Clear shared preferences
                SharedPreferences sharedPreferences = requireContext().getSharedPreferences("Profile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", false);
                editor.apply();

                // Redirect to log-in screen
                Intent intent = new Intent(requireContext(), loginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            });
        });
    }
// Extracts Firebase Storage file path from a given URL.
    private String extractStoragePath(String imageUrl) {
        try {
            Uri uri = Uri.parse(imageUrl);
            String path = uri.getPath();

            if (path != null) {
                // Extract the correct file path from the URL
                int index = path.indexOf("/o/");
                if (index != -1) {
                    String extractedPath = path.substring(index + 3); // Remove "/o/"
                    return extractedPath.replace("%2F", "/"); // Decode Firebase path encoding
                }
            }
        } catch (Exception e) {
            Log.e("Storage", "Error extracting path from URL: " + e.getMessage());
        }
        return null;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}