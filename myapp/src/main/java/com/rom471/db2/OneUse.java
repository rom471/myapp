package com.rom471.db2;

import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Locale;

/**
 * OneUse表的实体
 */
@Entity
public class OneUse {

    @PrimaryKey(autoGenerate = true)
    private long id;
    //应用名
    private String appName;
    //包名
    private String pkgName;
    @Ignore
    private Drawable icon;
    //使用开始时间
    private long startTimestamp;
    //使用时长
    private long spendTime;
    //开始使用时的电量
    private int battery;
    //结束时的电量
    private int batteryAfter;
    //开始使用时，是否在充电
    private int charging;
    //开始使用时的网络状态
    private int net;
    public String getAppName() {
        return appName;
    }

    public int getBatteryAfter() {
        return batteryAfter;
    }

    public void setBatteryAfter(int batteryAfter) {
        this.batteryAfter = batteryAfter;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public int getCharging() {
        return charging;
    }

    public void setCharging(int charging) {
        this.charging = charging;
    }

    public int getNet() {
        return net;
    }

    public void setNet(int net) {
        this.net = net;
    }



    public OneUse() {
    }

    @Override
    public String toString() {
        return "OneUse{" +
                "appName='" + appName + '\'' +
                ", startTimestamp=" + startTimestamp +
                ", spendTime=" + spendTime +
                '}';
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }
    public long getEndTimeStamp(){
        return startTimestamp+spendTime;
    }
    public long getSpendTime() {
        return spendTime;
    }

    public void setSpendTime(long spendTime) {
        this.spendTime = spendTime;
    }
    /////////
    public String getTimeSpendString() {
        long sec=spendTime/1000;
        if(sec<60)
            return sec+"秒";
        long min=sec/60;
        sec=sec%60;
        return min+"分"+sec+"秒";
    }
    public String getDatetime() {
        SimpleDateFormat sdf=new SimpleDateFormat("MM/dd HH:mm:ss", Locale.getDefault());
        return sdf.format(startTimestamp);
    }
    public String getNetString(){

        if(net==1) return "移动网络";
        else if(net==2) return "wifi";
        else return "无网络";
    }

    public String getChargingString() {

        if(charging==1) return "充电中";
        else if(charging==2) return "USB充电";
        return "未充电";
    }
}
