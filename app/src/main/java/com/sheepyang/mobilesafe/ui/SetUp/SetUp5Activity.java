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
public class SetUp5Activity extends SetUpBaseActivity {
    @ViewInject(R.id.sv_setup5_protected)
    private SettingView sv_setup5_protected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup5);
        initView();
    }

    private void initView() {
        ViewUtils.inject(this);

        if (sp.getBoolean("protected", false)) {
            sv_setup5_protected.setChecked(true);
        } else {
            sv_setup5_protected.setChecked(false);
        }
        sv_setup5_protected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edit = sp.edit();
                if (sv_setup5_protected.isChecked()) {
                    sv_setup5_protected.setChecked(false);
                    edit.putBoolean("protected", false);
                } else {
                    sv_setup5_protected.setChecked(true);
                    edit.putBoolean("protected", true);
                }
                edit.commit();
            }
        });
    }

    @Override
    public void nextActivity() {
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean("first", false);
        edit.commit();
        Intent intent = new Intent(SetUp5Activity.this, LostfindActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.setup_enter_next, R.anim.setup_exit_next);
    }

    @Override
    public void preActivity() {
        Intent intent = new Intent(SetUp5Activity.this, SetUp4Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.setup_enter_pre, R.anim.setup_exit_pre);
    }
}
