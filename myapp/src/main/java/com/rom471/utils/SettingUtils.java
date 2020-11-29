package com.rom471.utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.rom471.db2.MyDao;
import com.rom471.recorder.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 *与设置有关的工具类
 *
 */
public class SettingUtils {
    /**
     * 申请存储权限
     */

    public static boolean verifyStoragePermissions(Activity activity) {
        final int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE" };
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查看辅助功能服务是否打开
     * @param context
     * @return
     */
    public static boolean isAccessibilitySettingsOn(Context context) {
        String service="com.rom471.myapp/com.rom471.services.AccessibilityMonitorService";
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');
        String settingValue = Settings.Secure.getString(
                context.getApplicationContext().getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
//        Log.d("cedar", "isAccessibilitySettingsOn: "+settingValue);
        if (settingValue != null) {
            mStringColonSplitter.setString(settingValue); //各个服务由分号分割
            while (mStringColonSplitter.hasNext()) {
                String accessibilityService = mStringColonSplitter.next();
                if (accessibilityService.equalsIgnoreCase(service)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 导出数据库文件
     * @param context
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void export_db(Context context){
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source;
        FileChannel destination;
        String currentDBPath = "/data/"+ "com.rom471.myapp" +"/databases/"+"apps.db";
        String backupPath=sd+"/rom471/";
        File backupPathFile = new File(backupPath);
        if (!backupPathFile.exists()) { //检查目录是否存在
            try {
                //按照指定的路径创建文件夹
                backupPathFile.mkdirs();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        String dbname= DBUtils.getCurrentDBString();
        String backupDBName=dbname+".db";
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(backupPath, backupDBName);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(context, "文件保存在:"+backupDB.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    //弹出编辑服务器地址的对话框
    public static void alert_host_edit(Context context){
        final EditText et = new EditText(context);
        String host =(String) MyProperties.get(context, "host", "");
        et.setText(host);
        new AlertDialog.Builder(context).setTitle("请输入服务器地址")
                .setIcon(R.drawable.set_hostip)
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MyProperties.set(context,"host",et.getText().toString());
                        //按下确定键后的事件
                        Toast.makeText(context, "修改完成",Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton("取消",null).show();
    }
    //弹出确认清除数据的弹出对话框
    public static void confirmClearRecordsDialog(Context context, MyDao myDao){
        final boolean[] ret = {false};
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(context);
        normalDialog.setIcon(R.drawable.delete);
        normalDialog.setTitle("警告！");
        normalDialog.setMessage("确认要删除已有的数据吗？");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDao.deleteApps();
                        myDao.deleteOneUses();


                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        normalDialog.show();

    }

    //跳转到打开辅助功能界面
    public static void getAccessibilityPermission(Context context){
        final String mAction= Settings.ACTION_ACCESSIBILITY_SETTINGS;//辅助功能
        Intent intent=new Intent(mAction);
        context.startActivity(intent);
    }

    //弹出重新开始预测的对话框
    public static void aboutDialog(Context context){
        final AlertDialog.Builder aboutDialog =
                new AlertDialog.Builder(context);
        LayoutInflater inflater=LayoutInflater.from(context);
        View  aboutView=inflater.inflate(R.layout.aboutlayout,null);
        aboutDialog.setIcon(R.drawable.about);
        aboutDialog.setTitle("关于本应用");
        aboutDialog.setView(aboutView);
        aboutDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        aboutDialog.show();

    }
}
