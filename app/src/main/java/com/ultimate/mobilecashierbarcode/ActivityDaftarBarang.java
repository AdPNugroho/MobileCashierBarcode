package com.ultimate.mobilecashierbarcode;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class ActivityDaftarBarang extends AppCompatActivity{
    private String[] id,barcode,nama;
    protected Cursor cursor;
    DataHelper dbcenter;
    private RecyclerView.Adapter adapter;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_barang);
        dbcenter = new DataHelper(this);
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM tbl_barang",null);
        id = new String[cursor.getCount()];
        barcode = new String[cursor.getCount()];
        nama = new String[cursor.getCount()];

        for(int cc=0;cc<cursor.getCount();cc++){
            cursor.moveToPosition(cc);
            id[cc] = cursor.getString(0).toString();
            barcode[cc] = cursor.getString(1).toString();
            nama[cc] = cursor.getString(2).toString();
        }

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerBarang);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CardViewAdapter(id,barcode,nama);
        mRecyclerView.setAdapter(adapter);
    }
}
