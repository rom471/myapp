package com.rom471.db2;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public abstract class MyDao {

//App表操作
    @Insert
    public abstract void insert(App app);

    @Query("select * from App order by lastRuningTime desc  limit :limit")
    public abstract LiveData<List<App>>  getLastUsedApp(int limit);

    @Query("select * from App order by totalRuningTime desc  limit :limit")
    public abstract LiveData<List<App>>  getMostUsedApps(int limit);

    @Query("select * from App order by useCount desc  limit :limit")
    public abstract LiveData<List<App>>  getMostCountsApps(int limit);

    @Update
    public abstract void updateApp(App app);

    @Query("select * from App where appName=:appname limit 1")
    public abstract App getAppByName(String appname);

    @Query("delete from App")
    public abstract void deleteApps();
//OneUse表操作
    @Query("select * from OneUse order by id desc")
    public abstract LiveData<List<OneUse>> getAllOneUsesLive();

    @Query("select * from OneUse ")
    public abstract List<OneUse> getAllOneUses();

    @Query("select * from OneUse where startTimestamp>:timestamp")
    public abstract List<OneUse> getAllOneUsesAfter(long timestamp);

    @Insert
    public abstract void insert(OneUse oneUse);

    @Delete
    public abstract void delete(OneUse oneUse);

    @Query("delete from OneUse")
    public abstract void deleteOneUses();

    @Query("select appName  from OneUse order by id desc limit 1")
    public abstract String getCurrentAppName();
    //获取SimpleApp
    @Query("select appName,pkgName,startTimestamp from OneUse ")
    public abstract List<SimpleApp> getAll();

    @Query("select appName,pkgName,startTimestamp from OneUse where startTimestamp>:startTimeStamp")
    public abstract List<SimpleApp> getAfter(long startTimeStamp);

    @Query("select appName,pkgName,startTimestamp  from OneUse order by id desc limit 1")
    public abstract SimpleApp getCurrentApp();

    @Query("select appName,pkgName,lastRuningTime startTimestamp  from App order by useCount desc  limit :limit")
    public abstract List<SimpleApp> getMostCountSimpleApps(int limit);

    @Query("select appName from App order by appName")
    public abstract List<String> getAllAppName();

    @Query("select pkgName from App order by appName")
    public abstract List<String> getAllPkgName();
}
