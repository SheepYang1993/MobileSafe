package com.sheepyang.mobilesafe.ui.SetUp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.sheepyang.mobilesafe.R;
import com.sheepyang.mobilesafe.utils.ToastUtil;
import com.sheepyang.mobilesafe.view.SettingView;

/**
 * Created by SheepYang on 2016/6/9 22:41.
 */
public class SetUp2Activity extends SetUpBaseActivity {
    private SettingView sv_setup2_sim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        initView();
    }

    private void initView() {
        sv_setup2_sim = (SettingView) findViewById(R.id.sv_setup2_sim);
        if (TextUtils.isEmpty(sp.getString("sim", ""))) {
            //没有绑定sim卡
            sv_setup2_sim.setChecked(false);
        } else {
            //已经绑定了sim卡
            sv_setup2_sim.setChecked(true);
        }
        sv_setup2_sim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edit = sp.edit();
                if (sv_setup2_sim.isChecked()) {
                    //解绑sim卡
                    edit.putString("sim", "");
                    sv_setup2_sim.setChecked(false);
                } else {
                    //绑定sim卡
                    TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    String sim = tm.getSimSerialNumber();
                    edit.putString("sim", sim);
                    sv_setup2_sim.setChecked(true);
                }
                edit.commit();
            }
        });
    }

    @Override
    public void nextActivity() {
        if (sv_setup2_sim.isChecked()) {
            Intent intent = new Intent(SetUp2Activity.this, SetUp3Activity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.setup_enter_next, R.anim.setup_exit_next);
        } else {
            ToastUtil.show(getApplicationContext(), "请绑定SIM卡，否则不能使用防盗功能");
            return;
        }
    }

    @Override
    public void preActivity() {
        Intent intent = new Intent(SetUp2Activity.this, SetUp1Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.setup_enter_pre, R.anim.setup_exit_pre);
    }
}
