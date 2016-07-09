package com.sheepyang.mobilesafe.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sheepyang.mobilesafe.R;
import com.sheepyang.mobilesafe.db.dao.AddressDao;
import com.sheepyang.mobilesafe.utils.ToastUtil;
import com.sheepyang.mobilesafe.view.SettingView;

/**
 * Created by SheepYang on 2016/6/11 14:08.
 */
public class AddressActivity extends BaseActivity{
    @ViewInject(R.id.edt_address_phone)
    private EditText edt_address_phone;
    @ViewInject(R.id.tv_address_result)
    private TextView tv_address_result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        initView();
    }

    private void initView() {
        ViewUtils.inject(this);
    }

    public void queryAddress(View v) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edt_address_phone.getWindowToken(),0);
        String phone = edt_address_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            edt_address_phone.startAnimation(shake);
            //振动
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(500);//振动100毫秒,0.1秒
            ToastUtil.show(getApplicationContext(), "请输入要查询的手机号码");
            return;
        }
        String queryAddress = AddressDao.queryAddress(this, phone);
        if (!TextUtils.isEmpty(queryAddress)) {
            tv_address_result.setText("归属地：" + queryAddress);
        } else {
            tv_address_result.setText("没有查询到号码归属地");
        }
        tv_address_result.setVisibility(View.VISIBLE);
    }
}
