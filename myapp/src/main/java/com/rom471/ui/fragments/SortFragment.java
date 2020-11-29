package com.rom471.ui.fragments;

import androidx.fragment.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rom471.adapter.AppSortAdapter;

import com.rom471.adapter.AppDockAdapter;
import com.rom471.db2.MyDao;
import com.rom471.db2.MyDataBase;
import com.rom471.pred.MyPredictor;
import com.rom471.recorder.R;

public class SortFragment extends Fragment implements View.OnClickListener {
    RecyclerView recyclerView;
    AppDockAdapter appDockAdapter;
    MyPredictor predicter;
    RecyclerView pred_app_top_5;

    Context context;

    MyDao myDao;
    Button sortBtn;

    AppSortAdapter adapter_last;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment_sort,container,false);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context=getContext();
        recyclerView =getActivity().findViewById(R.id.app_list);
        sortBtn =getActivity().findViewById(R.id.sort_btn);
        sortBtn.setOnClickListener(this);
        MyDataBase myDataBase = MyDataBase.getInstance(context);
        myDao = myDataBase.getAppDao();
        myDataBase = MyDataBase.getInstance(getActivity().getApplication());
        myDao = myDataBase.getAppDao();
        adapter_last =new AppSortAdapter(this, myDao);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(context);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager1);
//
        recyclerView.setAdapter(adapter_last);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(context);
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);


        LinearLayoutManager layoutManager3= new LinearLayoutManager(context);
        layoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);

        LinearLayoutManager layoutManager4= new LinearLayoutManager(context);
        layoutManager4.setOrientation(LinearLayoutManager.HORIZONTAL);
        initPredView();
    }
    private void initPredView(){
        predicter=new MyPredictor(getActivity().getApplication());
        appDockAdapter =new AppDockAdapter(getActivity());
        pred_app_top_5=getActivity().findViewById(R.id.app_pred);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(context);
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        pred_app_top_5.setLayoutManager(layoutManager2);
//
        pred_app_top_5.setAdapter(appDockAdapter);
    }
    @Override
    public void onResume() {
        super.onResume();

        predicter.updateAdapter(appDockAdapter);
        //上传当前app名字到服务器，并获得推荐




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.sort_btn:

                PopupMenu popup = new PopupMenu(getActivity(), sortBtn);

                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.popup_one:
                                adapter_last.change( AppSortAdapter.CHANGE_LAST_USE);
                                sortBtn.setText(R.string.sort_1);
                                break;
                            case R.id.popup_two:
                                adapter_last.change( AppSortAdapter.CHAGE_TIME_MOST);
                                sortBtn.setText(R.string.sort_2);
                                break;
                            case R.id.popup_three:
                                adapter_last.change( AppSortAdapter.CHANGE_COUNT_MOST);
                                sortBtn.setText(R.string.sort_3);
                                break;
                        }
                        return true;
                    }
                });

                popup.show(); //showing popup menu
                break;
        }
    }
}
