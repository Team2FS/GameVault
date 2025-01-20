package com.example.gamevault.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.gamevault.ui.login.loginActivity;

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
        });
        // Handle manage profile button
        binding.ManageProfile.setOnClickListener(v -> {
            Log.d("SettingsFragment", "Manage Profile Clicked");
        });
        // Handle manage linked accounts button
        binding.ManageLinkedAccounts.setOnClickListener(v -> {
            Log.d("SettingsFragment", "Manage Linked Accounts Clicked");
        });
        // Handle manage camo progress button
        binding.ManageCamoProgress.setOnClickListener(v -> {
            Log.d("SettingsFragment", "Manage Camo Progress Clicked");
        });
        // Handle Suggest Feature button
        binding.SuggestFeature.setOnClickListener(v -> {
            Log.d("SettingsFragment", "Suggest Feature Clicked");
        });
        // Handle Donate button
        binding.Donate.setOnClickListener(v -> {
            Log.d("SettingsFragment", "Donate Clicked");
        });
        // Handle Credits button
        binding.Credits.setOnClickListener(v -> {
            Log.d("SettingsFragment", "Credit Clicked");
        });
        // Handle Delete button
        binding.DeleteAccount.setOnClickListener(v -> {
            Log.d("SettingsFragment", "Delete Clicked");
        });
        // Handle Log out button
        binding.LogOut.setOnClickListener(v -> {
            Log.d("SettingsFragment", "Log Out Clicked");
            SharedPreferences sharedpreferences = requireContext().getSharedPreferences("Profile", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean("isLoggedIn", false);
            editor.apply();
            Intent intent = new Intent(requireContext(), loginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        });

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}