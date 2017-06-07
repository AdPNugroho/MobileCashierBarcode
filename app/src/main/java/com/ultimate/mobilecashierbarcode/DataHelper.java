package com.ultimate.mobilecashierbarcode;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "mobilecashier.db";
    private static final int DATABASE_VERSION = 1;

    public DataHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String tbl_barang = "CREATE TABLE tbl_barang(id INTEGER PRIMARY KEY AUTOINCREMENT, barcode INTEGER NOT NULL,nama TEXT NOT NULL);";
        Log.d("Data","onCreate : " + tbl_barang);
        db.execSQL(tbl_barang);
        String tbl_rule = "CREATE TABLE tbl_rule(id INTEGER PRIMARY KEY AUTOINCREMENT, barcode INTEGER NOT NULL,jumlah INTEGER NOT NULL, harga INTEGER NOT NULL);";
        Log.d("Data","onCreate : " + tbl_rule);
        db.execSQL(tbl_rule);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tbl_barang");
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS tbl_rule");
        onCreate(db);
    }

}