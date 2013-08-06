package com.city.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.city.ChatActivity;
import com.city.R;
import com.city.request.BaseRequest;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;


public class UserLoginActivity extends Activity {
    private String username;
    private String password;

    private String userID;

    private EditText mUser;
    private EditText mPassword;

    RequestParams params;
    private JSONObject response;

    protected static final int GUIUPDATEIDENTIFIER = 0x101;

    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            loginHandler();
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        mUser = (EditText) findViewById(R.id.login_usr_edit);
        mPassword = (EditText) findViewById(R.id.login_pwd_edit);
    }

    public void user_login(View v) {
        if ("".equals(mUser.getText().toString()) || "".equals(mPassword.getText().toString()))   //判断 帐号和密码
        {
            Toast.makeText(getApplicationContext(), "请输入", Toast.LENGTH_SHORT).show();
        }
        new Thread(new myThread()).start();
    }

    public void loginHandler() {
        username = mUser.getText().toString();
        password = mPassword.getText().toString();

        response = new JSONObject();
        params = new RequestParams();
        params.put("user_name", username);
        params.put("user_psw", password);

        BaseRequest.get("login", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, JSONObject jsonObject) {
                super.onSuccess(jsonObject);
                Message msg = new Message();
                msg.what = i;
                handleMessage(msg);
                response = jsonObject;
                try {
                    if (response.get("login").toString().equalsIgnoreCase("valid")) {
                        Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                        userID = response.get("userID").toString();
                        pushToChatActivity();
                    } else {
                        Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable, JSONObject jsonObject) {
                super.onFailure(throwable, jsonObject);
                Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
                return;
            }

        });
    }

    public void pushToRegister(View v) {
        Log.d("UserLoginActivity", "pushToRegister");
        Intent intent;
        intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_out, R.anim.push_right_in);
        this.finish();
    }

    private void pushToChatActivity() {
        Log.d("UserLoginActivity", "pushToMainActivity");
        Intent intent;
        intent = new Intent(this, ChatActivity.class);
        intent.putExtra("userID", userID);
        intent.putExtra("username", username);

        startActivity(intent);
        overridePendingTransition(R.anim.push_left_out, R.anim.push_right_in);
        this.finish();
    }

    class myThread implements Runnable {
        public void run() {
            Message message = new Message();
            message.what = UserLoginActivity.GUIUPDATEIDENTIFIER;
            UserLoginActivity.this.myHandler.sendMessage(message);
        }
    }
}
