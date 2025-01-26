package com.example.gamevault.ui.settings.BuyUsACoffee;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamevault.R;

public class BuyUsACoffee extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_us_acoffee); // Ensure this matches your XML file name

        // Initialize the button
        Button donateButton = findViewById(R.id.DonateButton); // Ensure this ID matches the button in your XML

        // Set up the click listener for the button
        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openKoFiWebsite();
            }
        });
    }

    private void openKoFiWebsite() {
        String koFiUrl = "https://ko-fi.com/GameVaultDevs"; // Ko-fi website URL
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(koFiUrl));
        startActivity(intent);
    }
}