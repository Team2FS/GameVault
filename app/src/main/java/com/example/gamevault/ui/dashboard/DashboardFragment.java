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
import com.example.gamevault.R;
import com.example.gamevault.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //attach listeners for game selection
        setClickListener(binding.game1Thumbnail, "Call of Duty: Black Ops 6", R.drawable.black_ops_6);
        setClickListener(binding.game2Thumbnail, "Call of Duty: Warzone 2.0", R.drawable.cod_warzone_2);
        setClickListener(binding.game3Thumbnail, "Call of Duty: Modern Warfare 3", R.drawable.cod_modern_warfare_3);
        setClickListener(binding.game4Thumbnail, "Call of Duty: Modern Warfare 2", R.drawable.call_of_duty_modern_warfare_2);
        setClickListener(binding.game5Thumbnail, "Call of Duty: Black Ops 4", R.drawable.black_ops_4);
        setClickListener(binding.game6Thumbnail, "Call of Duty: Cold War", R.drawable.black_ops_cold_war);

        return root;
    }

    private void setClickListener(ImageView imageView, String gameName, int imageResId) {
        if (imageView != null) {
            imageView.setOnClickListener(view -> {
                //provide haptic feedback
                Vibrator vibrator = (Vibrator) requireContext().getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator != null && vibrator.hasVibrator()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(50);
                    }
                }

                //remove "Call of Duty" from message
                String shortGameName = gameName.replace("Call of Duty: ", "");

                //show toast message
                Toast.makeText(getContext(), "Tapped on " + shortGameName, Toast.LENGTH_SHORT).show();

                //navigate to GameModeFragment with game details
                Bundle bundle = new Bundle();
                bundle.putString("gameTitle", gameName);
                bundle.putInt("gameImage", imageResId);

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
