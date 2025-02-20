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


        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}