package com.ultimate.mobilecashierbarcode;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tapadoo.alerter.Alerter;

public class ActivityDaftarBarang extends AppCompatActivity {
    private String[] id, barcode, nama, text;
    private String searchBarcodeValue;
    ListView listBarang;
    Menu menu;
    protected Cursor cursor;
    DataHelper dbcenter;
    public static ActivityDaftarBarang adb;
    Context context;
    EditText searchBarcode;
    final Context contextDialog = this;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_barang);
        btn = (Button) findViewById(R.id.btnSearchData);
        String valueBtn = btn.getText().toString();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn.getText().toString() == "RESET") {
                    RefreshList();
                } else {
                    LayoutInflater li = LayoutInflater.from(contextDialog);
                    final View promptsView = li.inflate(R.layout.custom_dialog_searchbarang, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(contextDialog);
                    alertDialogBuilder.setTitle("Tambah Rule Harga");
                    alertDialogBuilder.setView(promptsView);
                    searchBarcode = (EditText) promptsView.findViewById(R.id.barcodeScanValue);
                    final Button btnScanBarcodeSearch = (Button) promptsView.findViewById(R.id.btnSearchScan);
                    btnScanBarcodeSearch.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            IntentIntegrator integrator = new IntentIntegrator(ActivityDaftarBarang.this);
                            integrator.setOrientationLocked(false);
                            integrator.setCaptureActivity(CustomScanner.class);
                            integrator.initiateScan();
                        }
                    });
                    alertDialogBuilder.setCancelable(false)
                            .setPositiveButton("Cari",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            SearchList(searchBarcodeValue);
                                        }
                                    })
                            .setNegativeButton("Batal",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    alertDialog.show();
                }
            }
        });
        adb = this;
        dbcenter = new DataHelper(this);
        RefreshList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            searchBarcodeValue = result.getContents();
            searchBarcode.setText(String.valueOf(searchBarcodeValue));
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void RefreshList() {
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM tbl_barang", null);
        id = new String[cursor.getCount()];
        barcode = new String[cursor.getCount()];
        nama = new String[cursor.getCount()];
        text = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int cc = 0; cc < cursor.getCount(); cc++) {
            cursor.moveToPosition(cc);
            id[cc] = cursor.getString(0).toString();
            barcode[cc] = cursor.getString(1).toString();
            nama[cc] = cursor.getString(2).toString();
            text[cc] = cursor.getString(1).toString() + "\n" + cursor.getString(2).toString();
        }
        listBarang = (ListView) findViewById(R.id.listDaftarBarang);
        listBarang.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, text));
        listBarang.setSelected(true);
        listBarang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                final String selection = barcode[position];
                final String namaSelection = nama[position];
                final CharSequence[] dialogitem = {"Lihat Rule", "Update Data", "Hapus Data"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityDaftarBarang.this);
                builder.setTitle("Pilihan");
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent i = new Intent(getApplicationContext(), ActivityRule.class);
                                i.putExtra("barcode", selection);
                                i.putExtra("nama", namaSelection);
                                startActivity(i);
                                break;
                            case 1:
                                Intent in = new Intent(getApplicationContext(), ActivityUpdateData.class);
                                in.putExtra("barcode", selection);
                                in.putExtra("nama", namaSelection);
                                startActivity(in);
                                break;
                            case 2:
                                final CharSequence[] pertanyaan = {"Yes", "No"};
                                AlertDialog.Builder question = new AlertDialog.Builder(ActivityDaftarBarang.this);
                                question.setTitle("Yakin Menghapus ?");
                                question.setItems(pertanyaan, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case 0:
                                                SQLiteDatabase tbl_barang = dbcenter.getWritableDatabase();
                                                tbl_barang.execSQL("DELETE FROM tbl_barang WHERE barcode='" + selection + "'");

                                                SQLiteDatabase tbl_rule = dbcenter.getWritableDatabase();
                                                tbl_rule.execSQL("DELETE FROM tbl_rule WHERE barcode='" + selection + "'");
                                                Alerter.create(ActivityDaftarBarang.this)
                                                        .setTitle("Information")
                                                        .setText("Data Berhasil DiHapus !")
                                                        .setBackgroundColor(R.color.colorHintFalse)
                                                        .show();
                                                RefreshList();
                                                ActivityBarang.ab.refreshCounter();
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
        ((ArrayAdapter) listBarang.getAdapter()).notifyDataSetInvalidated();
        btn.setText("CARI DATA BARANG");
    }

    public void SearchList(String kode) {
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM tbl_barang WHERE barcode='" + kode + "'", null);
        if (cursor.getCount() > 0) {
            btn.setText("RESET");
            id = new String[cursor.getCount()];
            barcode = new String[cursor.getCount()];
            nama = new String[cursor.getCount()];
            text = new String[cursor.getCount()];
            cursor.moveToFirst();
            for (int cc = 0; cc < cursor.getCount(); cc++) {
                cursor.moveToPosition(cc);
                id[cc] = cursor.getString(0).toString();
                barcode[cc] = cursor.getString(1).toString();
                nama[cc] = cursor.getString(2).toString();
                text[cc] = cursor.getString(1).toString() + "\n" + cursor.getString(2).toString();
            }
            listBarang = (ListView) findViewById(R.id.listDaftarBarang);
            listBarang.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, text));
            listBarang.setSelected(true);
            listBarang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View view, int position, long id) {
                    final String selection = barcode[position];
                    final String namaSelection = nama[position];
                    final CharSequence[] dialogitem = {"Lihat Rule", "Update Data", "Hapus Data"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityDaftarBarang.this);
                    builder.setTitle("Pilihan");
                    builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    Intent i = new Intent(getApplicationContext(), ActivityRule.class);
                                    i.putExtra("barcode", selection);
                                    i.putExtra("nama", namaSelection);
                                    startActivity(i);
                                    break;
                                case 1:
                                    Intent in = new Intent(getApplicationContext(), ActivityUpdateData.class);
                                    in.putExtra("barcode", selection);
                                    in.putExtra("nama", namaSelection);
                                    startActivity(in);
                                    break;
                                case 2:
                                    final CharSequence[] pertanyaan = {"Yes", "No"};
                                    AlertDialog.Builder question = new AlertDialog.Builder(ActivityDaftarBarang.this);
                                    question.setTitle("Yakin Menghapus ?");
                                    question.setItems(pertanyaan, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case 0:
                                                    SQLiteDatabase tbl_barang = dbcenter.getWritableDatabase();
                                                    tbl_barang.execSQL("DELETE FROM tbl_barang WHERE barcode='" + selection + "'");

                                                    SQLiteDatabase tbl_rule = dbcenter.getWritableDatabase();
                                                    tbl_rule.execSQL("DELETE FROM tbl_rule WHERE barcode='" + selection + "'");
                                                    RefreshList();
                                                    Alerter.create(ActivityDaftarBarang.this)
                                                            .setTitle("Information")
                                                            .setText("Data Berhasil DiHapus !")
                                                            .setBackgroundColor(R.color.colorHintFalse)
                                                            .show();
                                                    ActivityBarang.ab.refreshCounter();
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
            ((ArrayAdapter) listBarang.getAdapter()).notifyDataSetInvalidated();
        } else {
            Alerter.create(ActivityDaftarBarang.this)
                    .setTitle("Information")
                    .setText("Data Tidak Ditemukan !")
                    .setBackgroundColor(R.color.colorHintFalse)
                    .show();
        }
    }
}
