package com.remu;

import android.os.Bundle;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.saber.chentianslideback.SlideBackActivity;

public class ChangeProfileActivity extends SlideBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);

        Animatoo.animateSlideLeft(this);

        setSlideBackDirection(SlideBackActivity.LEFT);
    }

    @Override
    protected void slideBackSuccess() { finish(); }

    @Override
    public void finish() {
        super.finish();
        Animatoo.animateSlideRight(this);
    }

}
