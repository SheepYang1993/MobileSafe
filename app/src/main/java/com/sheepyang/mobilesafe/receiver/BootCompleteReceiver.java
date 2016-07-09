package com.sheepyang.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.sheepyang.mobilesafe.utils.Constants;

/**
 * 监听手机重启
 * Created by SheepYang on 2016/6/10 10:38.
 */
public class BootCompleteReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        if (sp.getBoolean("protected", false)) {
            //开启了防盗保护
            String sp_sim = sp.getString("sim", "");
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String sim = tm.getSimSerialNumber();
            if (TextUtils.isEmpty(sp_sim) && TextUtils.isEmpty(sim)) {
                if (!sp_sim.equals(sim)) {
                    SmsManager smsManager = SmsManager.getDefault();
                    String safenum = sp.getString("safenum", "");
                    smsManager.sendTextMessage(safenum, null, "手机卫士发现您的SIM卡改变了，已发送报警短信!", null, null);
                }
            }
        } else {
            //关闭了防盗保护
        }
    }
}
