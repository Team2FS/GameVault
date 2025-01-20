package com.example.gamevault.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.gamevault.MainActivity;
import com.example.gamevault.R;
import com.google.android.material.textfield.TextInputEditText;


//logic for all login email and password entry so that it gets sent over to firebase

public class loginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        //logic for user email and password
        EditText emailField = findViewById(R.id.emailTextField);
        EditText passwordField = findViewById(R.id.passwordTextField);

        //Buttons in order
        Button loginButton = findViewById(R.id.loginButton);
        Button signUpButton = findViewById(R.id.signUpButton);

        //listeners
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //email string
                String email = emailField.getText().toString();
                //password string
                String password = passwordField.getText().toString();

                //-----------------------------------------------------
                //space for implement firebase authentication for JAKE
                //-----------------------------------------------------

                //if email and password equals administrator(user) then they can be logged into app
                if(email.equals("admin") && password.equals("admin"))
                {
                    Intent intent = new Intent(loginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                    //makes sure user is logged in after the first login
                    SharedPreferences sharedpreferences = getSharedPreferences("Profile", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.apply();

                } else if(email.isEmpty() || password.isEmpty())
                {
                    Toast.makeText(loginActivity.this, "Error: Email or Password is blank. Try again.", Toast.LENGTH_SHORT).show();

                }else
                {
                    Toast.makeText(loginActivity.this, "Email or Password is invalid.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}