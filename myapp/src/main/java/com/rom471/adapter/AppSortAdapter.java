package com.rom471.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import com.rom471.db2.App;
import com.rom471.db2.MyDao;
import com.rom471.recorder.R;
import com.rom471.utils.DBUtils;
import java.util.List;

/**
 * 应用排序RecyclerView的适配器
 */
public class AppSortAdapter extends RecyclerView.Adapter<AppSortAdapter.ViewHolder> {
    public static final int CHANGE_LAST_USE=0;
    public static final int CHAGE_TIME_MOST=1;
    public static final int CHANGE_COUNT_MOST=2;
    private List<App> mAppsList;
    private static final int APP_LIST_SIZE=100;
    private LiveData<List<App>> liveData;
    private Fragment context;
    private MyDao myDao;
    private int sort_type;

    public void setmAppsList(List<App> mAppsList) {
        this.mAppsList = mAppsList;
    }

    public AppSortAdapter(Fragment context, MyDao myDao){
        this.context=context;
        this.myDao = myDao;
        change( CHANGE_LAST_USE);

    }

    /**
     * 改变排序方式
     * @param type
     */
    public void change(int type){
        this.sort_type =type;
        switch (type){
            case  CHAGE_TIME_MOST:
                liveData= myDao.getMostUsedApps(APP_LIST_SIZE);
                break;
            case  CHANGE_LAST_USE:
                liveData= myDao.getLastUsedApp(APP_LIST_SIZE);
                break;
            case  CHANGE_COUNT_MOST:
                liveData= myDao.getMostCountsApps(APP_LIST_SIZE);
                break;

        }
        liveData.observe( context, new Observer<List<App>>() {
            @Override
            public void onChanged(List<App> apps) {
                DBUtils.setAppsIcon(context.getContext(),apps);
                setmAppsList(apps);
                notifyDataSetChanged();
            }
        });
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sort_app_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        App app=mAppsList.get(position);
        holder.appIcon.setBackground(app.getIcon());
        holder.appName.setText(app.getAppName());
        holder.appRank.setText(""+(position+1));
        switch (sort_type){
            case  CHAGE_TIME_MOST:
                holder.appInfo.setText(DBUtils.getTimeSpendString(app.getTotalRuningTime()) );
                break;
            case  CHANGE_LAST_USE:
                holder.appInfo.setText(DBUtils.getSinceTimeString(app.getLastRuningTime())+"前");
                break;
            case  CHANGE_COUNT_MOST:
                holder.appInfo.setText(app.getUseCount()+"次");
                break;
        }
    }

    @Override
    public int getItemCount() {
        if(mAppsList!=null)
        return mAppsList.size();
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView appIcon;
        TextView appName;
        TextView appInfo;
        TextView appRank;

        public ViewHolder (View view)
        {
            super(view);
            appIcon = (ImageView) view.findViewById(R.id.appicon);
            appName = (TextView) view.findViewById(R.id.appname);
            appInfo = (TextView) view.findViewById(R.id.app_info);
            appRank = (TextView) view.findViewById(R.id.app_rank);
        }

    }
}
