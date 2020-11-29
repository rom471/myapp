package com.rom471.pred;

import android.app.Application;
import android.content.Context;
import com.rom471.adapter.AppDockAdapter;
import com.rom471.db2.MyDao;
import com.rom471.db2.MyDataBase;
import com.rom471.db2.SimpleApp;
import com.rom471.utils.DBUtils;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 本地预测器
 * 根据最多打开的应用和当前打开应用的下一个将要打开的应用，
 * 给出5个用户最有可能打开的应用
 */
public class MyPredictor {

    private SimpleApp currentApp;
    private SimpleApp lastApp;
    private MyDao simpleDao;
    private Context context;
    private List<String> app_index;
    private List<String> pkg_index;
    private int mSize;
    //应用打开矩阵
    private int[][] table;

    private static final int one_rate=3;
    private static final  int most_rate=2;

    public MyPredictor(Application context){
        this.context=context;
        MyDataBase myDataBase = MyDataBase.getInstance(context);
        simpleDao = myDataBase.getAppDao();
        initAppIndex();
        initMatrix();
    }

    /**
     * 建立app名和包名的索引
     */
    private void initAppIndex(){
        app_index=simpleDao.getAllAppName();
        pkg_index=simpleDao.getAllPkgName();
        mSize=app_index.size();
        mSize=mSize+mSize/2;//1.5倍
    }
    /**建立应用打开矩阵
     *
     */
    private void initMatrix(){
        table=new int[mSize][mSize];//矩阵
        List<SimpleApp> all = simpleDao.getAll();
        for(int i = 0; i< all.size()-2 ; i++){
            int index1=app_index.indexOf(all.get(i).getAppName());
            int index2=app_index.indexOf(all.get(i+1).getAppName());
            if(index1!=index2)
                table[index1][index2]+=1;

        }
    }
    /**
     * 更新应用打开矩阵
     * @param lastApp
     */
    private void updateMatrix(SimpleApp lastApp){
        //获得在上一个app之后打开过的app
        List<SimpleApp> all = simpleDao.getAfter(lastApp.getStartTimestamp());
        for(SimpleApp app:all){
            //如果是新打开的应用，则需要增加一个索引
           if(!app_index.contains(app.getAppName())){
               app_index.add(app.getAppName());
               pkg_index.add(app.getPkgName());
               //如果增加索引，导致超出了二维数组的size
               if(app_index.size()>mSize){
                   initMatrix();//重新建立矩阵
                   return;
               }
           }
        }
        for(int i = 0; i< all.size()-1 ; i++){
            int index1=app_index.indexOf(lastApp.getAppName());
            int index2=app_index.indexOf(all.get(i).getAppName());
            if(index1!=index2)
                table[index1][index2]+=1;
        }
    }




    /**
     * 根据历史记录，返回当前应用的下一个应用列表
     * @param currentApp 当前应用
     * @return 当前应用的下一个应用列表
     */
    private List<SimpleApp> getOneNear(SimpleApp currentApp){
        int current_index=app_index.indexOf(currentApp.getAppName());
        List<SimpleApp> ret=new ArrayList<>();
        for(int i=0;i<app_index.size();i++){
            if(table[current_index][i]!=0){
                SimpleApp app=new SimpleApp();
                app.setAppName(app_index.get(i));
                app.setPkgName(pkg_index.get(i));
                app.setWeight(table[current_index][i]*one_rate);
                ret.add(app);
            }
        }
        return ret;
    }

    /**
     * 获得打开次数最多的5个应用
     * 并分配一个权重，
     * 该权重等比例减小
     * @return
     */
    private List<SimpleApp> getMostCountsApps(){
        List<SimpleApp> most_count_5apps = simpleDao.getMostCountSimpleApps(5);
        int rate=most_rate;
        for(SimpleApp app:most_count_5apps){
            app.setWeight(rate);
            rate/=2;
        }
        return most_count_5apps;
    }

    /**
     * 合并两个SimpleApp表，
     * app名相同的会合并到一起，
     * 且它们的权重会加到一起
     * @param old_list
     * @param new_list
     * @return
     */
    private List<SimpleApp> merge(final List<SimpleApp> old_list, final List<SimpleApp> new_list){
        final Map<String, SimpleApp> listMap = new LinkedHashMap<>();
        for(SimpleApp app:old_list){
            String appname=app.getAppName();
            if(listMap.containsKey(appname)){
                SimpleApp app1 = listMap.get(appname);
                app.addWeight(app1.getWeight());
            }
            listMap.put(app.getAppName(),app);
        }
        for(SimpleApp app:new_list){
            String appname=app.getAppName();
            if(listMap.containsKey(appname)){
                SimpleApp app1 = listMap.get(appname);
                app.addWeight(app1.getWeight());
            }
            listMap.put(appname,app);
        }
        return new ArrayList<>(listMap.values());
    }

    /**
     *
     * @param appDockAdapter
     */
    public void    updateAdapter(AppDockAdapter appDockAdapter){
        currentApp= simpleDao.getCurrentApp();
        if(currentApp==null)
            return;
        if(lastApp==null){ //如果第一次预测

            initAppIndex();
            initMatrix();
            lastApp=currentApp;
        }
        else {

            updateMatrix(lastApp);
        }

        List<SimpleApp> oneNearApps = getOneNear(currentApp);

        List<SimpleApp> most_apps = getMostCountsApps();
        List<SimpleApp> top_apps =merge(oneNearApps,most_apps);
        top_apps.sort(SimpleApp.weightDescComparator);
        if(top_apps.size()>5)
            top_apps=top_apps.subList(0,5);
        DBUtils.setSimpleAppsIcon(context,top_apps );
        appDockAdapter.setmAppsList(top_apps);
        appDockAdapter.notifyDataSetChanged();
    }

}
