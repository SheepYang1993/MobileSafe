package com.sheepyang.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sheepyang.mobilesafe.R;

/**
 * Created by SheepYang on 2016/6/9 13:06.
 */
public class SettingView extends RelativeLayout{
    private TextView tv_setting_item_title;
    private TextView tv_setting_item_des;
    private CheckBox cb_setting_update;
    private boolean checked;
    private String des_on;
    private String des_off;

    public SettingView(Context context) {
        super(context);
        init();
    }

    public SettingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        String title = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto","settingTitle");
        des_on =  attrs.getAttributeValue("http://schemas.android.com/apk/res-auto","des_on");
        des_off =  attrs.getAttributeValue("http://schemas.android.com/apk/res-auto","des_off");
        checked =  attrs.getAttributeBooleanValue("http://schemas.android.com/apk/res-auto","checked", false);
        tv_setting_item_title.setText(title);
        setChecked(checked);
    }

    public SettingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.view_setting, this);
        tv_setting_item_title =  (TextView) view.findViewById(R.id.tv_setting_item_title);
        tv_setting_item_des = (TextView) view.findViewById(R.id.tv_setting_item_des);
        cb_setting_update = (CheckBox) view.findViewById(R.id.cb_setting_update);
    }

    public void setTitle(String title) {
        tv_setting_item_title.setText(title);
    }

    public void setDes(String des) {
        tv_setting_item_des.setText(des);
    }

    public void setChecked(boolean checked) {
        cb_setting_update.setChecked(checked);
        if (isChecked()) {
            tv_setting_item_des.setText(des_on);
        } else {
            tv_setting_item_des.setText(des_off);
        }
    }

    public boolean isChecked() {
        return cb_setting_update.isChecked();
    }
}
