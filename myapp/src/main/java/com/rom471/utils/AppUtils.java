 package com.rom471.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.rom471.db2.SimpleApp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AppUtils {

    private static Context context;
    private static HashMap<String,String>APP_PKG=new HashMap<>();
    public static Context getContext(){
        return context;
    }
    public static void init(Context context){
        AppUtils.context=context;
        PackageManager pm=context.getPackageManager();
        List<PackageInfo> packages=pm.getInstalledPackages(0);

        for(PackageInfo packageInfo:packages){
            if((packageInfo.applicationInfo.flags& ApplicationInfo.FLAG_SYSTEM)==0) {//非系统应用
                String pkg=packageInfo.packageName;
                String appname=packageInfo.applicationInfo.loadLabel(pm).toString();
                APP_PKG.put(appname,pkg);
            }
            else{
                String pkg=packageInfo.packageName;
                String appname=packageInfo.applicationInfo.loadLabel(pm).toString();
                APP_PKG.put(appname,pkg);
            }
        }
    }
    public static String getPkgName(String appName){
        return APP_PKG.get(appName);
    }
    public static List<SimpleApp> getSimpleAppsFromNames(List<String> names){
        List<SimpleApp> apps=new ArrayList<>();
        for (String name:names){
            SimpleApp app=new SimpleApp();
            app.setAppName(name);
            app.setPkgName(getPkgName(name));
            apps.add(app);
        }
        setSimpleAppsIcon(apps);
        return apps;
    }
    public static void setSimpleAppsIcon(List<SimpleApp> apps){
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
    public static void setSimpleAppsIcon(SimpleApp... apps){
        setSimpleAppsIcon(Arrays.asList(apps));
    }
}
