package com.rom471.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 *
 */
public class MyProperties {
    private static String CONFIG_FILE = "app_properties";
    public static boolean have(Context context,String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(CONFIG_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.contains(key);
    }
    public static void set(Context context , String key, Object object){
        String type = object.getClass().getSimpleName();
        SharedPreferences sharedPreferences = context.getSharedPreferences(CONFIG_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if("String".equals(type)){
            editor.putString(key, (String)object);
        }
        else if("Integer".equals(type)){
            editor.putInt(key, (Integer)object);
        }
        else if("Boolean".equals(type)){
            editor.putBoolean(key, (Boolean)object);
        }
        else if("Float".equals(type)){
            editor.putFloat(key, (Float)object);
        }
        else if("Long".equals(type)){
            editor.putLong(key, (Long)object);
        }
        editor.commit();
    }
    public static Object get(Context context , String key, Object defaultObject){
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sharedPreferences = context.getSharedPreferences(CONFIG_FILE, Context.MODE_PRIVATE);

        if("String".equals(type)){
            return sharedPreferences.getString(key, (String)defaultObject);
        }

        else if("Integer".equals(type)){
            return sharedPreferences.getInt(key, (Integer)defaultObject);
        }

        else if("Boolean".equals(type)){
            return sharedPreferences.getBoolean(key, (Boolean)defaultObject);
        }

        else if("Float".equals(type)){
            return sharedPreferences.getFloat(key, (Float)defaultObject);
        }

        else if("Long".equals(type)){
            return sharedPreferences.getLong(key, (Long)defaultObject);
        }

        return null;
    }


    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG_FILE,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().commit();
    }




}
