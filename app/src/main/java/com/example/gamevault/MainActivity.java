package com.example.gamevault;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import com.example.gamevault.ui.login.loginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.gamevault.databinding.ActivityMainBinding;
import com.example.gamevault.FirebaseUtility;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedpreferences = getSharedPreferences("Profile", MODE_PRIVATE);
        boolean isLoggedIn = sharedpreferences.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) //if user is not logged in
        {
            Intent intent = new Intent(this, loginActivity.class); //create activity

            startActivity(intent); //start activity

            finish(); //finish activity, not going to continue on the rest of the page
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top-level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_social,R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // Read JSON from assets and upload to Firebase
        String jsonData = FirebaseUtility.readJSONFromAsset(this, "CamoData.json");
        if (jsonData != null) {
            FirebaseUtility.uploadJSONToFirebase(jsonData);
        } else {
            Log.e("MainActivity", "Failed to read JSON data");
        }

    }

    // Enable Toolbar back-button to navigate
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}
