package com.ultimate.mobilecashierbarcode;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ActivityDataBarang extends AppCompatActivity {
    private Button scan, save;
    private EditText inputBarcode, inputNama, inputHarga;
    protected Cursor cursor;
    DataHelper dbHelper,dbCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_barang);
        dbHelper = new DataHelper(this);

        scan = (Button) findViewById(R.id.btnScanBarang);
        save = (Button) findViewById(R.id.btnSaveBarang);
        inputBarcode = (EditText) findViewById(R.id.txtBarcode);
        inputNama = (EditText) findViewById(R.id.txtNama);
        inputHarga = (EditText) findViewById(R.id.txtHarga);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(ActivityDataBarang.this);
                integrator.setOrientationLocked(false);
                integrator.setCaptureActivity(CustomScanner.class);
                integrator.initiateScan();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase dbr = dbHelper.getReadableDatabase();
                String valueBarcode = inputBarcode.getText().toString();
                String valueNama = inputNama.getText().toString();
                String valueHarga = inputHarga.getText().toString();
                if(valueBarcode.matches("")||valueHarga.matches("")||valueNama.matches("")){
                    Toast.makeText(getApplicationContext(),"Input Harus Lengkap",Toast.LENGTH_LONG).show();
                }else{
                    cursor = dbr.rawQuery("SELECT * FROM tbl_barang where barcode='"+valueBarcode+"'",null);
                    if(cursor.getCount()>0){
                        Toast.makeText(getApplicationContext(),"Barang dengan barcode ini sudah terdaftar",Toast.LENGTH_LONG).show();
                    }else{
                        SQLiteDatabase tbl_barang = dbHelper.getWritableDatabase();
                        tbl_barang.execSQL("insert into tbl_barang(barcode,nama) values('"+valueBarcode+"','"+valueNama+ "')");

                        SQLiteDatabase tbl_rule = dbHelper.getWritableDatabase();
                        tbl_rule.execSQL("insert into tbl_rule(barcode,jumlah,harga) values('"+valueBarcode+"','1','"+valueHarga+ "')");
                        Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_LONG).show();
                        inputBarcode.setText("");
                        inputNama.setText("");
                        inputHarga.setText("");
                    }
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            String barcode = result.getContents();
            inputBarcode.setText(String.valueOf(barcode));
            inputNama.requestFocus();
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
