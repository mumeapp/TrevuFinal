package com.remu;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int ID_HOME = 1;
    private static final int ID_SAVED = 2;
    private static final int ID_FRIENDS = 3;
    private static final int ID_PROFILE = 4;

    private MeowBottomNavigation meowBottomNavigation;
    private NavController navController;
    private int currentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Animatoo.animateSlideLeft(this);

        meowBottomNavigation = findViewById(R.id.nav_view);
        meowBottomNavigation.add(new MeowBottomNavigation.Model(ID_HOME, R.drawable.ic_home_black_24dp));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(ID_SAVED, R.drawable.ic_dashboard_black_24dp));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(ID_FRIENDS, R.drawable.ic_notifications_black_24dp));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(ID_PROFILE, R.drawable.ic_search_black_24dp));

        meowBottomNavigation.show(1, true);
        currentID = ID_HOME;

        navController = Navigation.findNavController(this, R.id.main_nav_host_fragment);

        meowBottomNavigation.setOnClickMenuListener(model -> {
            switch (model.getId()) {
                case ID_HOME:
                    navController.navigate(R.id.navigation_home);
                    break;
                case ID_SAVED:
                    navController.navigate(R.id.navigation_saved);
                    break;
                case ID_FRIENDS:
                    navController.navigate(R.id.navigation_friends);
                    break;
                case ID_PROFILE:
                    navController.navigate(R.id.navigation_profile);
                    break;
            }
            currentID = model.getId();
            return null;
        });

        meowBottomNavigation.setOnShowListener(model -> {
            switch (model.getId()) {
                case ID_HOME:
                    navController.navigate(R.id.navigation_home);
                    break;
                case ID_SAVED:
                    navController.navigate(R.id.navigation_saved);
                    break;
                case ID_FRIENDS:
                    navController.navigate(R.id.navigation_friends);
                    break;
                case ID_PROFILE:
                    navController.navigate(R.id.navigation_profile);
                    break;
            }
            currentID = model.getId();
            return null;
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (currentID == 1) {
            finish();
        } else {
            meowBottomNavigation.show(1, true);
        }
    }
}
