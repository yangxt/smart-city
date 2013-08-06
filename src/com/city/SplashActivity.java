package com.city;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.city.user.UserLoginActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                pushToNextActivity();
            }
        }, 1000);
    }

    private void pushToNextActivity() {
        Intent intent = new Intent(this, UserLoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_out, R.anim.push_right_in);
        this.finish();
    }

}
