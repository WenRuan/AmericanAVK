/*
 * Copyright 2020 CS426 AVK Automation Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tensorflow.AVK.CS426.demo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
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

    public ArrayList<Double> getLatitude(){
        cursor = db.rawQuery("select latitude from hydrant", new String[]{});
        ArrayList<Double> latitudeList = new ArrayList<Double>();
        while(cursor.moveToNext())
        {
            String latitude = cursor.getString(0);
            latitudeList.add(Double.parseDouble(latitude));
        }
        return latitudeList;
    }

    public ArrayList<String> getHydrants(){
        cursor = db.rawQuery("select model_name from hydrant", new String[]{});
        ArrayList<String> hydrantList = new ArrayList<String>();
        while(cursor.moveToNext())
        {
            String hydrant = cursor.getString(0);
            hydrantList.add(hydrant);
        }
        return hydrantList;
    }

    public ArrayList<Double> getLongitude(){
        cursor = db.rawQuery("select longitude from hydrant", new String[]{});
        ArrayList<Double> longitudeList = new ArrayList<Double>();
        while(cursor.moveToNext())
        {
            String longitude = cursor.getString(0);
            longitudeList.add(Double.parseDouble(longitude));
        }
        return longitudeList;
    }

    public ArrayList<String> getUserNames(){
        cursor = db.rawQuery("select UserID from Users", new String[]{});
        ArrayList<String> userList= new ArrayList<>();
        while (cursor.moveToNext())
        {
            String user = cursor.getString(0);
            userList.add(user);
        }
        return userList;
    }

    public ArrayList<String> getPasswords(){
        cursor = db.rawQuery("select Password from Users", new String[]{});
        ArrayList<String> pwList= new ArrayList<>();
        while (cursor.moveToNext())
        {
            String pass = cursor.getString(0);
            pwList.add(pass);
        }
        return pwList;
    }

    //Add Hydrant to the database
    public boolean addToDatabase(String name, String manDescription, String manLink){
        ContentValues contentValues = new ContentValues();
        contentValues.put("model_name", name);
        contentValues.put("description", manDescription);
        contentValues.put("link", manLink);
        long results = db.insert("manuals",null,contentValues);
        if(results == -1){
            return false;
        }
        else{
            return true;
        }
    }



}
