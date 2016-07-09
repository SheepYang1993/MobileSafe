package com.sheepyang.mobilesafe.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sheepyang.mobilesafe.R;
import com.sheepyang.mobilesafe.engine.ContactEngine;
import com.sheepyang.mobilesafe.entity.Contact;
import com.sheepyang.mobilesafe.utils.MyAsycnTask;

import java.util.List;

/**
 * Created by SheepYang on 2016/6/10 14:34.
 */
public class ContactActivity extends BaseActivity {
    @ViewInject(R.id.lv_contact_contacts)
    private ListView lv_contact_contacts;
    private List<Contact> list;
    @ViewInject(R.id.pb_contact_loading)
    private ProgressBar pb_contact_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        initView();
    }

    private void initView() {
        ViewUtils.inject(this);

        //自定义的异步加载
        new MyAsycnTask(){
            @Override
            public void preTask() {
                pb_contact_loading.setVisibility(View.VISIBLE);
            }

            @Override
            public void doInBack() {
                list = ContactEngine.getAllContactInfo(getApplicationContext());
            }

            @Override
            public void postTask() {
                lv_contact_contacts.setAdapter(new MyAdapter());
                pb_contact_loading.setVisibility(View.GONE);
            }
        }.execute();

//        new AsyncTask<Void, Integer, List<Contact>>() {
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                pb_contact_loading.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            protected List<Contact> doInBackground(Void... params) {
//                list = ContactEngine.getAllContactInfo(getApplicationContext());
//                return list;
//            }
//
//            @Override
//            protected void onPostExecute(List<Contact> aVoid) {
//                super.onPostExecute(aVoid);
//                lv_contact_contacts.setAdapter(new MyAdapter());
//                pb_contact_loading.setVisibility(View.GONE);
//            }
//        }.execute();

        lv_contact_contacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent data = new Intent();
                data.putExtra("safenum", list.get(position).getPhone());
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(), R.layout.item_contact, null);
            //初始化控件
            //view.findViewById 是从item_contact找控件,findViewById是从activity_contacts找控件
            TextView tv_itemcontact_name = (TextView) view.findViewById(R.id.tv_itemcontact_name);
            TextView tv_itemcontact_phone = (TextView) view.findViewById(R.id.tv_itemcontact_phone);
            //设置控件的值
            tv_itemcontact_name.setText(list.get(position).getName());//根据条目的位置从list集合中获取相应的数据
            tv_itemcontact_phone.setText(list.get(position).getPhone());
            return view;
        }
    }
}
