package com.example.gamevault.ui.dashboard;

import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.gamevault.databinding.FragmentDashboardBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Load profile picture
        loadProfilePicture();

        //attach listeners for haptic feedback
        setClickListener(binding.game1Thumbnail, "Call of Duty: Black Ops 6");
        setClickListener(binding.game2Thumbnail, "Call of Duty: Warzone 2.0");
        setClickListener(binding.game3Thumbnail, "Call of Duty: Modern Warfare 3");
        setClickListener(binding.game4Thumbnail, "Call of Duty: Modern Warfare 2");
        setClickListener(binding.game5Thumbnail, "Call of Duty: Black Ops 4");
        setClickListener(binding.game6Thumbnail, "Call of Duty: Cold War");

        return root;
    }

    private void loadProfilePicture() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(user.getUid()).get().addOnSuccessListener(document -> {
                if (document.exists() && document.contains("imageUrl")) {
                    String imageUrl = document.getString("imageUrl");

                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Glide.with(this)
                                .load(imageUrl)
                                .placeholder(android.R.drawable.ic_menu_gallery)
                                .error(android.R.drawable.ic_menu_report_image)
                                .into(binding.profileImageView);
                    }
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(requireContext(), "Failed to load profile picture", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void setClickListener(ImageView imageView, String gameName) {
        if (imageView != null) {
            imageView.setOnClickListener(view -> {
                //provide haptic feedback
                Vibrator vibrator = (Vibrator) requireContext().getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator != null && vibrator.hasVibrator()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        //for API 26+
                        vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //for older devices
                        vibrator.vibrate(50);
                    }
                }

                //remove "Call of Duty" from the message
                String shortGameName = gameName.replace("Call of Duty: ", "");

                //show toast message
                Toast.makeText(getContext(), "Tapped on " + shortGameName, Toast.LENGTH_SHORT).show();
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
