package com.sheepyang.mobilesafe.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sheepyang.mobilesafe.R;
import com.sheepyang.mobilesafe.ui.SetUp.SetUp1Activity;

/**
 * Created by SheepYang on 2016/6/9 18:50.
 */
public class LostfindActivity extends BaseActivity{
    private TextView tv_lostfind_safenum;
    private ImageView iv_lostfind_protected;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        if (sp.getBoolean("first", true)) {
            //第一次进入手机防盗模块，进入防盗设置界面
            Intent intent = new Intent(LostfindActivity.this, SetUp1Activity.class);
            startActivity(intent);
            finish();
        } else {
            //进入手机防盗模块界面
            setContentView(R.layout.activity_lostfind);
            initView();
        }
    }

    private void initView() {
        tv_lostfind_safenum = (TextView) findViewById(R.id.tv_lostfind_safenum);
        iv_lostfind_protected = (ImageView) findViewById(R.id.iv_lostfind_protected);

        tv_lostfind_safenum.setText(sp.getString("safenum", ""));
        if (sp.getBoolean("protected", false)) {
            //开启了防盗保护
             iv_lostfind_protected.setImageResource(R.drawable.lock);
        } else {
            //关闭了防盗保护
            iv_lostfind_protected.setImageResource(R.drawable.unlock);
        }
    }

    public void resetup(View v) {
        Intent intent = new Intent(LostfindActivity.this, SetUp1Activity.class);
        startActivity(intent);
        finish();
    }
}
