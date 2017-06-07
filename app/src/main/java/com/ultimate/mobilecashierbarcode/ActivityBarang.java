package com.ultimate.mobilecashierbarcode;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActivityBarang extends AppCompatActivity {
    private Button tambah,daftar,exit;
    private TextView jumlah;
    protected Cursor cursor;
    DataHelper dbHelper;
    public static ActivityBarang ab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barang);
        tambah = (Button) findViewById(R.id.btnTambahBarang);
        daftar = (Button) findViewById(R.id.btnDaftarBarang);
        exit = (Button) findViewById(R.id.btnExitBarang);
        jumlah = (TextView) findViewById(R.id.textView10);
        ab = this;
        dbHelper = new DataHelper(this);
        refreshCounter();

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(),ActivityDataBarang.class);
                startActivity(it);
            }
        });
        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent id = new Intent(getApplicationContext(),ActivityDaftarBarang.class);
                startActivity(id);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void refreshCounter(){
        SQLiteDatabase dbr = dbHelper.getReadableDatabase();
        cursor = dbr.rawQuery("SELECT * FROM tbl_barang",null);
        int total = cursor.getCount();
        jumlah.setText(String.valueOf(total));
    }
}
