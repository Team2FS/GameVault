package com.example.gamevault.ui.settings.suggestfeature;

import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.gamevault.R;

public class SuggestFeatureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_suggest_feature);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button SendResponse = findViewById(R.id.SubmitButton);
        RadioButton BugRadioButton = findViewById(R.id.BugButton);
        RadioButton FeatureRadioButton = findViewById(R.id.FeatureButton);
        EditText ResponseField = findViewById(R.id.ResponseField);

        SendResponse.setOnClickListener(v -> {
        String Response = ResponseField.getText().toString();
        String Type;

        if (BugRadioButton.isChecked()) {
            Type = "Bug";
        }
        else if (FeatureRadioButton.isChecked()) {
            Type = "Feature Request";
        }
        else {
            Type = "";
            Toast.makeText(SuggestFeatureActivity.this, "Select button", Toast.LENGTH_SHORT);
        }


        if (!FeatureRadioButton.isChecked() && !BugRadioButton.isChecked()) {
            Toast.makeText(SuggestFeatureActivity.this, "Please select Bug or Feature", Toast.LENGTH_SHORT).show();
        }
        else {
            DiscordWebhook discordWebhook = new DiscordWebhook();
            String temp = Type + " : " + Response;
            Log.d("Sending", temp);
            discordWebhook.sendMessage(temp);

            Toast.makeText(SuggestFeatureActivity.this, "Message Sent!", Toast.LENGTH_SHORT).show();
        }
        });



    }
}