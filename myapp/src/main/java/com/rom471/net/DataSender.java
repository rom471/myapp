package com.rom471.net;
import com.rom471.db2.OneUse;
import com.rom471.utils.AppUtils;
import com.rom471.utils.MyProperties;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

/**
 * 发送数据的工具类
 */
public class DataSender {
    /**
     * 获得服务器的主页url
     * @return 主页地址
     */
    public static String getUrl(){
        String host =(String) MyProperties.get(AppUtils.getContext(), "host", "192.168.199.151");
        return "http://"+host+":5000/";
    }

    /**
     * 将OneUse的数据发送给服务器的sends接口
     * @param oneUses
     * @return
     */
    public static String sends(List<OneUse>oneUses){
        return sendOneUsesTo(oneUses,"sends");
    }

    /**
     * 将OneUse的数据发送给服务器的sends_all接口
     * @param oneUses
     * @return
     */
    public static String sends_all(List<OneUse>oneUses){
        return sendOneUsesTo(oneUses,"sends_all");
    }


    /**
     * 将OneUse列表里的app名和时间戳转换成post参数
     * @param oneUses
     * @return
     */
    private static String changeOneUses(List<OneUse> oneUses){
        StringBuilder sb_appnames = new StringBuilder();
        StringBuilder sb_timestamps = new StringBuilder();
        String coma="";
        for (OneUse oneUse : oneUses) {
            sb_appnames.append(coma).append(oneUse.getAppName());
            sb_timestamps.append(coma).append(oneUse.getStartTimestamp());
            coma=",";
        }

        String data = "appnames=" + sb_appnames.toString() +
                "&timestamps=" + sb_timestamps.toString();
        return data;
    }

    /**
     * 将数据使用post发送到一个url
     * @param data 要post的数据
     * @param url_str url地址
     * @return post后，服务器返回的信息；如果出错，返回""
     */
    private static String sendStringToURL(String data,String url_str){
        try {
            URL url = new URL(url_str);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
            //至少要设置的两个请求头
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //post的方式提交实际上是留的方式提交给服务器
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data.getBytes());
            //获得结果码
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                InputStream is = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
                StringBuilder sb=new StringBuilder();
                String strRead = null;
                while ((strRead = reader.readLine()) != null) {
                    sb.append(strRead);
                }

                return sb.toString();
            } else {
                //请求失败
                return "";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 将OneUse列表的数据发送给服务器的某个接口
     * @param oneUses OneUse列表
     * @param to 服务器的接口名
     * @return 服务器的结果
     */
    private static String sendOneUsesTo(List<OneUse> oneUses,String to) {
        String data=changeOneUses(oneUses);
        String url=getUrl()+to;
        return sendStringToURL(data,url);
    }



}
