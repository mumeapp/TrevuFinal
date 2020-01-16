package com.remu;

import android.content.Intent;
import android.os.Bundle;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.saber.chentianslideback.SlideBackActivity;

public class FindFriendResultActivity extends SlideBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend_result);

        Animatoo.animateSlideLeft(this);

        setSlideBackDirection(SlideBackActivity.LEFT);
    }

    @Override
    protected void slideBackSuccess() {
        super.slideBackSuccess();

        overrideFinish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overrideFinish();
    }

    private void overrideFinish() {
        Animatoo.animateSlideRight(this);
        Intent intent = new Intent(FindFriendResultActivity.this, FindFriendActivity.class);
        intent.putExtra("sender", "FindFriendResult");
        startActivity(intent);
        finish();
    }

}
