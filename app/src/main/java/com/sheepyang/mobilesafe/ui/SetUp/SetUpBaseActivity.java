package com.sheepyang.mobilesafe.ui.SetUp;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.sheepyang.mobilesafe.receiver.AdminReceiver;
import com.sheepyang.mobilesafe.ui.BaseActivity;
import com.sheepyang.mobilesafe.ui.HomeActivity;

/**
 * Created by SheepYang on 2016/6/10 00:15.
 */
public abstract class SetUpBaseActivity extends BaseActivity{

    protected DevicePolicyManager devicePolicyManager;
    protected ComponentName componentName;
    private GestureDetector gestureDetector;
    protected SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //1.获取设备的管理者
        devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        //获取超级管理员标示
        componentName = new ComponentName(this, AdminReceiver.class);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        //监听手势
        gestureDetector =  new GestureDetector(this, new MyOnGestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float startX = e1.getRawX();
            float endX = e2.getRawX();
            float startY = e1.getRawY();
            float endY = e2.getRawY();
            if (Math.abs(startY - endY) > 50) {
                return false;
            }
            if ((startX - endX) > 100) {
                nextActivity();
            }
            if ((endX - startX) > 100) {
                preActivity();
            }
            return true;
        }
    }

    public void next(View v) {
        nextActivity();
    }

    public void pre(View v) {
        preActivity();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            preActivity();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void registerAdmin() {
        //代码激活超级管理员
        //设置激活超级管理员
        ComponentName componentName = new ComponentName(this, AdminReceiver.class);
        Intent intent2 = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        //设置激活那个超级管理员
        //mDeviceAdminSample : 超级管理员的标示
        intent2.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        //设置描述信息
        intent2.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"手机卫士,保卫您的手机");
        startActivity(intent2);
    }

    public abstract void nextActivity();
    public abstract void preActivity();
}
