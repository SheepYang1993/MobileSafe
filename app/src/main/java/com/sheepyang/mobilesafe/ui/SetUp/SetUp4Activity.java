package com.sheepyang.mobilesafe.ui.SetUp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sheepyang.mobilesafe.R;
import com.sheepyang.mobilesafe.ui.LostfindActivity;
import com.sheepyang.mobilesafe.view.SettingView;

/**
 * Created by SheepYang on 2016/6/9 22:41.
 */
public class SetUp4Activity extends SetUpBaseActivity {
    @ViewInject(R.id.sv_setup4_data)
    private SettingView sv_setup4_data;
    @ViewInject(R.id.sv_setup4_lockscreen)
    private SettingView sv_setup4_lockscreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        initView();
    }

    private void initView() {
        ViewUtils.inject(this);
        if (sp.getBoolean("remotedata", false)) {
            sv_setup4_data.setChecked(true);
        } else {
            sv_setup4_data.setChecked(false);
        }
        sv_setup4_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断超级管理员是否激活
                if (!devicePolicyManager.isAdminActive(componentName)) {
                    registerAdmin();
                    return;
                }
                SharedPreferences.Editor edit = sp.edit();
                if (sv_setup4_data.isChecked()) {
                    sv_setup4_data.setChecked(false);
                    edit.putBoolean("remotedata", false);
                } else {
                    sv_setup4_data.setChecked(true);
                    edit.putBoolean("remotedata", true);
                }
                edit.commit();
            }
        });
        if (sp.getBoolean("lockscreen", false)) {
            sv_setup4_lockscreen.setChecked(true);
        } else {
            sv_setup4_lockscreen.setChecked(false);
        }
        sv_setup4_lockscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断超级管理员是否激活
                if (!devicePolicyManager.isAdminActive(componentName)) {
                    registerAdmin();
                    return;
                }
                SharedPreferences.Editor edit = sp.edit();
                if (sv_setup4_lockscreen.isChecked()) {
                    sv_setup4_lockscreen.setChecked(false);
                    edit.putBoolean("lockscreen", false);
                } else {
                    sv_setup4_lockscreen.setChecked(true);
                    edit.putBoolean("lockscreen", true);
                }
                edit.commit();
            }
        });
    }

    @Override
    public void nextActivity() {
        Intent intent = new Intent(SetUp4Activity.this, SetUp5Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.setup_enter_next, R.anim.setup_exit_next);
    }

    @Override
    public void preActivity() {
        Intent intent = new Intent(SetUp4Activity.this, SetUp3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.setup_enter_pre, R.anim.setup_exit_pre);
    }
}
