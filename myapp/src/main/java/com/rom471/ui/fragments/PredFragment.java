package com.rom471.ui.fragments;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.rom471.adapter.AppDockAdapter;
import com.rom471.db2.MyDao;
import com.rom471.db2.MyDataBase;
import com.rom471.db2.OneUse;
import com.rom471.db2.SimpleApp;
import com.rom471.net.DataSender;
import com.rom471.recorder.R;
import com.rom471.utils.AppUtils;
import com.rom471.utils.MyProperties;
import java.util.Arrays;
import java.util.List;
public class PredFragment extends Fragment implements View.OnClickListener {
    Context context;
    MyDao myDao;
    WebView webView;
    RecyclerView pred_app_from_server;
    TextView tv_server;
    AppDockAdapter predServerAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment_pred,container,false);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context=getContext();
        pred_app_from_server =getActivity().findViewById(R.id.app_pred_from_server);
        MyDataBase myDataBase = MyDataBase.getInstance(context);
        myDao = myDataBase.getAppDao();
        myDataBase = MyDataBase.getInstance(getActivity().getApplication());
        myDao = myDataBase.getAppDao();
        predServerAdapter=new AppDockAdapter(getActivity());
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(context);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        LinearLayoutManager layoutManager3= new LinearLayoutManager(context);
        layoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
        LinearLayoutManager layoutManager4= new LinearLayoutManager(context);
        layoutManager4.setOrientation(LinearLayoutManager.HORIZONTAL);
        pred_app_from_server.setLayoutManager(layoutManager4);
        pred_app_from_server.setAdapter(predServerAdapter);
        initWebView();
    }
    public void initWebView(){
        tv_server=getActivity().findViewById(R.id.pred_server_tv);
        webView=getActivity().findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);//允许与js 交互
        webView.getSettings().setDefaultTextEncodingName("utf-8");//支持中文
        //webView.addJavascriptInterface(new JsInterface(this), "androidYZH");//在js中调用本地java方法（androidYZH这个是js和安卓之间的约定，js：window.androidYZH.closeH5）
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    view.loadUrl(url);
                    return true;
                }
                return false;
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        long lastSendTimeStamp = (long)MyProperties.get(context, "lastSendTimeStamp", 0L);

        tv_server.setText(String.format("打开%s后将会打开：", myDao.getCurrentAppName()));
        //上传当前app名字到服务器，并获得推荐
        List<OneUse> allOneUses = myDao.getAllOneUsesAfter(lastSendTimeStamp);
        Handler handler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                String s = msg.getData().getString("value");

                StringBuilder sb=new StringBuilder();
                List<String> converted = Arrays.asList(s.split(",", -1));
                List<SimpleApp> simpleAppsFromNames = AppUtils.getSimpleAppsFromNames(converted);
                predServerAdapter.setmAppsList(simpleAppsFromNames);
                predServerAdapter.notifyDataSetChanged();
            }
        };
        if(allOneUses!=null){
            new Thread(()->{
                String ret = DataSender.sends(allOneUses);
                long last=0;

                Log.d("cedar", "从服务器获得 "+ret);
                if(ret!=null) {

                    //对服务器返回的进行处理
                    String[] split = ret.split(";", -1);
                    Log.d("cedar", "onResume: "+split.length);
                    if(split.length>1&&allOneUses.size()>0){//如果没有附加信息就
                        Log.d("cedar", "onResume: "+split.length);
                        last=allOneUses.get(allOneUses.size()-1).getStartTimestamp();
                        MyProperties.set(context,"lastSendTimeStamp",last);
                    }else {

                    }
                    Bundle data = new Bundle();
                    data.putString("value", split[0]);
                    Message message = new Message();
                    message.setData(data);
                    handler.sendMessage(message);
                }
            }).start();
        }
        webView.loadUrl(DataSender.getUrl());
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
        }
    }
}
