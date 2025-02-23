package com.example.gamevault.ui.CallOfDutyBo6.unified;

import android.os.Bundle;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gamevault.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class weaponsList extends AppCompatActivity {

    private Map<String, List<mainGunCard>> weaponMap = new HashMap<>();
    private RecyclerView gunListRecyclerView;
    private gunListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_weapons_list);

        // start the setup of the list of guns
        initializeWeapons();

        // Get selected category from previous activity
        String weaponCategory = getIntent().getStringExtra("weapon_category");
        TextView title = findViewById(R.id.gun_title);
        title.setText(weaponCategory);

        // Apply usual window stuff
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.frameLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        gunListRecyclerView = findViewById(R.id.gun_recycler_view);
        gunListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Generate weapon cards based on category
        List<mainGunCard> cards = generateCards(weaponCategory);


        if (gunListRecyclerView != null) {
            adapter = new gunListAdapter(this, cards, weaponCategory);
            gunListRecyclerView.setAdapter(adapter);
        }
    }

    private void initializeWeapons() {
        // smgs array list add new guns here for smgs
        List<mainGunCard> smgs = new ArrayList<>();
        smgs.add(new mainGunCard(R.drawable.c9,"C9"));
        smgs.add(new mainGunCard(R.drawable.jackal,"JACKAL PDW"));
        smgs.add(new mainGunCard(R.drawable.kompakt_92,"KOMPAKT 92"));
        smgs.add(new mainGunCard(R.drawable.ksv,"KSV"));
        smgs.add(new mainGunCard(R.drawable.pp_919,"PP-919"));
        smgs.add(new mainGunCard(R.drawable.tanto_22,"TANTO .22"));
        smgs.add(new mainGunCard(R.drawable.saug_2,"SAUG"));
        smgs.add(new mainGunCard(R.drawable.ppsh_41,"PPSH-41"));

        List<mainGunCard> assaultRifles = new ArrayList<>();
        assaultRifles.add(new mainGunCard(R.drawable.ak_74, "AK-74"));
        assaultRifles.add(new mainGunCard(R.drawable.ames_85, "AMES 85"));
        assaultRifles.add(new mainGunCard(R.drawable.as, "AS VAL"));
        assaultRifles.add(new mainGunCard(R.drawable.goblin_mk2, "GOLBIN MK2"));
        assaultRifles.add(new mainGunCard(R.drawable.gpr_91, "GPR 91"));
        assaultRifles.add(new mainGunCard(R.drawable.model_l, "Model-L"));
        assaultRifles.add(new mainGunCard(R.drawable.xm4, "XM4"));
        assaultRifles.add(new mainGunCard(R.drawable.krig_c_2, "KRIG C"));
        assaultRifles.add(new mainGunCard(R.drawable.cypher_091, "CYPHER 091"));

        List<mainGunCard> shotguns = new ArrayList<>();
        shotguns.add(new mainGunCard(R.drawable.asg_89, "ASG-89"));
        shotguns.add(new mainGunCard(R.drawable.marine, "MARINE SP"));
        shotguns.add(new mainGunCard(R.drawable.maelstrom, "MAELSTROM"));

        List<mainGunCard> lmgs = new ArrayList<>();
        lmgs.add(new mainGunCard(R.drawable.gpmg_, "GPMG-7"));
        lmgs.add(new mainGunCard(R.drawable.pu_21, "PU-21"));
        lmgs.add(new mainGunCard(R.drawable.xmg, "XMG"));
        lmgs.add(new mainGunCard(R.drawable.feng_82, "FENG 82"));

        List<mainGunCard> marksmanRifles = new ArrayList<>();
        marksmanRifles.add(new mainGunCard(R.drawable.aek_973,"AEK-973"));
        marksmanRifles.add(new mainGunCard(R.drawable.dm_10,"DM-10"));
        marksmanRifles.add(new mainGunCard(R.drawable.swat_556,"SWAT 5.56"));
        marksmanRifles.add(new mainGunCard(R.drawable.tsarkov_762,"TSARKOV 7.62"));

        List<mainGunCard> sniperRifles = new ArrayList<>();
        sniperRifles.add(new mainGunCard(R.drawable.amr_mod_4,"AMR MOD 4"));
        sniperRifles.add(new mainGunCard(R.drawable.lr_762,"LR 7.62"));
        sniperRifles.add(new mainGunCard(R.drawable.lw3a1_frostline,"LW3A1 FROSTLINE"));
        sniperRifles.add(new mainGunCard(R.drawable.svd,"SVD"));

        List<mainGunCard> pistols = new ArrayList<>();
        pistols.add(new mainGunCard(R.drawable.mm9, "9MM PM"));
        pistols.add(new mainGunCard(R.drawable.gs45,"GS45"));
        pistols.add(new mainGunCard(R.drawable.grekhova,"GREKHOVA"));
        pistols.add(new mainGunCard(R.drawable.stryder_22,"STRYER .22"));

        List<mainGunCard> launchers = new ArrayList<>();
        launchers.add(new mainGunCard(R.drawable.cigma_2b,"CIGNMA 2B"));
        launchers.add(new mainGunCard(R.drawable.he_1,"HE-1"));


        List<mainGunCard> specials = new ArrayList<>();
        specials.add(new mainGunCard(R.drawable.sirin_9mm,"SIRIN 9mm"));

        List<mainGunCard> melees = new ArrayList<>();
        //melees.add(new mainGunCard(R.drawable.baseball,"Baseball Bat"));
        melees.add(new mainGunCard(R.drawable.knife,"Knife"));
        melees.add(new mainGunCard(R.drawable.power_drill,"Power Drill"));

        // Add categories to the weaponMap
        weaponMap.put("SMGs", smgs);
        weaponMap.put("Assault Rifles", assaultRifles);
        weaponMap.put("Shotguns", shotguns);
        weaponMap.put("LMGs", lmgs);
        weaponMap.put("Marksman Rifles", marksmanRifles);
        weaponMap.put("Sniper Rifles", sniperRifles);
        weaponMap.put("Pistols", pistols);
        weaponMap.put("Launchers", launchers);
        weaponMap.put("Specials", specials);
        weaponMap.put("Melees", melees);
    }


    //Generate the list of weapon cards based on the category.
    private List<mainGunCard> generateCards(String category) {
        return weaponMap.getOrDefault(category, new ArrayList<>());
    }


     //class for weapon cards.
    public static class mainGunCard {
        private Integer imageId;
        private String mainGunName;

        public mainGunCard(Integer imageId, String mainGunName) {
            this.imageId = imageId;
            this.mainGunName = mainGunName;
        }

        public Integer getImageId() {
            return imageId;
        }

        public String getMainGunName() {
            return mainGunName;
        }
    }
}