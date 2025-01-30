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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.gamevault.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        //attach listeners for haptic feedback
        setClickListener(binding.game1Thumbnail, "Call of Duty: Black Ops 6");
        setClickListener(binding.game2Thumbnail, "Call of Duty: Warzone 2.0");
        setClickListener(binding.game3Thumbnail, "Call of Duty: Modern Warfare 3");
        setClickListener(binding.game4Thumbnail, "Call of Duty: Modern Warfare 2");
        setClickListener(binding.game5Thumbnail, "Call of Duty: Black Ops Cold War");
        setClickListener(binding.game6Thumbnail, "Call of Duty: Black Ops 4");

        return root;
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

                //show toast message
                Toast.makeText(getContext(), "Tapped on " + gameName, Toast.LENGTH_SHORT).show();
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
