package com.example.gamevault.ui.CallOfDutyBo6;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.gamevault.R;

//weapon pages
import com.example.gamevault.ui.CallOfDutyBo6.bo6MultiplayerCamos.bo6SMGSActivity;
import com.example.gamevault.ui.CallOfDutyBo6.bo6MultiplayerCamos.bo6AssaultRiflesActivity;
import com.example.gamevault.ui.CallOfDutyBo6.bo6MultiplayerCamos.bo6ShotgunsActivity;
import com.example.gamevault.ui.CallOfDutyBo6.bo6MultiplayerCamos.bo6LMGSActivity;
import com.example.gamevault.ui.CallOfDutyBo6.bo6MultiplayerCamos.bo6MarksmanRiflesActivity;
import com.example.gamevault.ui.CallOfDutyBo6.bo6MultiplayerCamos.bo6SnipersActivity;
import com.example.gamevault.ui.CallOfDutyBo6.bo6MultiplayerCamos.bo6HandgunsActivity;
import com.example.gamevault.ui.CallOfDutyBo6.bo6MultiplayerCamos.bo6LaunchersActivity;
import com.example.gamevault.ui.CallOfDutyBo6.bo6MultiplayerCamos.bo6SpecialsActivity;
import com.example.gamevault.ui.CallOfDutyBo6.bo6MultiplayerCamos.bo6MeleeWeaponsActivity;
import com.example.gamevault.ui.CallOfDutyBo6.bo6MultiplayerCamos.bo6MultiplayerExtras.bo6MultiExtrasActivity;


public class bo6MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //view page
        setContentView(R.layout.activity_bo6_main);
        //buttons
        Button smgsButton = findViewById(R.id.smgsBo6MultiCamosButton);
        Button assaultRifleButton = findViewById(R.id.assaultRifleBo6MultiCamosButton);
        Button shotgunButton = findViewById(R.id.shotgunBo6MultiCamosButton);
        Button lmgsButton = findViewById(R.id.lmgsBo6MultiCamoButton);
        Button marksmanButton = findViewById(R.id.marksmanRifleBo6MultiCamoButton);
        Button sniperButton = findViewById(R.id.sniperRifleBo6MultiCamoButton);
        Button pistolButton = findViewById(R.id.pistolBo6MultiCamoButton);
        Button launcherButton = findViewById(R.id.launcherBo6MultiCamoButton);
        Button specialButton = findViewById(R.id.specialBo6MultiCamoButton);
        Button meleeButton = findViewById(R.id.meleeBo6MultiCamoButton);
        Button extrasButton = findViewById(R.id.bo6ExtrasMultiButton);

        //smgs
        smgsButton.setOnClickListener(view -> {
            Toast.makeText(bo6MainActivity.this, "Selected: SMGS", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(bo6MainActivity.this, bo6SMGSActivity.class);
            startActivity(intent);
        });
        //assaultRifles
        assaultRifleButton.setOnClickListener(view -> {
            Toast.makeText(bo6MainActivity.this, "Selected: Assault Rifles", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(bo6MainActivity.this, bo6AssaultRiflesActivity.class);
            startActivity(intent);
        });
        //shotguns
        shotgunButton.setOnClickListener(view -> {
            Toast.makeText(bo6MainActivity.this, "Selected: Shotguns", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(bo6MainActivity.this, bo6ShotgunsActivity.class);
            startActivity(intent);
        });
        //lmgs
        lmgsButton.setOnClickListener(view -> {
            Toast.makeText(bo6MainActivity.this, "Selected: LMGs", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(bo6MainActivity.this, bo6LMGSActivity.class);
            startActivity(intent);
        });
        //marksmanRifles
        marksmanButton.setOnClickListener(view -> {
            Toast.makeText(bo6MainActivity.this, "Selected: Marksman Rifles", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(bo6MainActivity.this, bo6MarksmanRiflesActivity.class);
            startActivity(intent);
        });
        //snipers
        sniperButton.setOnClickListener(view -> {
            Toast.makeText(bo6MainActivity.this, "Selected: Snipers", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(bo6MainActivity.this, bo6SnipersActivity.class);
            startActivity(intent);
        });
        //pistols
        pistolButton.setOnClickListener(view -> {
            Toast.makeText(bo6MainActivity.this, "Selected: Pistols", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(bo6MainActivity.this, bo6HandgunsActivity.class);
            startActivity(intent);
        });
        //launchers
        launcherButton.setOnClickListener(view -> {
            Toast.makeText(bo6MainActivity.this, "Selected: Launchers", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(bo6MainActivity.this, bo6LaunchersActivity.class);
            startActivity(intent);
        });
        //specials
        specialButton.setOnClickListener(view -> {
            Toast.makeText(bo6MainActivity.this, "Selected: Specials", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(bo6MainActivity.this, bo6SpecialsActivity.class);
            startActivity(intent);
        });
        //melee
        meleeButton.setOnClickListener(view -> {
            Toast.makeText(bo6MainActivity.this, "Selected: Melee", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(bo6MainActivity.this, bo6MeleeWeaponsActivity.class);
            startActivity(intent);
        });
        //extras
        extrasButton.setOnClickListener(view -> {
            Toast.makeText(bo6MainActivity.this, "Selected: Extras", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(bo6MainActivity.this, bo6MultiExtrasActivity.class);
            startActivity(intent);
        });





        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}