package com.example.gamevault.ui.CallOfDutyBo6.bo6MultiplayerCamos.bo6WeaponChallenges;

import android.os.Bundle;
import android.util.Log;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gamevault.R;
import com.google.firebase.firestore.*;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ArChallengeXM4MainActivity extends AppCompatActivity {

    private static final String TAG = "ArChallengeXM4";
    private FirebaseFirestore db;
    private DocumentReference camoProgressRef;
    private ListenerRegistration listener;

    private TextView graniteProgressText;
    private ImageView graniteIncrementButton;
    private ImageView graniteSubtractButton;
    private ImageView graniteCheckButton;
    private ImageView graniteFavButton;
    private ProgressBar graniteProgressBar;
    private TextView  woodlandProgressText;
    private ImageView  woodlandIncrementButton;
    private ImageView  woodlandSubtractButton;
    private ImageView woodlandCheckButton;
    private ImageView woodlandFavButton;
    private ProgressBar woodlandProgressBar;
    private TextView savannaProgressText;
    private ImageView savannaIncrementButton;
    private ImageView savannaSubtractButton;
    private ImageView savannaCheckButton;
    private ImageView savannaFavButton;
    private ProgressBar savannaProgressBar;
    private TextView splinterProgressText;
    private ImageView splinterIncrementButton;
    private ImageView  splinterSubtractButton;
    private ImageView splinterCheckButton;
    private ImageView splinterFavButton;
    private ProgressBar splinterProgressBar;
    private static final Map<String, Integer> CAMO_MAX_VALUES = new HashMap<>();

    static {
        CAMO_MAX_VALUES.put("granite", 5);
        CAMO_MAX_VALUES.put("woodland", 10);
        CAMO_MAX_VALUES.put("savanna", 15);
        CAMO_MAX_VALUES.put("splinter", 20);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ar_challenge_xm4_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Not logged in!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        String userId = user.getUid();

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();
        camoProgressRef = db.collection("camo_progress").document(userId).collection("assault_rifles").document("XM4");

        // Granite UI Elements
        graniteProgressText = findViewById(R.id.xm4graniteprogresstext);
        graniteIncrementButton = findViewById(R.id.xm4graniteadd);
        graniteSubtractButton = findViewById(R.id.xm4granitesubtract);
        graniteCheckButton = findViewById(R.id.xm4granitecheckbtn);
        graniteFavButton = findViewById(R.id.xm4granitefavbtn);
        graniteProgressBar = findViewById(R.id.xm4graniteprogressBar);

        // Woodland UI Elements
        woodlandProgressText = findViewById(R.id.xm4woodlandprogresstext);
        woodlandIncrementButton = findViewById(R.id.xm4woodlandadd);
        woodlandSubtractButton = findViewById(R.id.xm4woodlandsubtract);
        woodlandCheckButton = findViewById(R.id.xm4woodlandcheckbtn);
        woodlandFavButton = findViewById(R.id.xm4woodlandfavbtn);
        woodlandProgressBar = findViewById(R.id.xm4woodlandprogressBar);

        // Savanna UI Elements
        savannaProgressText = findViewById(R.id.xm4savannaprogresstext);
        savannaIncrementButton = findViewById(R.id.xm4savannaadd);
        savannaSubtractButton = findViewById(R.id.xm4savannasubtract);
        savannaCheckButton = findViewById(R.id.xm4savannacheckbtn);
        savannaFavButton = findViewById(R.id.xm4savannafavbtn);
        savannaProgressBar = findViewById(R.id.xm4savannaprogressBar);

        // Splinter UI Elements
        splinterProgressText = findViewById(R.id.xm4splinterprogresstext);
        splinterIncrementButton = findViewById(R.id.xm4splinteradd);
        splinterSubtractButton = findViewById(R.id.xm4splintersubtract);
        splinterCheckButton = findViewById(R.id.xm4splintercheckbtn);
        splinterFavButton = findViewById(R.id.xm4splinterfavbtn);
        splinterProgressBar = findViewById(R.id.xm4splinterprogressBar);


        // Load progress from Firestore
        loadProgress();

        // click listeners for granite camo
        graniteIncrementButton.setOnClickListener(v -> updateProgress("granite"));
        graniteSubtractButton.setOnClickListener(v -> subtractProgress("granite"));
        graniteCheckButton.setOnClickListener(v -> completeChallenge("granite"));
        graniteProgressBar.setOnClickListener(v -> completeChallenge("granite"));

        // click listeners for Woodland Camo
        woodlandIncrementButton.setOnClickListener(v -> updateProgress("woodland"));
        woodlandSubtractButton.setOnClickListener(v -> subtractProgress("woodland"));
        woodlandCheckButton.setOnClickListener(v -> completeChallenge("woodland"));
        woodlandProgressBar.setOnClickListener(v -> completeChallenge("woodland"));

        // click listeners for savanna Camo
        savannaIncrementButton.setOnClickListener(v -> updateProgress("savanna"));
        savannaSubtractButton.setOnClickListener(v -> subtractProgress("savanna"));
        savannaCheckButton.setOnClickListener(v -> completeChallenge("savanna"));
        savannaProgressBar.setOnClickListener(v -> completeChallenge("savanna"));


        // click listeners for Splinter camo
        splinterIncrementButton.setOnClickListener(v -> updateProgress("splinter"));
        splinterSubtractButton.setOnClickListener(v -> subtractProgress("splinter"));
        splinterCheckButton.setOnClickListener(v -> completeChallenge("splinter"));
        splinterProgressBar.setOnClickListener(v -> completeChallenge("splinter"));

    }

    private void loadProgress() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Not logged in!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        String userId = user.getUid();

        // Reference to the user's favorites document
        DocumentReference favoritesRef = db.collection("favorites").document(userId);

        // Load progress from Firestore
        listener = camoProgressRef.addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) {
                Log.e(TAG, "Error loading progress", e);
                return;
            }
            if (documentSnapshot == null || !documentSnapshot.exists()) {
                Log.w(TAG, "Document does not exist, initializing progress...");
                initializeCamoProgress();
                return;
            }

            // Update progress text and progress bars
            updateProgressText("granite", documentSnapshot);
            updateProgressText("woodland", documentSnapshot);
            updateProgressText("savanna", documentSnapshot);
            updateProgressText("splinter", documentSnapshot);
        });
    }


    private void updateProgressText(String camoName, DocumentSnapshot documentSnapshot) {
        Long progress = documentSnapshot.getLong(camoName);
        if (progress == null) {
            progress = 0L;
            camoProgressRef.update(camoName, progress);
        }

        // Update the TextView
        switch (camoName) {
            case "granite":
                graniteProgressText.setText("Progress: " + progress);
                graniteProgressBar.setMax(CAMO_MAX_VALUES.get(camoName));
                graniteProgressBar.setProgress(progress.intValue());
                break;
            case "woodland":
                woodlandProgressText.setText("Progress: " + progress);
                woodlandProgressBar.setMax(CAMO_MAX_VALUES.get(camoName));
                woodlandProgressBar.setProgress(progress.intValue());
                break;
            case "savanna":
                savannaProgressText.setText("Progress: " + progress);
                savannaProgressBar.setMax(CAMO_MAX_VALUES.get(camoName));
                savannaProgressBar.setProgress(progress.intValue());
                break;
            case "splinter":
                splinterProgressText.setText("Progress: " + progress);
                splinterProgressBar.setMax(CAMO_MAX_VALUES.get(camoName));
                splinterProgressBar.setProgress(progress.intValue());
                break;
            default:
                break;
        }
    }

    private void initializeCamoProgress() {
        Map<String, Object> initializeData = new HashMap<>();
        initializeData.put("granite", 0);
        initializeData.put("woodland", 0);
        initializeData.put("savanna", 0);
        initializeData.put("splinter", 0);
        camoProgressRef.set(initializeData, SetOptions.merge());
    }

    private void updateProgress(String camoName) {
        camoProgressRef.get().addOnSuccessListener(documentSnapshot -> {
            long currentProgress = documentSnapshot.exists() ? documentSnapshot.getLong(camoName) : 0;
            int maxProgress = CAMO_MAX_VALUES.getOrDefault(camoName, 100);

            long newProgress = Math.min(maxProgress, currentProgress + 1); // Ensure max limit

            camoProgressRef.update(camoName, newProgress)
                    .addOnSuccessListener(aVoid -> Toast.makeText(this, camoName + " progress updated!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Log.e(TAG, "Error updating " + camoName + " progress", e));
        });
    }



    private void subtractProgress(String camoName) {
        camoProgressRef.get().addOnSuccessListener(documentSnapshot -> {
            long currentProgress = documentSnapshot.exists() ? documentSnapshot.getLong(camoName) : 0;
            long newProgress = Math.max(0, currentProgress - 1);

            camoProgressRef.update(camoName, newProgress)
                    .addOnSuccessListener(aVoid -> Toast.makeText(this, camoName + " progress decreased!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Log.e(TAG, "Error decreasing " + camoName + " progress", e));
        });
    }

    private void completeChallenge(String camoName) {
        int maxProgress = CAMO_MAX_VALUES.getOrDefault(camoName, 100); // Default to 100 if not found

        camoProgressRef.update(camoName, maxProgress)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, camoName + " completed!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Log.e(TAG, "Error setting " + camoName + " to max progress", e));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (listener != null) listener.remove();
    }
}
