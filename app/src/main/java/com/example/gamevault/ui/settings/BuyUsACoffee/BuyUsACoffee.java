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
        setContentView(R.layout.activity_buy_us_acoffee);


        Button donateButton = findViewById(R.id.DonateButton);


        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openKoFiWebsite();
            }
        });
    }

    private void openKoFiWebsite() {
        String koFiUrl = "https://ko-fi.com/GameVaultDevs";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(koFiUrl));
        startActivity(intent);
    }
}