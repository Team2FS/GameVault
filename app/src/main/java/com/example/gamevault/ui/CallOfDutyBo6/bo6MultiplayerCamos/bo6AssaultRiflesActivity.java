package com.example.gamevault.ui.CallOfDutyBo6.bo6MultiplayerCamos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.gamevault.R;
import com.example.gamevault.ui.CallOfDutyBo6.bo6MultiplayerCamos.bo6WeaponChallenges.*;

public class bo6AssaultRiflesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bo6_assault_rifles);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //set click listeners for each AR
        findViewById(R.id.xm4_image).setOnClickListener(v -> navigateToWeaponChallenge(ArChallengeXM4MainActivity.class));
        findViewById(R.id.model_L_image).setOnClickListener(v -> navigateToWeaponChallenge(ArChallengeModelLMainActivity.class));
        findViewById(R.id.krigC_image).setOnClickListener(v -> navigateToWeaponChallenge(ArChallengeKrigCMainActivity.class));
        findViewById(R.id.ames85_image).setOnClickListener(v -> navigateToWeaponChallenge(ArChallengeAMES85MainActivity.class));
        findViewById(R.id.as_val_image).setOnClickListener(v -> navigateToWeaponChallenge(ArChallengeASVALMainActivity.class));
        findViewById(R.id.ak74_image).setOnClickListener(v -> navigateToWeaponChallenge(ArChallengeAK74MainActivity.class));
        findViewById(R.id.goblinMk2_image).setOnClickListener(v -> navigateToWeaponChallenge(ArChallengeGoblinMk2MainActivity.class));
        findViewById(R.id.gpr91_image).setOnClickListener(v -> navigateToWeaponChallenge(ArChallengeGPR91MainActivity.class));
    }

    private void navigateToWeaponChallenge(Class<?> targetActivity) {
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent);
    }
}
