package com.zhangbin.paint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhangbin.paint.beans.StartBean;
import com.zhangbin.paint.constants.Constatans;
import com.zhangbin.paint.util.OperationUtils;
import com.zhangbin.paint.util.Util;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FirstActivity extends Activity {
    private String TAG = "--FirstActivity--";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        initView();
    }


    /**
     * 初始化操作
     */
    private void initView() {
        final Toast mToast = Toast.makeText(FirstActivity.this, "", Toast.LENGTH_LONG);
        final EditText userId = findViewById(R.id.et_id);
        final EditText userName = findViewById(R.id.et_name);
        final EditText mLiveId = findViewById(R.id.et_liveId);
        userId.setText("001");
        userName.setText("测试1");
        findViewById(R.id.btn_vip_look_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Id = userId.getText().toString().trim();
                final String Name = userName.getText().toString().trim();
                final String liveId = mLiveId.getText().toString().trim();
                if(Id.length() == 0 || Name.length() == 0 || mLiveId.length() == 0) {
                    mToast.setText("请输入用户ID  用户名称 URL");
                    mToast.show();
                }else {
                    //请求网络
                    String url = Constatans.liveIdUrl+"?liveId="+liveId+"&userType=1";
                    OkHttpClient okHttpClient = new OkHttpClient();
                    final Request request = new Request.Builder()
                            .url(url)
                            .get()//默认就是GET请求，可以不写
                            .build();
                    Call call = okHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d(TAG, "onFailure: ");
                            mToast.setText("网络请求失败,请重试");
                            mToast.show();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String body = response.body().string();
                            Gson gson = new Gson();
                            StartBean startBean = gson.fromJson(body, StartBean.class);
                            if (startBean.getMsg().equals("ok")){
                                OperationUtils.getInstance().mBoardHeight = Util.toInteger(startBean.getLiveInfo().getBoardHeight());
                                OperationUtils.getInstance().mBoardWidth = Util.toInteger(startBean.getLiveInfo().getBoardWidth());
                                Intent intent = new Intent(FirstActivity.this, MainActivity.class);
                                intent.putExtra(MainActivity.IS_VIP, true);
                                intent.putExtra(MainActivity.USER_ID,Id);
                                intent.putExtra(MainActivity.USER_NAME,Name);
                                String allIpAddress = startBean.getLiveInfo().getPullUrl()+startBean.getLiveInfo().getLiveId();
                                intent.putExtra(MainActivity.ALL_IP_ADDRESS,allIpAddress);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });
    }
}
