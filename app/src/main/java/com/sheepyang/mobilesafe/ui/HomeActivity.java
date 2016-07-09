package com.sheepyang.mobilesafe.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.sheepyang.mobilesafe.R;
import com.sheepyang.mobilesafe.utils.MD5Util;
import com.sheepyang.mobilesafe.utils.ToastUtil;

/**
 * Created by SheepYang on 2016/6/8 22:11.
 */
public class HomeActivity extends BaseActivity {
    private GridView gv_home_gridview;
    private AlertDialog dialog;
    private SharedPreferences sp;
    private boolean isPasswordSee = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        sp = getSharedPreferences("config", MODE_PRIVATE);
    }

    private void initView() {
        gv_home_gridview = (GridView) findViewById(R.id.gv_home_gridview);
        gv_home_gridview.setAdapter(new MyAdapter());
        gv_home_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position) {
                    case 0://手机防盗
                        if (TextUtils.isEmpty(sp.getString("password", ""))) {
                            //第一次设置密码
                            showSetPassWordDialog();
                        } else {
                            //已经有了密码，输入密码进入模块
                            showEnterPassWordDialog();
                        }
                        break;
                    case 1://通讯卫士
                        Intent intent1 = new Intent(HomeActivity.this, CallSmsSafeActivity.class);
                        startActivity(intent1);
                        break;
                    case 2://软件管理
                        Intent intent2 = new Intent(HomeActivity.this, SoftManagerActivity.class);
                        startActivity(intent2);
                        break;
                    case 3://进程管理
                            Intent intent3 = new Intent(HomeActivity.this, TaskManagerActivity.class);
                        startActivity(intent3);
                        break;
                    case 7://高级工具
                        Intent intent7 = new Intent(HomeActivity.this, ToolsActivity.class);
                        startActivity(intent7);
                        break;
                    case 8://设置中心
                        Intent intent8 = new Intent(HomeActivity.this, SettingActivity.class);
                        startActivity(intent8);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void showEnterPassWordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        View view = View.inflate(getApplicationContext(), R.layout.dialog_enterpassword, null);
        final EditText edt_enterpassword_password = (EditText) view.findViewById(R.id.edt_enterpassword_password);
        Button btn_enterpassword_ok = (Button) view.findViewById(R.id.btn_enterpassword_ok);
        Button btn_enterpassword_cancel = (Button) view.findViewById(R.id.btn_enterpassword_cancel);
        final ImageView iv_enterpassword_password_see = (ImageView) view.findViewById(R.id.iv_enterpassword_password_see);
        iv_enterpassword_password_see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordSee) {
                    //显示密码
                    edt_enterpassword_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                    iv_enterpassword_password_see.setImageResource(R.drawable.password_see);
                    Editable etext = edt_enterpassword_password.getText();
                    Selection.setSelection(etext, etext.length());
                    isPasswordSee = false;
                } else {
                    //隐藏密码
                    edt_enterpassword_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    iv_enterpassword_password_see.setImageResource(R.drawable.password_see_no);
                    Editable etext = edt_enterpassword_password.getText();
                    Selection.setSelection(etext, etext.length());
                    isPasswordSee = true;
                }
            }
        });
        btn_enterpassword_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = edt_enterpassword_password.getText().toString().trim();
                if (TextUtils.isEmpty(password)) {
                    ToastUtil.show(getApplicationContext(), "请输入密码");
                    return;
                }
                String sp_password = sp.getString("password", "");
                if (MD5Util.passwordMD5(password).equals(sp_password)) {
                    Intent intent = new Intent(HomeActivity.this, LostfindActivity.class);
                    startActivity(intent);
                    dialog.dismiss();
                    ToastUtil.show(getApplicationContext(), "密码正确");
                } else {
                    ToastUtil.show(getApplicationContext(), "密码错误，请重新输入密码");
                    return;
                }
            }
        });
        btn_enterpassword_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog =  builder.create();
        dialog.setView(view, 0, 0, 0, 0);
        dialog.show();
    }

    private void showSetPassWordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        View view = View.inflate(getApplicationContext(), R.layout.dialog_setpassword, null);
        final EditText edt_setpassword_password = (EditText) view.findViewById(R.id.edt_setpassword_password);
        final EditText edt_setpassword_confirm = (EditText) view.findViewById(R.id.edt_setpassword_confirm);
        Button btn_setpassword_ok = (Button) view.findViewById(R.id.btn_setpassword_ok);
        Button btn_setpassword_cancel = (Button) view.findViewById(R.id.btn_setpassword_cancel);
        btn_setpassword_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = edt_setpassword_password.getText().toString().trim();
                if (TextUtils.isEmpty(password)) {
                    ToastUtil.show(getApplicationContext(), "请输入密码");
                    return;
                }
                String confirm_password = edt_setpassword_confirm.getText().toString().trim();
                if (password.equals(confirm_password)) {
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("password", MD5Util.passwordMD5(password));
                    edit.commit();
                    dialog.dismiss();
                    ToastUtil.show(getApplicationContext(), "密码设置成功");
                    Intent intent = new Intent(HomeActivity.this, LostfindActivity.class);
                    startActivity(intent);
                } else {
                    ToastUtil.show(getApplicationContext(), "两次密码不一致，请再次输入密码");
                    return;
                }
            }
        });
        btn_setpassword_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog =  builder.create();
        dialog.setView(view, 0, 0, 0, 0);
        dialog.show();
    }

    private class MyAdapter extends BaseAdapter {
        int[] imageId = { R.drawable.safe, R.drawable.callmsgsafe, R.drawable.app,
                R.drawable.taskmanager, R.drawable.netmanager, R.drawable.trojan,
                R.drawable.sysoptimize, R.drawable.atools, R.drawable.settings };
        String[] names = { "手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计", "手机杀毒", "缓存清理",
                "高级工具", "设置中心" };
        // 设置条目的个数
        @Override
        public int getCount() {
            return 9;
        }

        // 设置条目的样式
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(), R.layout.item_home, null);
            ImageView iv_itemhome_icon = (ImageView)view.findViewById(R.id.iv_itemhome_icon);
            TextView tv_itemhome_text = (TextView) view.findViewById(R.id.tv_itemhome_text);
            //设置控件的值
            iv_itemhome_icon.setImageResource(imageId[position]);//给imageview设置图片,根据条目的位置从图片数组中获取相应的图片
            tv_itemhome_text.setText(names[position]);
            return view;
        }

        // 获取条目对应的数据
        @Override
        public Object getItem(int position) {
            return null;
        }

        // 获取条目的id
        @Override
        public long getItemId(int position) {
            return 0;
        }
    }
}
