package com.sheepyang.mobilesafe.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by SheepYang on 2016/6/9 14:27.
 */
public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
}
