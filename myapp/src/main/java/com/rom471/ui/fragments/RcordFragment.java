package com.rom471.ui.fragments;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.rom471.recorder.R;
import com.rom471.ui.find_fragments.FindByDateFragment;
import com.rom471.ui.find_fragments.FindByNameFragment;
import com.rom471.ui.find_fragments.FindByTimeFragment;

import java.util.ArrayList;
import java.util.List;

public class RcordFragment extends Fragment implements RadioGroup.OnCheckedChangeListener{

    private List<Fragment> fragments = new ArrayList<>();
    private Fragment currentfragment;
    private FragmentManager fm;

    RadioGroup mRadioGroup;
    RadioButton rb1,rb2,rb3;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment_record,container,false);
    }
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context=getActivity();

        bindView();
        initFragments();
        mRadioGroup.setOnCheckedChangeListener(this);
        showFragmnet(fragments.get(0));
        rb1.setChecked(true);





    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void initFragments(){
        fm=getChildFragmentManager();
        fragments.add(new FindByNameFragment());
        fragments.add(new FindByDateFragment());

        fragments.add(new FindByTimeFragment());

    }
    private void showFragmnet(Fragment fragment){
        FragmentTransaction transaction = fm.beginTransaction();
        if(currentfragment==null){// 当还没有fragment时
            currentfragment=fragment;
            transaction.add(R.id.record_fragment,currentfragment).show(currentfragment).commit();
            return;
        }
        if(currentfragment!=fragment){

            transaction.hide(currentfragment);
            currentfragment=fragment;
            if(!fragment.isAdded()){
                transaction.add(R.id.record_fragment,fragment).show(fragment).commit();
            }
            else {
                transaction.show(fragment).commit();
            }
        }
    }
    private void bindView(){
        mRadioGroup=getActivity().findViewById(R.id.radioGroup1);
        rb1=getActivity().findViewById(R.id.record_findbyname);
        rb2=getActivity().findViewById(R.id.record_find_by_date);
        rb3=getActivity().findViewById(R.id.record_find_by_time);


    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.record_findbyname:
                showFragmnet(fragments.get(0));
                break;
            case R.id.record_find_by_date:
                showFragmnet(fragments.get(1));
                break;
            case R.id.record_find_by_time:
                showFragmnet(fragments.get(2));
                break;

        }
    }
}
