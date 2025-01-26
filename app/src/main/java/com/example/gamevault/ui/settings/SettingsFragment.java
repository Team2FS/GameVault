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
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.gamevault.databinding.FragmentSettingsBinding;
import com.example.gamevault.ui.login.loginActivity;
import com.example.gamevault.ui.settings.BuyUsACoffee.BuyUsACoffee;
import com.example.gamevault.ui.settings.manageprofile.ManageProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.example.gamevault.ui.settings.credits.Credits;
import com.example.gamevault.ui.settings.suggestfeature.SuggestFeatureActivity;

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
            //Use Fire base to delete the user data then call the same logic to set shared preferences and show the log out view.
            /*
            *********************************************************************
            * Logic for Fire base deleting account
            *********************************************************************
             */
            SharedPreferences sharedpreferences = requireContext().getSharedPreferences("Profile", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean("isLoggedIn", false);
            editor.apply();
            Intent intent = new Intent(requireContext(), loginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}