package com.example.gamevault.ui.CallOfDutyBo6.unified;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gamevault.JSONReader;
import com.example.gamevault.R;
import com.example.gamevault.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class camoList extends AppCompatActivity {

    private RecyclerView camoListView;
    private camoListadapter adapter;
    private String weaponCategory;
    private String gunName;
    private int gunImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_weapons_list);

        // Get selected data from Intent
        weaponCategory = getIntent().getStringExtra("weapon_category");
        gunName = getIntent().getStringExtra("gun_name");
        gunImage = getIntent().getIntExtra("gun_image", 0);

        // Validate the data
        if (weaponCategory == null || gunName == null) {
            Toast.makeText(this, "Missing weapon category or gun name!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Set title
        TextView title = findViewById(R.id.gun_title);
        title.setText(gunName);

        // Apply usual window stuff
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.frameLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Setup RecyclerView
        camoListView = findViewById(R.id.gun_recycler_view);
        camoListView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch Firestore Data and Update UI
        fetchCamoData();
    }

    private void fetchCamoData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Not logged in!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String userId = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // ✅ Fix Firestore path formatting
        String gunType;
        switch (weaponCategory) {
            case "Assault Rifles": gunType = "assault_rifles"; break;
            case "Smg": gunType = "smg"; break;
            case "Shotguns": gunType = "shotguns"; break;
            case "Marksman Rifles": gunType = "marksman_rifles"; break;
            case "Sniper Rifles": gunType = "sniper_rifles"; break; // ✅ Fixed typo
            case "Lmgs": gunType = "lmgs"; break;
            default: gunType = "Unknown";
        }

        DocumentReference camoProgressRef = db.collection("camo_progress")
                .document(userId)
                .collection(gunType)
                .document(gunName);

        // ✅ Fetch Firestore Data & Update Adapter
        camoProgressRef.get().addOnSuccessListener(documentSnapshot -> {
            List<HomeFragment.ChallengeCard> camoList = new ArrayList<>();
            String description = "";
            Integer counter = 0;

            if (documentSnapshot.exists()) {
                for (String camoName : documentSnapshot.getData().keySet()) {
                    long progress = documentSnapshot.getLong(camoName) != null ? documentSnapshot.getLong(camoName) : 0;

                    JSONObject jsonObject = JSONReader.readJSONFromAssets(this, "CamoData.json");

                    // disect json to get rest of info



                    // "level 1 modes
                    JSONObject modeObject = jsonObject.optJSONObject("modes");
                    if (modeObject != null) {
                        // level 2: "mp"
                        JSONArray mpArray = modeObject.optJSONArray("mp");
                        if (mpArray != null && mpArray.length() > 0) {
                            // level 3: Get first object in "mp"
                            JSONObject firstMpEntry = mpArray.optJSONObject(0);
                            if (firstMpEntry != null) {
                                // level 4: Get "Assault Rifles" object
                                JSONArray weaponArray = firstMpEntry.optJSONArray("Assault Rifles");
                                if (weaponArray != null) {
                                    // level 5: Find the specific weapon
                                    for (int i = 0; i < weaponArray.length(); i++) {
                                        JSONObject weapon = weaponArray.optJSONObject(i);
                                        if (weapon != null && gunName.equals(weapon.optString("Name"))) {
                                            // level 6: get the Skins object values
                                            JSONObject skins = weapon.optJSONObject("Skins");
                                            if (skins != null) {
                                                // level 7 get camo details from skins
                                                JSONObject camoDetails = skins.optJSONObject(camoName);
                                                if (camoDetails != null) {
                                                    description = camoDetails.optString("description", "No description available");
                                                    counter = camoDetails.optInt("counter", 0);
                                                    System.out.println("Description: " + description + " | Counter: " + counter);
                                                } else {
                                                    System.out.println("Camo not found!");
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    System.out.println("Weapon category not found!");
                                }
                            }
                        }
                    }







                    camoList.add(new HomeFragment.ChallengeCard(
                            gunImage,
                            camoName,
                            description, // Placeholder
                            gunName,
                            gunType,
                            counter, // Placeholder
                            (int) progress,
                            false // Placeholder
                    ));
                }

                adapter = new camoListadapter(camoList);
                camoListView.setAdapter(adapter);

            } else {
                Toast.makeText(this, "No camo progress found! Inserting challenge", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Log.e("Firestore", "Error fetching camo progress", e);
            Toast.makeText(this, "Error loading data", Toast.LENGTH_SHORT).show();
        });
    }
}