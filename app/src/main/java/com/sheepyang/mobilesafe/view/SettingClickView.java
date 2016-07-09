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
public class SettingClickView extends RelativeLayout{
    private TextView tv_setting_item_title;
    private TextView tv_setting_item_des;

    public SettingClickView(Context context) {
        super(context);
        init();
    }

    public SettingClickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        String title = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto","settingClickTitle");
        String des = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto","des");
        tv_setting_item_title.setText(title);
        tv_setting_item_des.setText(des);
    }

    public SettingClickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.view_click_setting, this);
        tv_setting_item_title =  (TextView) view.findViewById(R.id.tv_setting_item_title);
        tv_setting_item_des = (TextView) view.findViewById(R.id.tv_setting_item_des);
    }

    public void setTitle(String title) {
        tv_setting_item_title.setText(title);
    }

    public void setDes(String des) {
        tv_setting_item_des.setText(des);
    }
}
