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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth; // Firebase Authentication instance
    private boolean isSignUpMode = false; // Flag to toggle between login and sign-up

    @Override
    public void onStart() {
        super.onStart();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Check SharedPreferences for login state
        SharedPreferences sharedPreferences = getSharedPreferences("Profile", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            // If user is logged in, navigate to MainActivity
            reload();
        } else {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                // If Firebase user exists but SharedPreferences doesn't match, update it
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", true);
                editor.apply();

                reload();
            }
        }
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            reload();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Logic for user email and password
        EditText emailField = findViewById(R.id.emailTextField);
        EditText passwordField = findViewById(R.id.passwordTextField);

        // Buttons in order
        Button loginButton = findViewById(R.id.loginButton);
        Button signUpButton = findViewById(R.id.signUpButton);

        // Listeners
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSignUpMode) {
                    // Handle sign-up logic
                    String email = emailField.getText().toString().trim();
                    String password = passwordField.getText().toString().trim();

                    if (email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(loginActivity.this, "Error: Email or Password is blank. Try again.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Create user with Firebase
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // Sign-up success
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(loginActivity.this, "Sign-up successful! Please log in.", Toast.LENGTH_SHORT).show();

                                    // Switch back to login mode
                                    isSignUpMode = false;
                                    loginButton.setText("Login");
                                    signUpButton.setText("Sign Up");
                                } else {
                                    // If sign-up fails, display a message to the user.
                                    Toast.makeText(loginActivity.this, "Sign-up failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    // Handle login logic
                    String email = emailField.getText().toString().trim();
                    String password = passwordField.getText().toString().trim();

                    if (email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(loginActivity.this, "Error: Email or Password is blank. Try again.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Admin override
                    if (email.equals("admin") && password.equals("admin")) {
                        Toast.makeText(loginActivity.this, "Admin login successful!", Toast.LENGTH_SHORT).show();

                        // Navigate to MainActivity
                        Intent intent = new Intent(loginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                        // Save login state
                        SharedPreferences sharedPreferences = getSharedPreferences("Profile", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.apply();
                        return; // Exit the method after admin login
                    }

                    // Authenticate user with Firebase
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // Sign in success
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(loginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                                    // Navigate to MainActivity
                                    Intent intent = new Intent(loginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                    // Save login state
                                    SharedPreferences sharedPreferences = getSharedPreferences("Profile", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean("isLoggedIn", true);
                                    editor.apply();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(loginActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        signUpButton.setOnClickListener(v -> {
            // Toggle between login and sign-up modes
            isSignUpMode = !isSignUpMode;
            if (isSignUpMode) {
                loginButton.setText("Sign Up");
                signUpButton.setText("Back to Login");
            } else {
                loginButton.setText("Login");
                signUpButton.setText("Sign Up");
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void reload() {
        // Ensure we only navigate if the activity is not already finishing
        if (!isFinishing()) {
            Toast.makeText(this, "User already logged in!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish(); // Close the current activity to prevent returning to it
        }
    }
}