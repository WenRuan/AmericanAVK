package com.tensorflow.AVK.CS426.demo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {

    //Variable definitions
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;
    Cursor cursor = null;


    //Basic Constructor
    private DatabaseAccess(Context context){

        this.openHelper = new DatabaseHelper(context);

    }

    //Could be merged with the constructor...
    public static DatabaseAccess getInstance(Context context){
        if(instance==null){
            instance=new DatabaseAccess(context);
        }
        return instance;
    }


    //Opens the database file
    public void open(){
        this.db = openHelper.getWritableDatabase();
    }

    //closes the database file
    public void close(){
        if(db!=null){
            this.db.close();
        }
    }


    //Get the hydrant link using the name
    public String getLink(String model_name){
        cursor = db.rawQuery("select link from manuals where model_name = '"+model_name+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while(cursor.moveToNext()){
            String link = cursor.getString(0);
                    buffer.append(""+link);
        }
        return buffer.toString();
    }


    public ArrayList<String> getModelName(){
        cursor = db.rawQuery("select model_name from manuals", new String[]{});
        StringBuffer buffer = new StringBuffer();
        ArrayList<String> nameList = new ArrayList<String>();
        while(cursor.moveToNext())
        {
            String name = cursor.getString(0);
            nameList.add(name);
        }
        return nameList;
    }


}
