package com.sheepyang.mobilesafe.ui.SetUp;

import android.content.Intent;
import android.os.Bundle;

import com.sheepyang.mobilesafe.R;

/**
 * Created by SheepYang on 2016/6/9 22:41.
 */
public class SetUp1Activity extends SetUpBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
    }

    @Override
    public void nextActivity() {
        Intent intent = new Intent(SetUp1Activity.this, SetUp2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.setup_enter_next, R.anim.setup_exit_next);
    }

    @Override
    public void preActivity() {
        finish();
        overridePendingTransition(R.anim.setup_enter_pre, R.anim.setup_exit_pre);
    }
}
