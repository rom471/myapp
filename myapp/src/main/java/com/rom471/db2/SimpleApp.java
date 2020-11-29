package com.rom471.db2;

import android.graphics.drawable.Drawable;

import androidx.room.Ignore;

import java.util.Comparator;
import java.util.Objects;

public class SimpleApp implements Comparable<SimpleApp>{
    private String appName;
    private String pkgName;
    private long startTimestamp;
    @Ignore
    private Drawable icon;
    @Ignore
    private int weight;
    public long getStartTimestamp() {
        return startTimestamp;
    }
    public void setStartTimestamp(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }
    public int getWeight() {
        return weight;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }
    public void addWeight(int weight) {
        this.weight +=weight;
    }
    public Drawable getIcon() {
        return icon;
    }
    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
    public SimpleApp() {
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
    @Override
    public int compareTo(SimpleApp o) {

        return this.getWeight()-o.getWeight();
    }
    @Ignore
    public static Comparator<SimpleApp> weightDescComparator=new Comparator<SimpleApp>() {
        @Override
        public int compare(SimpleApp o1, SimpleApp o2) {
            return o2.weight-o1.weight;
        }
    };
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleApp app = (SimpleApp) o;
        if(Objects.equals(appName, app.appName)){
            return true;
        }
        return false;
    }
    @Override
    public int hashCode() {
        return Objects.hash(appName);
    }
}
