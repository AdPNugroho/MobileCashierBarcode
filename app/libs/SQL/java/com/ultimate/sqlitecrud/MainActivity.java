package com.ultimate.sqlitecrud;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    String[] daftar,kode,textList;
    ListView ListView01;
    Menu menu;
    protected Cursor cursor;
    DataHelper dbcenter;
    public static MainActivity ma;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,BuatBarang.class);
                startActivity(i);
            }
        });

        ma = this;
        dbcenter = new DataHelper(this);
        RefreshList();
    }

    public void RefreshList(){
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM barang",null);
        kode = new String[cursor.getCount()];
        daftar = new String[cursor.getCount()];
        textList = new String[cursor.getCount()];
        cursor.moveToFirst();
        for(int cc=0;cc<cursor.getCount();cc++){
            cursor.moveToPosition(cc);
            kode[cc] = cursor.getString(0).toString();
            daftar[cc] = cursor.getString(1).toString();
            textList[cc] = cursor.getString(0).toString() + " - " + cursor.getString(1).toString();
        }
        ListView01 = (ListView) findViewById(R.id.listView1);
        ListView01.setAdapter(new ArrayAdapter(this,android.R.layout.simple_list_item_1,textList));
        ListView01.setSelected(true);
        ListView01.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                final String selection = kode[position];
                final CharSequence[] dialogitem = {"Lihat Biodata","Update Biodata","Hapus Biodata"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Pilihan");
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which){
                            case 0:
                                Intent i = new Intent(getApplicationContext(),LihatBarang.class);
                                i.putExtra("nama",selection);
                                startActivity(i);
                                break;
                            case 1:
                                Intent in = new Intent(getApplicationContext(),UpdateBarang.class);
                                in.putExtra("nama",selection);
                                startActivity(in);
                                break;
                            case 2:
                                final CharSequence[] pertanyaan = {"Yes","No"};
                                AlertDialog.Builder question = new AlertDialog.Builder(MainActivity.this);
                                question.setTitle("Yakin Menghapus ?");
                                question.setItems(pertanyaan, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch(which){
                                            case 0:
                                                SQLiteDatabase db = dbcenter.getWritableDatabase();
                                                db.execSQL("DELETE FROM barang WHERE kode='"+selection+"'");
                                                RefreshList();
                                                break;
                                            case 1:
                                                break;
                                        }
                                    }
                                });
                                question.create().show();
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });
        ((ArrayAdapter)ListView01.getAdapter()).notifyDataSetInvalidated();
    }
}
