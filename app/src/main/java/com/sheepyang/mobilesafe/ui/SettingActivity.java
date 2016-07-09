package com.sheepyang.mobilesafe.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sheepyang.mobilesafe.R;
import com.sheepyang.mobilesafe.service.AddressService;
import com.sheepyang.mobilesafe.service.BlackNumService;
import com.sheepyang.mobilesafe.utils.ServiceUtil;
import com.sheepyang.mobilesafe.view.SettingClickView;
import com.sheepyang.mobilesafe.view.SettingView;

/**
 * Created by SheepYang on 2016/6/9 11:28.
 */
public class SettingActivity extends BaseActivity {
    @ViewInject(R.id.sv_setting_addressservice)
    private SettingView sv_setting_addressservice;
    @ViewInject(R.id.sv_setting_update)
    private SettingView sv_setting_update;
    @ViewInject(R.id.scv_setting_toaststyle)
    private SettingClickView scv_setting_toaststyle;
    @ViewInject(R.id.scv_setting_toastlocation)
    private SettingClickView scv_setting_toastlocation;
    @ViewInject(R.id.sv_setting_blacknum)
    private SettingView sv_setting_blacknum;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ServiceUtil.isRunningService(getApplicationContext(), "com.sheepyang.mobilesafe.service.AddressService")) {
            sv_setting_addressservice.setChecked(true);
        } else {
            sv_setting_addressservice.setChecked(false);
        }
        if (ServiceUtil.isRunningService(getApplicationContext(), "com.sheepyang.mobilesafe.service.BlackNumService")) {
            sv_setting_blacknum.setChecked(true);
        } else {
            sv_setting_blacknum.setChecked(false);
        }
    }

    private void initView() {
        ViewUtils.inject(this);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        update();
        address();
        blacknum();
        changedToastBackground();
        location();
    }

    private void blacknum() {
        sv_setting_blacknum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, BlackNumService.class);
                if (sv_setting_blacknum.isChecked()) {
                    sv_setting_blacknum.setChecked(false);
                    stopService(intent);
                } else {
                    sv_setting_blacknum.setChecked(true);
                    startService(intent);
                }
            }
        });
    }

    private void location() {
        scv_setting_toastlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, DragViewActivity.class);
                startActivity(intent);
            }
        });
    }

    private void changedToastBackground() {
        final String[] items={"半透明","活力橙","卫士蓝","金属灰","苹果绿"};
        //根据保存的选中的选项的索引值设置自定义组合控件描述信息回显操作
        scv_setting_toaststyle.setDes(items[sp.getInt("which", 0)]);
        //设置自定义组合控件的点击事件
        scv_setting_toaststyle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //弹出单选对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                //设置图标
                builder.setIcon(R.drawable.ic_launcher);
                //设置标题
                builder.setTitle("归属地提示框风格");
                //设置单选框
                //items : 选项的文本的数组
                //checkedItem : 选中的选项
                //listener : 点击事件
                //设置单选框选中选项的回显操作
                builder.setSingleChoiceItems(items, sp.getInt("which", 0), new DialogInterface.OnClickListener() {
                    //which : 选中的选项索引值
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SharedPreferences.Editor edit = sp.edit();
                        edit.putInt("which", which);
                        edit.commit();
                        //1.设置自定义组合控件描述信息文本
                        scv_setting_toaststyle.setDes(items[which]);//根据选中选项索引值从items数组中获取出相应文本,设置给描述信息控件
                        //2.隐藏对话框
                        dialog.dismiss();
                    }
                });
                //设置取消按钮
                builder.setNegativeButton("取消", null);//当点击按钮只是需要进行隐藏对话框的操作的话,参数2可以写null,表示隐藏对话框
                builder.show();
            }
        });
    }

    private void address() {
        sv_setting_addressservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, AddressService.class);
                if (sv_setting_addressservice.isChecked()) {
                    sv_setting_addressservice.setChecked(false);
                    stopService(intent);
                } else {
                    sv_setting_addressservice.setChecked(true);
                    startService(intent);
                }
            }
        });
    }

    private void update() {
        if (sp.getBoolean("update", true)) {
            sv_setting_update.setChecked(true);
        } else {
            sv_setting_update.setChecked(false);
        }
        sv_setting_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edit = sp.edit();
                if (sv_setting_update.isChecked()) {
                    sv_setting_update.setChecked(false);
                    edit.putBoolean("update", false);
                } else {
                    sv_setting_update.setChecked(true);
                    edit.putBoolean("update", true);
                }
                edit.commit();
            }
        });
    }
}
