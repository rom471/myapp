package com.rom471.ui.find_fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;

import com.rom471.db2.OneUse;
import com.rom471.recorder.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FindByTimeFragment extends OneUseFindFragment {
    private Button start_time_btn;
    private Button end_time_btn;
    private Button start_btn;
    private TextView start_time_tv;
    private TextView end_time_tv;
    private TextView result_tv;
    private long start_timestamp;
    private long end_timestamp;

    public FindByTimeFragment() {
        super(R.layout.main_fragment_record_find_by_time, R.id.record_list_by_time);
    }

    public void bindView() {
        start_time_btn = getActivity().findViewById(R.id.record_time_start_btn);
        end_time_btn = getActivity().findViewById(R.id.record_time_end_btn);
        start_btn = getActivity().findViewById(R.id.time_start_search);
        start_time_tv = getActivity().findViewById(R.id.record_time_start_tv);
        end_time_tv = getActivity().findViewById(R.id.record_time_end_tv);
        result_tv = getActivity().findViewById(R.id.time_search_result);
        start_btn.setOnClickListener(this);
        start_time_btn.setOnClickListener(this);
        end_time_btn.setOnClickListener(this);

    }

    //过滤时间
    private List<OneUse> filterByTime(List<OneUse> old_list, long start, long end) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        if (start_timestamp == 0 && end_timestamp == 0) //未指定时间则返回所有记录
            return old_list;
        List<OneUse> new_list = new ArrayList<>();
        for (OneUse r : old_list
        ) {
            long timestamp = r.getStartTimestamp();

            cal.setTimeInMillis(timestamp);
            int hours = cal.get(Calendar.HOUR_OF_DAY);
            int minutes = cal.get(Calendar.MINUTE);
            //int ms_in_day=(hours*60+minutes)*60*1000;
            int ms_in_day = cal.get(Calendar.MILLISECONDS_IN_DAY);

            if (start != 0 && ms_in_day < start)
                continue;
            if (end != 0 && ms_in_day > end)
                continue;
            new_list.add(r);
        }
        return new_list;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        Log.d("TAG", "onClick:按下了 ");
        switch (v.getId()) {
            case R.id.record_time_start_btn:

                showTimePickerDialog(getActivity(), true);
                break;
            case R.id.record_time_end_btn:
                showTimePickerDialog(getActivity(), false);
                break;
            case R.id.time_start_search:
                List<OneUse> records = filterByTime(getmOneUses(), start_timestamp, end_timestamp);
                result_tv.setText("查到记录：\n" + records.size() + "条");
                updateWithFoundRecords(records);

                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showTimePickerDialog(Context context, boolean start) {
        Calendar cal = Calendar.getInstance();
        Dialog timeDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                SimpleDateFormat format = new SimpleDateFormat(" HH:mm ");
                // TODO Auto-generated method stub
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                cal.set(Calendar.MINUTE, minute);
                long timestamp = cal.get(Calendar.MILLISECONDS_IN_DAY);
                if (start) {
                    start_time_tv.setText(format.format(cal.getTime()));
                    start_timestamp = timestamp;
                } else {
                    end_time_tv.setText(format.format(cal.getTime()));
                    end_timestamp = timestamp;
                }
            }
        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
        timeDialog.setTitle("请选择时间");
        timeDialog.show();
    }

}
