package com.rom471.ui.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.rom471.db2.MyDao;
import com.rom471.db2.MyDataBase;
import com.rom471.db2.OneUse;
import com.rom471.net.DataSender;
import com.rom471.recorder.R;
import com.rom471.utils.SettingUtils;

import java.util.List;


public class SettingsFragment extends Fragment implements View.OnClickListener {

    private Button accessibility_btn;
    private Button clearRecord_btn;
    private Button about_btn;
    private Button setHost_btn;
    private Button ouput_db_btn;
    private Button post_records_btn;
    private MyDao myDao;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment_settings, container, false);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getContext();
        myDao = MyDataBase.getAppDao();
        accessibility_btn = getActivity().findViewById(R.id.open_accessibility_btn);
        accessibility_btn.setOnClickListener(this);
        clearRecord_btn = getActivity().findViewById(R.id.clear_records_btn);
        about_btn = getActivity().findViewById(R.id.about_btn);
        ouput_db_btn = getActivity().findViewById(R.id.output_db_btn);
        post_records_btn = getActivity().findViewById(R.id.post_records_btn);
        setHost_btn = getActivity().findViewById(R.id.set_host_ip_btn);
        setHost_btn.setOnClickListener(this);
        post_records_btn.setOnClickListener(this);
        ouput_db_btn.setOnClickListener(this);
        clearRecord_btn.setOnClickListener(this);
        about_btn.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear_records_btn:

                SettingUtils.confirmClearRecordsDialog(context, myDao);

                break;
            case R.id.set_host_ip_btn:
                //TODO
                SettingUtils.alert_host_edit(context);

                break;
            case R.id.about_btn:
                //TODO

                SettingUtils.aboutDialog(context);
                break;
            case R.id.open_accessibility_btn:
                SettingUtils.getAccessibilityPermission(getContext());
                break;

            case R.id.output_db_btn:
                if (SettingUtils.verifyStoragePermissions(getActivity())) {
                    SettingUtils.export_db(context);
                }

                break;
            case R.id.post_records_btn:
                List<OneUse> allOneUses = myDao.getAllOneUses();
                new Thread(() -> {
                    DataSender.sends_all(allOneUses);
                }).start();
                Toast.makeText(context, "上传数据"+allOneUses.size()+"条",Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean accessibilitySettingsOn = SettingUtils.isAccessibilitySettingsOn(context);
        if (accessibilitySettingsOn) {
            accessibility_btn.setText("辅助功能已经打开");
        } else {
            accessibility_btn.setText("点击打开辅助功能");
        }
    }


}
