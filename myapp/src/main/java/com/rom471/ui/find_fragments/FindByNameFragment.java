package com.rom471.ui.find_fragments;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rom471.db2.OneUse;
import com.rom471.recorder.R;

import java.util.ArrayList;
import java.util.List;

public class FindByNameFragment extends OneUseFindFragment{
    private Button record_search_btn;
    private EditText record_search_et;
    private TextView record_result_tv;
    public FindByNameFragment(){
        super(R.layout.main_fragment_record_find_by_name,R.id.record_list_by_name);
    }

    @Override
    void bindView() {
        record_search_btn=getActivity().findViewById(R.id.record_search_btn);
        record_search_et=getActivity().findViewById(R.id.record_search_et);
        record_result_tv =getActivity().findViewById(R.id.record_serch_result);

        record_search_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.record_search_btn:
                String appname=record_search_et.getText().toString();
                List<OneUse> records = filterByName(appname, getmOneUses());
                updateWithFoundRecords(records);
                record_result_tv.setText("查到记录：\n"+records.size()+"条");
                break;

        }
    }
    private List<OneUse> filterByName(String appname, List<OneUse> old_list){
        List<OneUse> new_list=new ArrayList<>();
        for (OneUse r:old_list
        ) {
            if (r.getAppName().toLowerCase().contains(appname.toLowerCase())){
                new_list.add(r);
            }
        }
        return  new_list;
    }
}
