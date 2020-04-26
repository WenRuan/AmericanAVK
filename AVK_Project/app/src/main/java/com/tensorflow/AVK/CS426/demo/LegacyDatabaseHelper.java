package com.tensorflow.AVK.CS426.demo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LegacyDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "databases/AVK.db";
    private static final String DB_MANUALS = "manuals";
    //private static final String DB_HYDRANT = "hydrants";

    //columns for manuals table
    private static final String model_name = "model";
    private static final String description = "description";
    private static final String product_type = "product";
    private static final String link = "link";

    /*
    //columns for hydrants table
    private static final String hydrant_id = "ID";
    private static final String model_name = "model";
    private static final String latitude = "latitude";
    private static final String longitude = "longitude";
    private static final String date_added = "date_added";
    //private static final String model_name = "model_name";

    private static final String CREATE_TABLE = "create table " + DB_HYDRANT + " (" +
            hydrant_id + " integer primary key autoincrement, " +
            model_name + " varchar(50), " +
            latitude + " blob not null, " +
            longitude + " blob not null, " +
            date_added + " date);";
    */

    private static final String CREATE_TABLE = "create table " + DB_MANUALS + " (" +
            model_name + " varchar(50) primary key, " +
            //description + " varchar(50), " +
            //product_type + " varchar(50), " +
            link + " blob, " +
            description + " varchar(50));";

    //! IMPORTANT ! change the version number if you make any changes to the overall TABLE
    public LegacyDatabaseHelper(Context context) {
        super(context, DB_NAME, null, 8);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int il) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DB_MANUALS);
        onCreate(sqLiteDatabase);
    }

    //creating a method to insert data by using the insert function
    public boolean insertData(String name, String desc, String links){
        SQLiteDatabase db = this.getWritableDatabase(), sqLiteDatabase;;
        ContentValues contentValues = new ContentValues();
        contentValues.put(model_name, name);
        contentValues.put(description, desc);
        //contentValues.put(product_type, prod);
        contentValues.put(link, links);

        long result = db.insert(DB_MANUALS, null, contentValues);

        //if result = -1, then data does not get inserted
        return result != -1;

    }

    //creating a method to view data by using a select * statement
    public Cursor browseActivity(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from " + DB_MANUALS;
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }
}