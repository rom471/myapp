package com.rom471.adapter;

import android.os.Build;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.rom471.db2.OneUse;
import com.rom471.recorder.R;

import java.util.List;

/**
 * 记录列表适配器
 */
public class OneUseAdapter extends RecyclerView.Adapter<OneUseAdapter.ViewHolder> {
    private List<OneUse> oneUses;
    private int position;

    public OneUseAdapter(List<OneUse> oneUses) {
        this.oneUses = oneUses;
    }

    public OneUseAdapter() {
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.oneuse_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OneUse oneuse = oneUses.get(position);
        holder.appIcon.setBackground(oneuse.getIcon());
        holder.appName.setText(oneuse.getAppName());
        holder.spendTime.setText(oneuse.getTimeSpendString());
        holder.timeStamp.setText(oneuse.getDatetime());
        holder.battery.setText("" + oneuse.getBattery() + "->" + oneuse.getBatteryAfter());
        holder.charging.setText(oneuse.getChargingString());
        holder.net.setText(oneuse.getNetString());
        holder.listView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(position);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (oneUses == null)
            return 0;
        return oneUses.size();
    }

    public void setOneUses(List<OneUse> filterByName) {
        this.oneUses = filterByName;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        LinearLayout listView;
        ImageView appIcon;
        TextView id;
        TextView appName;
        TextView spendTime;
        TextView timeStamp;
        TextView battery;
        TextView charging;
        TextView net;

        public ViewHolder(View view) {
            super(view);
            view.setOnCreateContextMenuListener(this);
            listView = view.findViewById(R.id.list_view);
            appIcon = (ImageView) view.findViewById(R.id.appicon_img);
            id = (TextView) view.findViewById(R.id.id_tv);
            appName = (TextView) view.findViewById(R.id.appname_tv);
            spendTime = (TextView) view.findViewById(R.id.spendtime_tv);
            timeStamp = (TextView) view.findViewById(R.id.timestamp_tv);
            battery = (TextView) view.findViewById(R.id.battery_tv);
            charging = (TextView) view.findViewById(R.id.charging_tv);
            net = (TextView) view.findViewById(R.id.net_tv);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle(appName.getText());
            menu.add(0, 0, 0, "删除记录");

        }

    }


}
