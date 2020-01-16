package com.remu;

import android.os.Bundle;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.remu.POJO.FragmentChangeListener;
import com.saber.chentianslideback.SlideBackActivity;

public class FindFriendActivity extends SlideBackActivity implements FragmentChangeListener {

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);

        if (getIntent().getStringExtra("sender").equals("FindFriendResult")) {
            Animatoo.animateSlideRight(this);
        } else {
            Animatoo.animateSlideLeft(this);
        }

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_findfriends);

        setSlideBackDirection(SlideBackActivity.LEFT);
    }

    @Override
    protected void slideBackSuccess() {
        super.slideBackSuccess();

        finish();
    }

    @Override
    public void finish() {
        super.finish();

        Animatoo.animateSlideRight(this);
    }

    @Override
    public void replaceFragment(int idFragment) {
        switch (idFragment) {
            case 1:
                navController.navigate(R.id.nav_findfriend1);
                break;
            case 2:
                navController.navigate(R.id.nav_findfriend2);
                break;
            case 3:
                navController.navigate(R.id.nav_findfriend3);
                break;
        }
    }
}
