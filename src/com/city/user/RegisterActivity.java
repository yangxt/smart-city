package com.city.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.city.R;

public class RegisterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void to_login(View view) {
        pushToRegister();
    }

    private void pushToRegister() {
        Intent intent = new Intent(this, UserLoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_out, R.anim.push_right_in);
        this.finish();
    }

}
