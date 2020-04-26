package com.tensorflow.AVK.CS426.demo;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteAssetHelper {

    private static String DB_NAME = "AVK.db";
    private static final int DB_VERSION = 8;


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
}
















