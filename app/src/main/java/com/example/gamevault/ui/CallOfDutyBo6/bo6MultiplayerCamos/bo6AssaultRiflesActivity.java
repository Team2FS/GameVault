package com.example.gamevault.ui.CallOfDutyBo6.bo6MultiplayerCamos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.gamevault.R;
import com.example.gamevault.ui.CallOfDutyBo6.bo6MultiplayerCamos.bo6WeaponChallenges.ArChallengeXM4MainActivity;

import com.example.gamevault.ui.CallOfDutyBo6.bo6MultiplayerCamos.bo6WeaponChallenges.ArChallengeXM4MainActivity;
import com.example.gamevault.ui.CallOfDutyBo6.bo6MultiplayerCamos.bo6WeaponChallenges.ArChallengeModelLMainActivity;
import com.example.gamevault.ui.CallOfDutyBo6.bo6MultiplayerCamos.bo6WeaponChallenges.ArChallengeKrigCMainActivity;
import com.example.gamevault.ui.CallOfDutyBo6.bo6MultiplayerCamos.bo6WeaponChallenges.ArChallengeAMES85MainActivity;
import com.example.gamevault.ui.CallOfDutyBo6.bo6MultiplayerCamos.bo6WeaponChallenges.ArChallengeASVALMainActivity;
import com.example.gamevault.ui.CallOfDutyBo6.bo6MultiplayerCamos.bo6WeaponChallenges.ArChallengeAK74MainActivity;
import com.example.gamevault.ui.CallOfDutyBo6.bo6MultiplayerCamos.bo6WeaponChallenges.ArChallengeGoblinMk2MainActivity;
import com.example.gamevault.ui.CallOfDutyBo6.bo6MultiplayerCamos.bo6WeaponChallenges.ArChallengeGPR91MainActivity;

public class bo6AssaultRiflesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bo6_assault_rifles);

        //xm4 clickable image
        ImageView xm4ImageClickable = findViewById(R.id.xm4_image);

        //xm4 camo page
        xm4ImageClickable.setClickable(true);

        xm4ImageClickable.setOnClickListener(view -> {

            Intent intent = new Intent(this, ArChallengeXM4MainActivity.class);

            startActivity(intent);

        });

        //other AR rifle click listeners

        ImageView modelLImage = findViewById(R.id.model_L_image);
        modelLImage.setClickable(true);
        modelLImage.setOnClickListener(view -> {
            Intent intent = new Intent(this, ArChallengeModelLMainActivity.class);
            startActivity(intent);
        });

        ImageView krigCImage = findViewById(R.id.krigC_image);
        krigCImage.setClickable(true);
        krigCImage.setOnClickListener(view -> {
            Intent intent = new Intent(this, ArChallengeKrigCMainActivity.class);
            startActivity(intent);
        });

        ImageView ames85Image = findViewById(R.id.ames85_image);
        ames85Image.setClickable(true);
        ames85Image.setOnClickListener(view -> {
            Intent intent = new Intent(this, ArChallengeAMES85MainActivity.class);
            startActivity(intent);
        });

        ImageView asValImage = findViewById(R.id.as_val_image);
        asValImage.setClickable(true);
        asValImage.setOnClickListener(view -> {
            Intent intent = new Intent(this, ArChallengeASVALMainActivity.class);
            startActivity(intent);
        });

        ImageView ak74Image = findViewById(R.id.ak74_image);
        ak74Image.setClickable(true);
        ak74Image.setOnClickListener(view -> {
            Intent intent = new Intent(this, ArChallengeAK74MainActivity.class);
            startActivity(intent);
        });

        ImageView goblinMk2Image = findViewById(R.id.goblinMk2_image);
        goblinMk2Image.setClickable(true);
        goblinMk2Image.setOnClickListener(view -> {
            Intent intent = new Intent(this, ArChallengeGoblinMk2MainActivity.class);
            startActivity(intent);
        });

        ImageView gpr91Image = findViewById(R.id.gpr91_image);
        gpr91Image.setClickable(true);
        gpr91Image.setOnClickListener(view -> {
            Intent intent = new Intent(this, ArChallengeGPR91MainActivity.class);
            startActivity(intent);
        });


        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}