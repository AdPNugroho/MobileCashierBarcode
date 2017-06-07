package com.ultimate.sqlitecrud;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "barang.db";
    private static final int DATABASE_VERSION = 1;

    public DataHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String sql = "CREATE TABLE barang(kode integer primary key,nama text null, harga integer null);";
        Log.d("Data","onCreate : " + sql);
        db.execSQL(sql);
        sql = "INSERT INTO barang(kode,nama,harga) VALUES ('1','Barang A','1000');";
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase args0,int arg1,int arg2){

    }

}
