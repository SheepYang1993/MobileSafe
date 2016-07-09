package com.sheepyang.mobilesafe.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sheepyang.mobilesafe.R;

/**
 * Created by SheepYang on 2016/6/11 14:00.
 */
public class ToolsActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);
    }

    public void queryAddress(View v) {
        Intent intent = new Intent(this, AddressActivity.class);
        startActivity(intent);
    }
}
