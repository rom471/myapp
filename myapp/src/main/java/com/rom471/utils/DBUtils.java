package com.rom471.utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import com.rom471.db2.App;
import com.rom471.db2.OneUse;
import com.rom471.db2.SimpleApp;
import java.util.List;
import java.util.Locale;
import static android.content.Context.BATTERY_SERVICE;
public class DBUtils {
    //给OneUse附加电池信息
    public static void storeBatteryInfo(Context context, OneUse oneUse,boolean start){
        BatteryManager manager = (BatteryManager)context.getSystemService(BATTERY_SERVICE);
        if(start)
            oneUse.setBattery(manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY));
        else
            oneUse.setBatteryAfter(manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY));
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);


        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
        int charging=0;
        if(acCharge)charging=1;
        if(usbCharge) charging=2;
        oneUse.setCharging(charging);

    }
    //给OneUse附加网络信息
    public static void storeNetworkInfo(Context context, OneUse record){
        int net_state=0;
        ConnectivityManager manager= (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        //判断NetworkInfo对象是否为空
        if (networkInfo != null) {

            if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){//使用数据
                net_state=1;
            }
            else if(networkInfo.getType()==ConnectivityManager.TYPE_WIFI){//使用wifi
                net_state=2;
            }
        }
        record.setNet(net_state);

    }
    public static void setSimpleAppsIcon(Context context,List<SimpleApp> apps){
        if (apps==null)
            return;
        PackageManager pm =context.getPackageManager();
        ApplicationInfo appInfo;
        Drawable appIcon;
        for (SimpleApp app:apps
        ) {
            if(app==null)
                continue;
            try {
                appInfo = pm.getApplicationInfo(app.getPkgName(), PackageManager.GET_META_DATA);
                appIcon = pm.getApplicationIcon(appInfo);
                app.setIcon(appIcon);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    public static void setAppsIcon(Context context,List<App> apps){
        if (apps==null)
            return;
        PackageManager pm =context.getPackageManager();
        ApplicationInfo appInfo;
        Drawable appIcon;
        for (App app:apps
        ) {
            try {
                appInfo = pm.getApplicationInfo(app.getPkgName(), PackageManager.GET_META_DATA);
                appIcon = pm.getApplicationIcon(appInfo);
                app.setIcon(appIcon);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    public static void setOneUseIcon(Context context,List<OneUse> apps){
        PackageManager pm =context.getPackageManager();
        ApplicationInfo appInfo;
        Drawable appIcon;
        for (OneUse app:apps
        ) {
            try {
                appInfo = pm.getApplicationInfo(app.getPkgName(), PackageManager.GET_META_DATA);
                appIcon = pm.getApplicationIcon(appInfo);
                app.setIcon(appIcon);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    //用当前时间保存数据库
    public static String getCurrentDBString(){
        return getDatatime(System.currentTimeMillis(),"yy-MM-dd_HH");
    }
    public static  String getDatatimeLong(long timeStamp) {
        return getDatatime(timeStamp,"MM/dd HH:mm:ss");
    }
    public static long since(long start){
        return System.currentTimeMillis()-start;

    }
    public static String getSinceTimeString(long start){
        return getTimeSpendString(since(start));
    }
    public static  String getDatatime(long timeStamp,String pattern) {
        SimpleDateFormat sdf=new SimpleDateFormat(pattern, Locale.getDefault());
        return sdf.format(timeStamp);

    }
    public static String getTimeSpendString(long timeStamp) {
        long sec=timeStamp/1000;
        if(sec<60)
            return sec+"秒";
        long min=sec/60;
        sec=sec%60;
        if(min<60)
        return min+"分"+sec+"秒";
        long hour=min/60;
        min=min%60;
        if(hour<24)
            return hour+"时"+min+"分";
        long day=hour/24;
        hour=hour%24;
        return day+"天"+hour+"时";


    }
}
