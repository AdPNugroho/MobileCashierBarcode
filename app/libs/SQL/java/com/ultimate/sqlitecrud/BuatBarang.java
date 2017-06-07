package com.ultimate.sqlitecrud;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Ultimate on 6/2/2017.
 */

public class BuatBarang extends AppCompatActivity{
    protected Cursor cursor;
    DataHelper dbHelper,dbCenter;
    Button ton1, ton2;
    EditText text1, text2, text3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buat_barang);

        dbHelper = new DataHelper(this);
        text1 = (EditText) findViewById(R.id.editText1);
        text2 = (EditText) findViewById(R.id.editText2);
        text3 = (EditText) findViewById(R.id.editText3);
        ton1 = (Button) findViewById(R.id.button1);
        ton2 = (Button) findViewById(R.id.button2);

        ton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                SQLiteDatabase dbr = dbHelper.getReadableDatabase();
                cursor = dbr.rawQuery("SELECT * FROM barang where kode='"+text1.getText().toString()+"'",null);
                if(cursor.getCount()>0){
                    Toast.makeText(getApplicationContext(),"Barang dengan barcode ini sudah terdaftar",Toast.LENGTH_LONG).show();
                    text1.requestFocus();
                }else{
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.execSQL("insert into barang(kode,nama,harga) values('" +text1.getText().toString() + "','" +text2.getText().toString() + "','" +text3.getText().toString() + "')");
                    Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_LONG).show();
                    MainActivity.ma.RefreshList();
                    finish();
                }
            }
        });
        ton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }

}
