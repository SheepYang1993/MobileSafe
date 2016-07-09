package com.sheepyang.mobilesafe.ui.SetUp;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.sheepyang.mobilesafe.R;
import com.sheepyang.mobilesafe.ui.ContactActivity;
import com.sheepyang.mobilesafe.utils.ToastUtil;

/**
 * Created by SheepYang on 2016/6/9 22:41.
 */
public class SetUp3Activity extends SetUpBaseActivity {
    private EditText edt_setup3_safenum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        initView();
    }

    private void initView() {
        edt_setup3_safenum = (EditText) findViewById(R.id.edt_setup3_safenum);
        edt_setup3_safenum.setText(sp.getString("safenum", ""));
    }

    private void saveSafenum(String safenum) {
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("safenum", safenum);
        edit.commit();
    }

    /**
     * 选择联系人
     * @param v
     */
    public void selectContacts(View v) {
        Intent intent = new Intent(SetUp3Activity.this, ContactActivity.class);
        startActivityForResult(intent, 0);
    }

    /**
     * 通过系统自带通讯录选择联系人
     * @param v
     */
    public void selectContactsBySystem(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.ContactsContract.Contacts.CONTENT_URI);
        intent.setAction("android.intent.action.PICK");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("vnd.android.cursor.dir/phone_v2");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 0:
                if (data != null) {
                    String safenum = data.getStringExtra("safenum");
                    edt_setup3_safenum.setText(safenum);
                }
                break;
            case 1:
                if(data !=null){
                    Uri uri = data.getData();
                    String num = null;
                    // 创建内容解析者
                    ContentResolver contentResolver = getContentResolver();
                    Cursor cursor = contentResolver.query(uri, null, null, null, null);
                    while(cursor.moveToNext()){
                        num = cursor.getString(cursor.getColumnIndex("data1"));
                    }
                    cursor.close();
                    if (!TextUtils.isEmpty(num)) {
                        num = num.replaceAll("-", "");//替换的操作,555-6 -> 5556
                        edt_setup3_safenum.setText(num);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void nextActivity() {
        String safenum = edt_setup3_safenum.getText().toString().trim();
        if (TextUtils.isEmpty(safenum)) {
            ToastUtil.show(getApplicationContext(), "请输入安全号码");
            return;
        }
        saveSafenum(safenum);
        Intent intent = new Intent(SetUp3Activity.this, SetUp4Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.setup_enter_next, R.anim.setup_exit_next);
    }

    @Override
    public void preActivity() {
        String safenum = edt_setup3_safenum.getText().toString().trim();
        saveSafenum(safenum);
        Intent intent = new Intent(SetUp3Activity.this, SetUp2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.setup_enter_pre, R.anim.setup_exit_pre);
    }
}
