package com.ultimate.mobilecashierbarcode;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ActivityUpdateData extends AppCompatActivity {
    private Button update;
    private EditText editBarcode,editNama;
    DataHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);

        dbHelper = new DataHelper(this);

        editBarcode = (EditText) findViewById(R.id.txtEditBarcode);
        editNama = (EditText) findViewById(R.id.txtEditNama);
        update = (Button) findViewById(R.id.btnUpdateBarang);
        String barcode = getIntent().getStringExtra("barcode");
        String nama = getIntent().getStringExtra("nama");

        editBarcode.setText(String.valueOf(barcode));
        editNama.setText(String.valueOf(nama));

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.execSQL("UPDATE tbl_barang SET nama='"+editNama.getText().toString()+"' WHERE barcode='"+editBarcode.getText().toString()+"';");
                ActivityDaftarBarang.adb.RefreshList();
                finish();
            }
        });

    }
}
