package com.city;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.city.chat.ChatMsgEntity;
import com.city.chat.ChatMsgViewAdapter;
import com.city.request.BaseRequest;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.yunzhisheng.basic.USCRecognizerDialog;
import cn.yunzhisheng.basic.USCRecognizerDialogListener;
import cn.yunzhisheng.common.USCError;


public class ChatActivity extends Activity implements OnClickListener {
    private Button mBtnVoice;
    private Button mBtnSend;
    private EditText mEditTextContent;
    private ListView mListView;
    private ChatMsgViewAdapter mAdapter;
    private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();

    private String[] msgArray = new String[]{};

    private String[] dateArray = new String[]{};
    private final static int COUNT = 0;

    private String userID;
    private String username;
    private String message;

    private RequestParams params;
    private JSONObject response;

    protected static final int MSG_IDENTIFIER = 0x101;
    protected static final String USC_IDENTIFIER = "3hc7h4ctu6cafp4mngzoo55thar6xlfctjigrjia";
    private USCRecognizerDialog recognizer;

    Handler newHandler = new Handler() {
        public void handleMessage(Message msg) {
            chatHandler();
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        userID = (String) this.getIntent().getSerializableExtra("userID");
        username = (String) this.getIntent().getSerializableExtra("username");

        initView();
        initData();
        initVoice();

    }

    private void initVoice() {
        mBtnVoice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditTextContent.setText("");
                recognizer.show();
            }
        });

        recognizer = new USCRecognizerDialog(this, USC_IDENTIFIER);
        recognizer.setEngine("general");
        recognizer.setListener(new USCRecognizerDialogListener() {
            public void onResult(String result, boolean isLast) {
                mEditTextContent.append(result);
            }
            public void onEnd(USCError error) {
            }
        });
    }


    private void initView() {
        mBtnVoice = (Button) findViewById(R.id.btn_voice);
        mBtnVoice.setOnClickListener(this);
        mBtnSend = (Button) findViewById(R.id.btn_send);
        mBtnSend.setOnClickListener(this);
        mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
        mListView = (ListView) findViewById(R.id.listview);
    }

    private void initData() {

        for (int i = 0; i < COUNT; i++) {
            ChatMsgEntity entity = new ChatMsgEntity();
            entity.setDate(dateArray[i]);
            if (i % 2 == 0) {
                entity.setName("Smarter");
                entity.setMsgType(true);
            } else {
                entity.setName(username);
                entity.setMsgType(false);
            }

            entity.setText(msgArray[i]);
            mDataArrays.add(entity);
        }

        mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
        mListView.setAdapter(mAdapter);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_send) {
            new Thread(new newThread()).start();
        }
    }

    private void chatHandler() {

        message = mEditTextContent.getText().toString();

        if (message == null || message.equalsIgnoreCase("")) {
            return;
        }

        ChatMsgEntity entity = new ChatMsgEntity();
        entity.setDate(getDate());
        entity.setName(username);
        entity.setMsgType(false);
        entity.setText(message);

        mDataArrays.add(entity);
        mAdapter.notifyDataSetChanged();
        mListView.setSelection(mListView.getCount() - 1);

        response = new JSONObject();
        params = new RequestParams();
        params.put("user_id", userID);
        params.put("req_string", message);

        BaseRequest.get("main", params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(JSONObject jsonObject) {
                super.onSuccess(jsonObject);
                response = jsonObject;
                try

                {
                    String contString = response.get("response_str").toString();
                    Thread.sleep(100);
                    if (contString.length() > 0) {
                        ChatMsgEntity entity = new ChatMsgEntity();
                        entity.setDate(getDate());
                        entity.setName("客服");
                        entity.setMsgType(true);
                        entity.setText(contString);

                        mDataArrays.add(entity);
                        mAdapter.notifyDataSetChanged();
                        mEditTextContent.setText("");
                        mListView.setSelection(mListView.getCount() - 1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable, JSONObject jsonObject) {
                super.onFailure(throwable, jsonObject);
                Toast.makeText(getApplicationContext(), "NetWork Error", Toast.LENGTH_SHORT).show();
                return;
            }
        });

    }


    private String getDate() {
        Calendar c = Calendar.getInstance();

        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH));
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);
        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String min = String.valueOf(c.get(Calendar.MINUTE));


        StringBuffer sbBuffer = new StringBuffer();
        sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":" + min);


        return sbBuffer.toString();
    }

    class newThread implements Runnable {
        public void run() {
            Message message = new Message();
            message.what = ChatActivity.MSG_IDENTIFIER;
            ChatActivity.this.newHandler.sendMessage(message);
        }
    }

}
