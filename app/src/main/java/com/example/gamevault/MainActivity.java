package com.example.gamevault;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.gamevault.databinding.ActivityMainBinding;
import com.example.gamevault.ui.login.loginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if the user is logged in
        SharedPreferences sharedPreferences = getSharedPreferences("Profile", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            Intent intent = new Intent(this, loginActivity.class);
            startActivity(intent);
            finish(); // Close MainActivity since the user is redirected to login
        }

        // Inflate layout with ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_settings)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // ensure that switching tabs removes the GameModeFragment
        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
                int itemId = item.getItemId();

                //check if current destination is different from the selected item
                if (navController.getCurrentDestination() != null &&
                        navController.getCurrentDestination().getId() != itemId) {

                    // pop stack to ensure GameModeFragment is removed
                    navController.popBackStack(R.id.navigation_dashboard, false);

                    //navigate to selected tab
                    navController.navigate(itemId);
                }
                return true;
            }
        });

        // Firebase JSON handling
        String jsonData = FirebaseUtility.readJSONFromAsset(this, "CamoData.json");
        if (jsonData != null) {
            FirebaseUtility.uploadJSONToFirebase(jsonData);
        } else {
            Log.e("MainActivity", "Failed to read JSON data");
        }
    }
}
