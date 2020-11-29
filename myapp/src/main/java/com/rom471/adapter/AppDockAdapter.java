package com.rom471.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.rom471.db2.SimpleApp;
import com.rom471.recorder.R;
import java.util.List;

/**
 * 应用dock适配器
 * 通过点击应用图标，可以启动应用
 */
public class AppDockAdapter extends RecyclerView.Adapter<AppDockAdapter.ViewHolder> {

    private List<SimpleApp> mAppsList;

    private Context context;



    public void setmAppsList(List<SimpleApp> mAppsList) {
        this.mAppsList = mAppsList;
    }
    public AppDockAdapter(Context context){
        this.context=context;



    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.app_dock_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(position>=5) {
            return;
        }
        SimpleApp app=mAppsList.get(position);
        if(app==null) return;



        holder.appIcon.setBackground(app.getIcon());
        holder.appName.setText(app.getAppName());
        holder.appIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent =context.getPackageManager().getLaunchIntentForPackage(app.getPkgName());
                    context.startActivity(intent);
                    toast("启动成功");
                }catch (Exception e){
                    toast("打开失败");
                }
                Log.d("TAG", "onLongClick: "+position);

            }
        });

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

        public ViewHolder (View view)
        {
            super(view);
            appIcon = (ImageView) view.findViewById(R.id.appicon);
            appName = (TextView) view.findViewById(R.id.appname);
            appInfo = (TextView) view.findViewById(R.id.app_info);
        }

    }
    private   void toast( String text){
        Toast toast=Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
