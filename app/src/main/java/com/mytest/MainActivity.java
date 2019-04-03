package com.mytest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mytest.utils.JsonFileWriteAndRead;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity implements ServiceConnection {
    private static final String TAG = "MainActivity";

    private static final String pkgName = "com.eightmile.videotrack";
    private static final String clsName = "com.eightmile.videotrack.MainService";

    private static final int IMAGE_REQUEST_CODE = 0x01;
    private static final int GETIMAGE_RESULT_CODE = 0x02;
    private static final int SERVICE_CONNECTED = 0x03;
    private EditText widthSet;
    private EditText heightSet;
    private EditText leftSet;
    private EditText topSet;
    private EditText ipSet;
    private EditText portSet;
    private TextView imagePath;
    private EditText android_name;
    private final MyHandler mHandler = new MyHandler(this);
    ServiceControl serviceControl;

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        serviceControl = ServiceControl.Stub.asInterface(service);
        mHandler.sendEmptyMessage(SERVICE_CONNECTED);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    class MyHandler extends Handler {
        private WeakReference<MainActivity> mActivityRef;

        public MyHandler(MainActivity activity) {
            mActivityRef = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mActivityRef.get();
            switch (msg.what) {
                case GETIMAGE_RESULT_CODE:
                    imagePath.setText((String) msg.obj);
                    break;
                case SERVICE_CONNECTED:
                    Toast.makeText(activity, "服务已绑定成功", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;

            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        widthSet = findViewById(R.id.widthSet);
        heightSet = findViewById(R.id.heightSet);
        leftSet = findViewById(R.id.leftSet);
        topSet = findViewById(R.id.topSet);
        imagePath = findViewById(R.id.picpath);
        ipSet = findViewById(R.id.ipSet);
        portSet = findViewById(R.id.portSet);
        android_name = findViewById(R.id.android_name);
    }

    /**
     * 检查包是否存在
     *
     * @param packname
     * @return
     */
    private boolean checkPackInfo(String packname) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(packname, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo != null;
    }

    public void onBinderService(View view) {
//        PackageManager packageManager = getPackageManager();
//
//        if (checkPackInfo("com.eightmile.videotrack")) {
//            Toast.makeText(this, "检查到app存在", Toast.LENGTH_LONG).show();
//            Intent intent = packageManager.getLaunchIntentForPackage("com.eightmile.videotrack");
//            startActivity(intent);
//        } else {
//            Toast.makeText(this, "没检查到app存在", Toast.LENGTH_LONG).show();
//        }
        startMainService();

    }

    //ComponentName(param1,param2)
    //param1:Activity、Service所在应用的包名
    //param2:Activity、Service的包名+类名
    private void startMainService() {
        Intent intent = new Intent();
        intent = intent.setComponent(new ComponentName(pkgName, clsName));
        startService(intent);
        //bindService(intent, this, BIND_AUTO_CREATE);
    }

    public void onUnBinderService(View view) {
        stopMainService();
    }

    public void onShowView(View view) {
        try {
            serviceControl.showView();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void onDismissView(View view) {
        try {
            serviceControl.dismissView();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void stopMainService() {
        Log.d(TAG, "into stopMainService");
        Intent intent = new Intent();
        intent = intent.setComponent(new ComponentName(pkgName, clsName));
        stopService(intent);
        //unbindService(this);
    }

    public void onSetting(View view) {
        String fileName = "layoutSetting";
        SettingDataStruct settingDataStruct = new SettingDataStruct();
        String width = widthSet.getText().toString();
        settingDataStruct.setWidth(width);
        settingDataStruct.setHeight(heightSet.getText().toString());
        settingDataStruct.setLeft(leftSet.getText().toString());
        settingDataStruct.setTop(topSet.getText().toString());
        settingDataStruct.setImagePath(imagePath.getText().toString());
        Gson gson = new Gson();
        String jsonString = gson.toJson(settingDataStruct);
        JsonFileWriteAndRead.saveSettingFile(fileName, jsonString);
        Toast.makeText(this, "参数设置完毕", Toast.LENGTH_SHORT).show();
    }

    public void onReading(View view) {
        String fileName = "layoutSetting";
        SettingDataStruct settingDataStruct = null;
        try {
            settingDataStruct = JsonFileWriteAndRead.getJsonString(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "读取完毕", Toast.LENGTH_SHORT).show();
        widthSet.setText(settingDataStruct.getWidth());
        heightSet.setText(settingDataStruct.getHeight());
        leftSet.setText(settingDataStruct.getLeft());
        topSet.setText(settingDataStruct.getTop());
        imagePath.setText(settingDataStruct.getImagePath());
    }

    public void onResetting(View view) {
        widthSet.setText("");
        heightSet.setText("");
        leftSet.setText("");
        topSet.setText("");
        imagePath.setText("");
        ipSet.setText("");
        portSet.setText("");
        android_name.setText("");
    }

    public void onSelect(View view) {
        //在这里跳转到手机系统相册里面
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }

    public void stopPusher(View view) {
        Intent serviceIntent = serviceIntent = new Intent();
        serviceIntent.setComponent(new ComponentName("com.mysuspensionwindow", "com.mysuspensionwindow.MainService"));
        stopService(serviceIntent);
    }

    public void restartPusher(View view) {
        Intent serviceIntent = serviceIntent = new Intent();
        serviceIntent.setComponent(new ComponentName("com.mysuspensionwindow", "com.mysuspensionwindow.MainService"));
        startService(serviceIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        //在相册里面选择好相片之后调回到现在的这个activity中
        switch (requestCode) {
            case IMAGE_REQUEST_CODE://这里的requestCode是我自己设置的，就是确定返回到那个Activity的标志
                if (resultCode == RESULT_OK) {//resultcode是setResult里面设置的code值
                    try {
                        Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String path = cursor.getString(columnIndex);  //获取照片路径
                        Message msg = Message.obtain();
                        msg.what = GETIMAGE_RESULT_CODE;
                        msg.obj = path;
                        mHandler.sendMessage(msg);
//                        cursor.close();
//                        Bitmap bitmap = BitmapFactory.decodeFile(path);
//                        iv_photo.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        // TODO Auto-generatedcatch block
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public void onSetIPAndPort(View view) {
        ServerInfo serverInfo = new ServerInfo();
        try {
            serverInfo = JsonFileWriteAndRead.getServerInfo("videochat");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //写入ip和端口
        String ip = ipSet.getText().toString();
        String port = portSet.getText().toString();
        serverInfo.setIpString(ip);
        serverInfo.setPortString(port);
        Gson gson = new Gson();
        if (ip.isEmpty() || port.isEmpty()) {
            Toast.makeText(this, "地址或端口为空", Toast.LENGTH_SHORT).show();
        } else {
            JsonFileWriteAndRead.saveServerInfo("videochat", gson.toJson(serverInfo));
            Toast.makeText(this, "地址和端口已经写入文件", Toast.LENGTH_SHORT).show();
        }
    }

    public void onReadIPAndPort(View view) {
        ServerInfo serverInfo = new ServerInfo();
        try {
            serverInfo = JsonFileWriteAndRead.getServerInfo("videochat");
            ipSet.setText(serverInfo.getIpString());
            portSet.setText(serverInfo.getPortString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onSetAndroidName(View view) {
        ServerInfo serverInfo = new ServerInfo();
        try {
            serverInfo = JsonFileWriteAndRead.getServerInfo("videochat");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String android_name_str = android_name.getText().toString();
        serverInfo.setAndroid_name(android_name_str);
        Gson gson = new Gson();
        if (android_name_str.isEmpty()) {
            Toast.makeText(this, "设备名为空", Toast.LENGTH_SHORT).show();
        } else {
            JsonFileWriteAndRead.saveServerInfo("videochat", gson.toJson(serverInfo));
            Toast.makeText(this, "设备名已经写入文件", Toast.LENGTH_SHORT).show();
        }
    }

    public void onReadAndroidName(View view) {
        ServerInfo serverInfo = new ServerInfo();
        try {
            serverInfo = JsonFileWriteAndRead.getServerInfo("videochat");
            android_name.setText(serverInfo.getAndroid_name());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
