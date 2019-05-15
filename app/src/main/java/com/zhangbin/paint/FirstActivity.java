package com.zhangbin.paint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tencent.bugly.crashreport.CrashReport;

public class FirstActivity extends Activity{
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
        userId.setText("001");
        userName.setText("测试1");
        findViewById(R.id.btn_vip_look_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Id = userId.getText().toString().trim();
                String Name = userName.getText().toString().trim();
                if(Id.length() == 0 || Name.length() == 0) {
                    mToast.setText("请输入用户ID和用户名称");
                    mToast.show();
                }else {
                    Intent intent = new Intent(FirstActivity.this, MainActivity.class);
                    intent.putExtra(MainActivity.IS_VIP, true);
                    intent.putExtra(MainActivity.USER_ID,Id);
                    intent.putExtra(MainActivity.USER_NAME,Name);
                    startActivity(intent);
                }
            }
        });
    }
}
