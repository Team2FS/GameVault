package com.example.gamevault.ui.CallOfDutyBo6;

import android.content.Intent;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

import android.widget.Button;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.gamevault.R;

//weapon pages
import com.example.gamevault.ui.CallOfDutyBo6.unified.weaponsList;


public class bo6MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //view page
        setContentView(R.layout.activity_bo6_main);

        // store the buttons in a map with a description
        Map<Integer, String> buttonMap = new HashMap<>();
        buttonMap.put(R.id.smgsBo6MultiCamosButton, "SMGs");
        buttonMap.put(R.id.assaultRifleBo6MultiCamosButton, "Assault Rifles");
        buttonMap.put(R.id.shotgunBo6MultiCamosButton, "Shotguns");
        buttonMap.put(R.id.lmgsBo6MultiCamoButton, "LMGs");
        buttonMap.put(R.id.marksmanRifleBo6MultiCamoButton, "Marksman Rifles");
        buttonMap.put(R.id.sniperRifleBo6MultiCamoButton, "Sniper Rifles");
        buttonMap.put(R.id.pistolBo6MultiCamoButton, "Pistols");
        buttonMap.put(R.id.launcherBo6MultiCamoButton, "Launchers");
        buttonMap.put(R.id.specialBo6MultiCamoButton, "Specials");
        buttonMap.put(R.id.meleeBo6MultiCamoButton, "Melees");
        buttonMap.put(R.id.bo6ExtrasMultiButton, "Extras");


        // use a loop to create each button and onclick event

        for (Map.Entry<Integer, String> entry : buttonMap.entrySet()) {
            Button button = findViewById(entry.getKey());

            // is it a valid button
            if (button != null) {
                button.setOnClickListener(view -> {
                    Toast.makeText(bo6MainActivity.this, "Selected: " + entry.getValue(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, weaponsList.class);
                    intent.putExtra("weapon_category", entry.getValue());
                    startActivity(intent);
                });
            }
        }
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}