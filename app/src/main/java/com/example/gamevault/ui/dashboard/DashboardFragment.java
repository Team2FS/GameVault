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
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.gamevault.R;
import com.example.gamevault.databinding.FragmentDashboardBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

        // Attach listeners for thumbnails
        setClickListener(binding.game1Thumbnail, "Call of Duty: Black Ops 6", R.drawable.black_ops_6);
        setClickListener(binding.game2Thumbnail, "Call of Duty: Warzone 2.0", R.drawable.cod_warzone_2);
        setClickListener(binding.game3Thumbnail, "Call of Duty: Modern Warfare 3", R.drawable.cod_modern_warfare_3);
        setClickListener(binding.game4Thumbnail, "Call of Duty: Modern Warfare 2", R.drawable.call_of_duty_modern_warfare_2);
        setClickListener(binding.game5Thumbnail, "Call of Duty: Black Ops 4", R.drawable.black_ops_4);
        setClickListener(binding.game6Thumbnail, "Call of Duty: Cold War", R.drawable.black_ops_cold_war);

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

    private void setClickListener(ImageView imageView, String gameName, int gameImageResId) {
        if (imageView != null) {
            imageView.setOnClickListener(view -> {
                // Haptic Feedback
                Vibrator vibrator = (Vibrator) requireContext().getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator != null && vibrator.hasVibrator()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(50);
                    }
                }

                // Show Toast Confirmation
                String shortGameName = gameName.replace("Call of Duty: ", "");
                Toast.makeText(getContext(), "Selected: " + shortGameName, Toast.LENGTH_SHORT).show();

                // Pass game details to GameModeFragment
                Bundle bundle = new Bundle();
                bundle.putString("gameTitle", gameName);
                bundle.putInt("gameImage", gameImageResId);

                Navigation.findNavController(view).navigate(R.id.action_dashboard_to_gameMode, bundle);
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
