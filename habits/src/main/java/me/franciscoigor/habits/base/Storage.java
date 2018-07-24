package me.franciscoigor.habits.base;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Storage {

    public final static String SAVED_DATA="saved_data";
    private static SharedPreferences settings;
    private static HashMap<String, Serializable> savedData;
    private static Bundle savedInstanceState;



    public static void loadStorage(Bundle instanceState,Activity activity){
        settings = PreferenceManager.getDefaultSharedPreferences(activity);
        savedInstanceState = instanceState;

        if (savedInstanceState!=null){
            savedData = (HashMap<String, Serializable>)savedInstanceState.getSerializable(SAVED_DATA);
        }
        if (savedData==null){
            savedData=new HashMap<String, Serializable>();
            Map<String, ?> values=settings.getAll();
            for (String key: values.keySet()
                 ) {
                savedData.put(key,(Serializable)values.get(key));
            }
        }
    }

    public static void saveStorage(Bundle savedInstance){
        if (savedInstance!=null){
            savedInstance.putSerializable(SAVED_DATA,savedData);
        }

        SharedPreferences.Editor editor=settings.edit();
        for (String key: savedData.keySet()
                ) {
            Serializable data=savedData.get(key);
            editor.putString(key,getString(key,""));
        }
        editor.commit();
        System.out.println("Saving "+savedData);
    }

    public static Serializable getObject(String key,Serializable defaultValue){
        Serializable data=savedData.get(key);
        if (data==null) return defaultValue;
        return data;
    }

    public static String getString(String key,String defaultValue){
        return getObject(key,defaultValue).toString();
    }

    public static int getInt(String key,Integer defaultValue){
        return Integer.valueOf(getString(key,defaultValue.toString()));
    }

    public static void setObject(String key,Serializable value){
        savedData.put(key, value);
    }

    public static void setInt(String key, Integer value){
        setObject(key,value);
    }

    public static void addInt(String key, Integer amount){
        int value=getInt(key,0);
        setObject(key,value+ amount);
    }

}
