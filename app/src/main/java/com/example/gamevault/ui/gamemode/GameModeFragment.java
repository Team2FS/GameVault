package com.example.gamevault.ui.gamemode;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.gamevault.R;
import com.example.gamevault.ui.CallOfDutyBo6.bo6MainActivity;
import com.example.gamevault.ui.CallOfDutyBo6.bo6ZombieMainActivity;
import com.example.gamevault.ui.CallOfDutyWarzone.warzoneMainActivity;

public class GameModeFragment extends Fragment {

    private String gameTitle;
    private int gameImageResId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_mode, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Handle Android System Back Button
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        Navigation.findNavController(view).navigateUp(); // Navigate back
                    }
                });

        // Get game title + image from arguments
        if (getArguments() != null) {
            gameTitle = getArguments().getString("gameTitle", "Unknown Game");
            gameImageResId = getArguments().getInt("gameImage", R.drawable.black_ops_4);
        }

        // Set game title and image
        TextView gameTitleTextView = view.findViewById(R.id.game_title);
        gameTitleTextView.setText(gameTitle);

        ImageView gameImageView = view.findViewById(R.id.game_image);
        gameImageView.setImageResource(gameImageResId);

        // Find buttons
        Button multiplayerButton = view.findViewById(R.id.btn_multiplayer);
        Button zombiesButton = view.findViewById(R.id.btn_zombies);
        Button warzoneButton = view.findViewById(R.id.btn_warzone);

        // Handle button clicks (Toast shows Mode instead of Game)
        multiplayerButton.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Selected: Multiplayer", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), bo6MainActivity.class);
            startActivity(intent);
        });

        zombiesButton.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Selected: Zombies", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), bo6ZombieMainActivity.class);
            startActivity(intent);
        });

        warzoneButton.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Selected: Warzone", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), warzoneMainActivity.class);
            startActivity(intent);
        });
        

        // Hide buttons if mode isn't available
        if (!gameSupportsZombies(gameTitle)) {
            zombiesButton.setVisibility(View.GONE);
        }
        if (!gameSupportsWarzone(gameTitle)) {
            warzoneButton.setVisibility(View.GONE);
        }



    }

    private void showMessage(String message) {
        android.widget.Toast.makeText(requireContext(), message, android.widget.Toast.LENGTH_SHORT).show();
    }

    private boolean gameSupportsZombies(String game) {
        return game.contains("Black Ops") || game.contains("Cold War");
    }

    private boolean gameSupportsWarzone(String game) {
        return game.contains("Warzone");
    }
}
