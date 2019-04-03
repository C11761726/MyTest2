package com.mytest.utils;

import android.os.Environment;

import com.google.gson.Gson;
import com.mytest.ServerInfo;
import com.mytest.SettingDataStruct;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class JsonFileWriteAndRead {
    public static void saveSettingFile(String fileName, String content) {
        FileOutputStream fos = null;
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.getExternalStorageDirectory()
                    + "/" + fileName + ".json");
            try {
                fos = new FileOutputStream(file);
                byte[] buffer = content.getBytes();
                fos.write(buffer);
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static SettingDataStruct getJsonString(String fileName)
            throws IOException {
        SettingDataStruct settingDataStruct = new SettingDataStruct();
        if ((fileName == null) || fileName.isEmpty()) {
            settingDataStruct = null;
            return settingDataStruct;
        }
        FileInputStream fis = null;
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.getExternalStorageDirectory()
                    + "/" + fileName + ".json");
            if (file.exists()) {
                fis = new FileInputStream(file);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();

                Gson gson = new Gson();
                settingDataStruct = gson.fromJson(new String(buffer),
                        SettingDataStruct.class);
            } else {
                settingDataStruct = null;
            }
        }
        return settingDataStruct;
    }

    public static void saveServerInfo(String fileName, String content) {
        FileOutputStream fos = null;
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.getExternalStorageDirectory()
                    + "/" + fileName + ".json");
            try {
                fos = new FileOutputStream(file);
                byte[] buffer = content.getBytes();
                fos.write(buffer);
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static ServerInfo getServerInfo(String fileName)
            throws IOException {
        ServerInfo serverInfo = new ServerInfo();
        if ((fileName == null) || fileName.isEmpty()) {
            serverInfo = null;
            return serverInfo;
        }
        FileInputStream fis = null;
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.getExternalStorageDirectory()
                    + "/" + fileName + ".json");
            if (file.exists()) {
                fis = new FileInputStream(file);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();

                Gson gson = new Gson();
                serverInfo = gson.fromJson(new String(buffer),
                        ServerInfo.class);
            } else {
                serverInfo = null;
            }
        }
        return serverInfo;
    }

    public static void saveIPandPort(String content) {
        FileOutputStream fos = null;
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.getExternalStorageDirectory() + "/videochat.txt");
            try {
                fos = new FileOutputStream(file);
                byte[] buffer = content.getBytes();
                fos.write(buffer);
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
