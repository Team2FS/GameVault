package com.example.gamevault.ui.settings;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.gamevault.databinding.FragmentSettingsBinding;

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
        binding.ToggleTheme.setOnClickListener(v -> {
            Log.d("SettingsFragment", "Toggle Theme Clicked");
            // Handle toggle theme button
        });
        binding.ManageProfile.setOnClickListener(v -> {
            Log.d("SettingsFragment", "Manage Profile Clicked");
            // Handle toggle theme button
        });
        binding.ManageLinkedAccounts.setOnClickListener(v -> {
            Log.d("SettingsFragment", "Manage Linked Accounts Clicked");
            // Handle toggle theme button
        });
        binding.ManageCamoProgress.setOnClickListener(v -> {
            Log.d("SettingsFragment", "Manage Camo Progress Clicked");
            // Handle toggle theme button
        });
        binding.SuggestFeature.setOnClickListener(v -> {
            Log.d("SettingsFragment", "Suggest Feature Clicked");
            // Handle toggle theme button
        });
        binding.Donate.setOnClickListener(v -> {
            Log.d("SettingsFragment", "Donate Clicked");
            // Handle toggle theme button
        });
        binding.Credits.setOnClickListener(v -> {
            Log.d("SettingsFragment", "Credit Clicked");
            // Handle toggle theme button
        });
        binding.DeleteAccount.setOnClickListener(v -> {
            Log.d("SettingsFragment", "Delete Clicked");
            // Handle toggle theme button
        });
        binding.LogOut.setOnClickListener(v -> {
            Log.d("SettingsFragment", "Log Out Clicked");
            // Handle toggle theme button
        });

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}