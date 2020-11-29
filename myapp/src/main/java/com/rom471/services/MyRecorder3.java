package com.rom471.services;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.rom471.db2.App;
import com.rom471.db2.MyDao;
import com.rom471.db2.MyDataBase;
import com.rom471.db2.OneUse;
import com.rom471.utils.DBUtils;

import java.util.ArrayList;
import java.util.List;

public class MyRecorder3 {
    private String lastPkgName = "";
    // RecordDAO dao;
    private MyDao myDao;
    private App app;
    private OneUse oneUse;
    private Context context;
    private PackageManager pm;
    private List<String> exclude_names;
    private List<String> stopApps;
    private boolean filter_exclude = false;
    private boolean filter_skip = false;
    private boolean app_first=false;//当前app是否是第一次插入
    private boolean record_events=false;
    private MyDataBase appDB;

    public MyRecorder3(Context context) {
        this.context=context;
        appDB = MyDataBase.getInstance(context);
        myDao = appDB.getAppDao();
        pm = context.getPackageManager();//初始化包管理器

        stopApps = getSkip_names();
        filter_exclude = true;//过滤开关
        filter_skip = true;//过滤开关
        record_events=true;


    }

    private List<String> getSkip_names() {
        List<String> excludes = new ArrayList<>();
        excludes.add("系统 UI");
        excludes.add("应用预测");
        excludes.add("系统桌面");
        excludes.add("设置");
        excludes.add("权限管理服务");
        excludes.add(("万象息屏"));
        excludes.add("Android 系统");
        return excludes;
    }



    //获得应用名称
    private String getAppLabel(String pkgname) {
        String appLabel = "";
        try {
            appLabel = pm.getPackageInfo(pkgname, PackageManager.GET_ACTIVITIES).applicationInfo.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appLabel;

    }

    //开始记录
    private void record_start(String pkgname) {
        String appname =getAppLabel(pkgname);
        long l = System.currentTimeMillis();
        app_first=false;
        app= myDao.getAppByName(appname);
        if(app==null){
            app=new App();
            app.setFirstRunningTime(l);
            app.setPkgName(pkgname);
            app.setAppName(appname);
            app_first=true;
        }
        oneUse=new OneUse();
        oneUse.setPkgName(pkgname);
        oneUse.setAppName(appname);
        oneUse.setStartTimestamp(l);
        DBUtils.storeBatteryInfo(context,oneUse,true);

    }

    //结束记录
    private void record_finish(String lastPkgName) {

        if(oneUse==null)
            return;
        if( !oneUse.getPkgName().equals(lastPkgName)){
            return;
        }
        long l = System.currentTimeMillis();
        long spend = l - oneUse.getStartTimestamp();
        //不记录0.3秒内的
        if (spend < 300) return;
        oneUse.setSpendTime(spend);
        app.setLastRuningTime(l);
        app.addTotalRuningTime(spend);//增加统计表的时间
        app.addUseCount();//增加使用次数
        DBUtils.storeBatteryInfo(context, oneUse, false);
        DBUtils.storeNetworkInfo(context, oneUse);
        if (app_first)
            myDao.insert(app);
        else
            myDao.updateApp(app);

        myDao.insert(oneUse);

        oneUse=null;

    }

    public void record(AccessibilityEvent event) {
        int type=event.getEventType();
        if(type!=32)return;
        String pkgname = event.getPackageName() == null ? "" : event.getPackageName().toString();
        String appName = getAppLabel(pkgname);
        //获取类名
        String name= event.getClassName()==null ?"":event.getClassName().toString();
        if(!name.startsWith("android.")) {
            //如果回到桌面就停止记录
            if(stopApps.contains(appName)){
                record_finish(lastPkgName);

            }
            //不是桌面程序就开始记录
            else {
                record_start(pkgname);
                lastPkgName=pkgname;
            }

        }
    }
    public void close(){
        appDB.close();
    }
}
