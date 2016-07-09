package com.sheepyang.mobilesafe.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.IOUtils;
import com.sheepyang.mobilesafe.R;
import com.sheepyang.mobilesafe.utils.Constants;
import com.sheepyang.mobilesafe.utils.StreamUtil;
import com.sheepyang.mobilesafe.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

public class SplashActivity extends BaseActivity {
    private static final int REQUEST_CODE_ENTER_HOME = 0;
    private static final int MSG_UPDATE_DIALOG = 1;
    private static final int MSG_ENTER_HOME = 2;
    private static final int MSG_SERVER_ERROR = 3;
    private static final int MSG_URL_ERROR = 4;
    private static final int MSG_IO_ERROR = 5;
    private static final int MSG_JSON_ERROR = 6;
    private static final int MSG_TIMEOUT_ERROR = 7;
    private SharedPreferences sp;
    private TextView tv_splash_versionname;
    private TextView tv_splash_plan;
    private String code;
    private String apkurl;
    private String savePath;
    private String des;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case MSG_UPDATE_DIALOG:
                    //弹出更新对话框
                    showUpdateDialog();
                    break;
                case MSG_ENTER_HOME:
                    enterHome();
                    break;
                case MSG_SERVER_ERROR:
                    ToastUtil.show(getApplicationContext(), "服务器异常！");
                    enterHome();
                    break;
                case MSG_URL_ERROR:
                    ToastUtil.show(getApplicationContext(), "错误编码：" + MSG_URL_ERROR);
                    enterHome();
                    break;
                case MSG_TIMEOUT_ERROR:
                    ToastUtil.show(getApplicationContext(), "服务器连接超时！");
                    enterHome();
                    break;
                case MSG_IO_ERROR:
                    ToastUtil.show(getApplicationContext(), "网络连接异常，请检查网络！");
                    enterHome();
                    break;
                case MSG_JSON_ERROR:
                    ToastUtil.show(getApplicationContext(), "错误编码：" + MSG_JSON_ERROR);
                    enterHome();
                    break;
                default:
                    break;
            }
        }
    };

    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("发现新版本：" + code);
        builder.setMessage(des);
        builder.setPositiveButton("升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //下载最新版本
                download();
            }
        });
        builder.setNegativeButton("忽略", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                enterHome();
            }
        });
        builder.create().show();
    }

    /**
     * 下载最新版本
     */
    private void download() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            HttpUtils httpUtils = new HttpUtils();
            savePath =  Environment.getExternalStorageDirectory().getPath()+"/mobilesafe/手机卫士" + code + ".apk";
            httpUtils.download(apkurl, savePath, new RequestCallBack<File>() {
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    installAPK();
                }

                @Override
                public void onFailure(HttpException e, String s) {

                }

                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    tv_splash_plan.setVisibility(View.VISIBLE);
                    tv_splash_plan.setText("下载进度：" + current + "/" + total);
                }
            });
        } else {
            //SD卡没有挂在
            ToastUtil.show(getApplicationContext(), "没有找到SD卡");
        }
    }

    /**
     * 安装APK
     */
    private void installAPK() {
        /**
         *  <intent-filter>
         <action android:name="android.intent.action.VIEW" />
         <category android:name="android.intent.category.DEFAULT" />
         <data android:scheme="content" /> //content : 从内容提供者中获取数据  content://
         <data android:scheme="file" /> // file : 从文件中获取数据
         <data android:mimeType="application/vnd.android.package-archive" />
         </intent-filter>
         */
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        //单独设置会相互覆盖
		/*intent.setType("application/vnd.android.package-archive");
		intent.setData(Uri.fromFile(new File(savePath)));*/
        intent.setDataAndType(Uri.fromFile(new File(savePath)), "application/vnd.android.package-archive");
        //在当前activity退出的时候,会调用之前的activity的onActivityResult方法
        //requestCode : 请求码,用来标示是从哪个activity跳转过来
        //ABC  a -> c    b-> c  ,c区分intent是从哪个activity传递过来的,这时候就要用到请求码
        startActivityForResult(intent, REQUEST_CODE_ENTER_HOME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        enterHome();
    }

    private void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        sp = getSharedPreferences("config", MODE_PRIVATE);
        if (sp.getBoolean("update", true)) {
            update();
        } else {
            new Thread(){
                @Override
                public void run() {
                    SystemClock.sleep(2000);
                    enterHome();
                }
            }.start();
        }
        copyDB();
        shortcut();
    }

    /**
     * 创建快捷方式
     */
    private void shortcut() {
        if (sp.getBoolean("firstshortcut", true)) {
            // 给桌面发送一个广播
            Intent intent = new Intent(
                    "com.android.launcher.action.INSTALL_SHORTCUT");
            // 设置属性
            // 设置快捷方式名称
            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "手机卫士75");
            // 设置快捷方式的图标
            Bitmap value = BitmapFactory.decodeResource(getResources(),
                    R.drawable.ic_launcher);
            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, value);
            // 设置快捷方式执行的操作
            Intent intent2 = new Intent();
            intent2.setAction("com.sheepyang.home");
            intent2.addCategory("android.intent.category.DEFAULT");
            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent2);
            sendBroadcast(intent);

            //保存已经创建快捷方式的状态
            SharedPreferences.Editor edit = sp.edit();
            edit.putBoolean("firstshortcut", false);
            edit.commit();
        }
    }

    /**
     * 拷贝数据库
     */
    private void copyDB() {
        File file = new File(getFilesDir(), "address.db");
        if (!file.exists()) {
            AssetManager am = getAssets();
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                is =  am.open("address.db");
                fos = new FileOutputStream(file);
                byte[] b = new byte[1024];
                int len = -1;
                while ((len = is.read(b)) != -1) {
                    fos.write(b, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(is);
                IOUtils.closeQuietly(fos);
            }
        }
    }

    private void initView() {
        tv_splash_versionname = (TextView) findViewById(R.id.tv_splash_versionname);
        tv_splash_versionname.setText("版本号：" + getVersionName());
        tv_splash_plan = (TextView) findViewById(R.id.tv_splash_plan);
    }

    /**
     * 更新版本
     */
    private void update() {
        new Thread(){

            private long startTime;

            @Override
            public void run() {
                Message msg = Message.obtain();
                startTime =  System.currentTimeMillis();
                try {
                    URL url = new URL("http://192.168.0.121:8080/updateinfo.html");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    conn.setRequestMethod("GET");
                    int responseCode = conn.getResponseCode();
                    if (responseCode == 200) {
                        Log.i(Constants.TAG, "responseCode:" + responseCode + ", 连接服务器成功!");
                        InputStream is = conn.getInputStream();
                        String json = StreamUtil.parserStreamUtil(is);
                        JSONObject jo = new JSONObject(json);
                        code =  jo.getString("code");
                        apkurl =  jo.getString("apkurl");
                        des =  jo.getString("des");
                        Log.i(Constants.TAG, "code:" + code + ",\napkurl:" + apkurl + ",\ndes:" + des);
                        if (code.equals(getVersionName())) {
                            //没有最新版本
                            Log.i(Constants.TAG, "没有最新版本");
                            msg.what = MSG_ENTER_HOME;
                        } else {
                            //发现最新版本
                            Log.i(Constants.TAG, "发现最新版本!");
                            msg.what = MSG_UPDATE_DIALOG;
                        }
                    } else {
                        Log.i(Constants.TAG, "responseCode:" + responseCode + ", 连接服务器失败!");
                        msg.what = MSG_SERVER_ERROR;
                    }
                } catch (SocketTimeoutException e) {
                    msg.what = MSG_TIMEOUT_ERROR;
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    msg.what = MSG_URL_ERROR;
                    e.printStackTrace();
                } catch (IOException e) {
                    msg.what = MSG_IO_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    msg.what = MSG_JSON_ERROR;
                    e.printStackTrace();
                } finally {
                    long endTime =  System.currentTimeMillis();
                    long dTime = startTime - endTime;
                    if (dTime < 2000) {
                        SystemClock.sleep(2000 - dTime);
                    }
                    mHandler.sendMessage(msg);
                }
            }
        }.start();
    }

    private String getVersionName() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            String versionName = packageInfo.versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "未知";
    }
}
