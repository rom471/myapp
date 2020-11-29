package com.rom471.db2;

import android.graphics.drawable.Drawable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;


/**
 * App表的实体
 */
@Entity(indices = {@Index(value = {"pkgName"},
        unique = true)})
public class App {
    @PrimaryKey(autoGenerate = true)
    private long appId;
    //应用名
    private String appName;
    //包名
    private String pkgName;
    //应用图标
    @Ignore
    private Drawable icon;
    //应用第一次运行时间
    private long firstRunningTime;
    //应用最后一次运行时间
    private long lastRuningTime;
    //应用总使用时间
    private long totalRuningTime;
    //使用次数
    private long useCount;

    public App() {
    }

    public long getUseCount() {
        return useCount;
    }

    public void setUseCount(long useCount) {
        this.useCount = useCount;
    }

    public void addUseCount() {
        this.useCount++;
    }

    public long getFirstRunningTime() {
        return firstRunningTime;
    }

    public void setFirstRunningTime(long firstRunningTime) {
        this.firstRunningTime = firstRunningTime;
    }

    public long getLastRuningTime() {
        return lastRuningTime;
    }

    public void setLastRuningTime(long lastRuningTime) {
        this.lastRuningTime = lastRuningTime;
    }

    public long getTotalRuningTime() {
        return totalRuningTime;
    }

    public void setTotalRuningTime(long totalRuningTime) {
        this.totalRuningTime = totalRuningTime;
    }

    public void addTotalRuningTime(long timespend) {
        this.totalRuningTime += timespend;
    }

    public long getAppId() {
        return appId;
    }

    public void setAppId(long appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
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

    @Override
    public String toString() {
        return "App{" +
                "appName='" + appName + '\'' +
                ", lastRuningTime=" + lastRuningTime +
                ", totalRuningTime=" + totalRuningTime +
                '}';
    }


}
