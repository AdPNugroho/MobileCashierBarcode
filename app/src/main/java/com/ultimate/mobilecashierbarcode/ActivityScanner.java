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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tapadoo.alerter.Alerter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static android.util.Log.d;

public class ActivityScanner extends AppCompatActivity {
    private String[] dataBarcode,dataNama,dataHarga,dataJumlah,textShow;
    private Button scan, restart;
    private ListView listTransaksi;
    private EditText inputBarcodeTransaksi, inputBarangTransaksi,updateBarcodeTransaksi,updateBarangTransaksi;
    private TextView totalPembayaran;
    private ArrayList<Barang> barang;
    final Context contextDialog = this;
    protected Cursor cursor;
    DataHelper dbcenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        barang = new ArrayList<>();
        dbcenter = new DataHelper(this);
        scan = (Button) findViewById(R.id.ScanTransaksi);
        restart = (Button) findViewById(R.id.ResetTransaksi);
        totalPembayaran = (TextView) findViewById(R.id.totalPembayaran);
        listTransaksi = (ListView) findViewById(R.id.ListTransaksi);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(contextDialog);
                final View promptsView = li.inflate(R.layout.custom_dialog_scantransaksi, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(contextDialog);
                alertDialogBuilder.setView(promptsView);

                inputBarcodeTransaksi = (EditText) promptsView.findViewById(R.id.scanTransaksiBarcode);
                inputBarangTransaksi = (EditText) promptsView.findViewById(R.id.barangScanTransaksi);
                final TextView inputJumlahTransaksi = (TextView) promptsView.findViewById(R.id.jumlahBarangTransaksi);
                final Button btnTambahJumlah = (Button) promptsView.findViewById(R.id.tambahJumlahTransaksi);
                final Button btnKurangJumlah = (Button) promptsView.findViewById(R.id.kurangJumlahTransaksi);
                final Button btnScanner = (Button) promptsView.findViewById(R.id.scannerActive);

                btnTambahJumlah.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int jumlah = Integer.parseInt(inputJumlahTransaksi.getText().toString());
                        jumlah = jumlah + 1;
                        inputJumlahTransaksi.setText(String.valueOf(jumlah));
                    }
                });
                btnKurangJumlah.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int jumlah = Integer.parseInt(inputJumlahTransaksi.getText().toString());
                        if (jumlah > 1) {
                            jumlah = jumlah - 1;
                            inputJumlahTransaksi.setText(String.valueOf(jumlah));
                        }
                    }
                });
                btnScanner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IntentIntegrator integrator = new IntentIntegrator(ActivityScanner.this);
                        integrator.setOrientationLocked(false);
                        integrator.setCaptureActivity(CustomScanner.class);
                        integrator.initiateScan();
                    }
                });
                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton("Simpan",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                })
                        .setNegativeButton("Tutup",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Boolean wantToCloseDialog = false;

                        String barcode = inputBarcodeTransaksi.getText().toString();
                        String nama = inputBarangTransaksi.getText().toString();
                        SQLiteDatabase cariData = dbcenter.getReadableDatabase();
                        cursor = cariData.rawQuery("SELECT * FROM tbl_rule WHERE barcode='" + barcode + "' ORDER BY jumlah DESC;", null);
                        if (cursor.getCount() > 0) {
                            int barangBeli = Integer.parseInt(inputJumlahTransaksi.getText().toString());
                            int tempBeli = barangBeli;
                            cursor.moveToFirst();
                            int total = 0;
                            for (int cc = 0; cc < cursor.getCount(); cc++) {
                                cursor.moveToPosition(cc);
                                int harga = Integer.parseInt(cursor.getString(3).toString());
                                int jumlah = Integer.parseInt(cursor.getString(2).toString());
                                if (barangBeli >= jumlah) {
                                    int sisa = barangBeli % jumlah;
                                    barangBeli = barangBeli - sisa;
                                    total = total + ((barangBeli / jumlah) * harga);
                                    barangBeli = sisa;
                                }
                            }
                            inputBarangTransaksi.setText("");
                            inputBarcodeTransaksi.setText("");
                            inputJumlahTransaksi.setText("1");
                            barang.add(new Barang(barcode, nama, total, tempBeli));
                            RefreshListTransaksi();
                        } else {
                            Alerter.create(ActivityScanner.this)
                                    .setTitle("Error")
                                    .setText("Rule Barang Tidak Ditemukan\nSilahkan ke menu Barang terlebih dahulu!")
                                    .setBackgroundColor(R.color.colorHintFalse)
                                    .show();
                        }


                        if(wantToCloseDialog)
                            alertDialog.dismiss();
                        //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
                    }
                });
            }
        });
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barang = new ArrayList<>();
                //TODO SAVE ARRAY TRANSAKSI INTO DB
                RefreshListTransaksi();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            String barcode = result.getContents();
            if (barcode != null) {
                inputBarcodeTransaksi.setText(String.valueOf(barcode));
                SQLiteDatabase cariData = dbcenter.getReadableDatabase();
                cursor = cariData.rawQuery("SELECT * FROM tbl_barang WHERE barcode='" + barcode + "' LIMIT 1;", null);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    inputBarangTransaksi.setText(String.valueOf(cursor.getString(2)));
                    inputBarangTransaksi.setTextColor(getResources().getColor(R.color.colorGrey));
                } else {
                    Alerter.create(ActivityScanner.this)
                            .setTitle("Error")
                            .setText("Barang Tidak Ditemukan")
                            .setBackgroundColor(R.color.colorHintFalse)
                            .show();
                    inputBarangTransaksi.setText("Barang Tidak Ditemukan!");
                    inputBarangTransaksi.setTextColor(getResources().getColor(R.color.colorHintFalse));
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void RefreshListTransaksi() {
        int ukuranArray = barang.size();
        if (ukuranArray > 0) {
            dataBarcode = new String[ukuranArray];
            dataNama = new String[ukuranArray];
            dataHarga = new String[ukuranArray];
            dataJumlah = new String[ukuranArray];
            textShow = new String[ukuranArray];

            int total = 0;
            int x = 0;
            for (Barang item : barang) {
                total = total + item.getHarga();
                dataBarcode[x] = item.getKode();
                dataNama[x] = item.getNama();
                dataHarga[x] = String.valueOf(item.getHarga());
                dataJumlah[x] = String.valueOf(item.getJumlah());
                textShow[x] = "Kode : " + item.getKode() + "\n" + item.getNama() + "\nJumlah Beli : " + item.getJumlah() + "\nHarga : " + item.getHarga();
                x++;
            }
            listTransaksi.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, textShow));
            listTransaksi.setSelected(true);
            listTransaksi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View view, int position, long id) {
                    final String selection = dataBarcode[position];
                    final String selectionnama = dataNama[position];
                    final String selectionharga = dataHarga[position];
                    final String selectionJumlah = dataJumlah[position];
                    final CharSequence[] dialogitem = {"Edit", "Hapus"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityScanner.this);
                    builder.setTitle("Pilihan");
                    builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case 0:
                                    LayoutInflater li = LayoutInflater.from(contextDialog);
                                    final View promptsView = li.inflate(R.layout.custom_dialog_updatetransaksi, null);

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(contextDialog);
                                    alertDialogBuilder.setView(promptsView);

                                    updateBarcodeTransaksi = (EditText) promptsView.findViewById(R.id.updateTransaksiBarcode);
                                    updateBarangTransaksi = (EditText) promptsView.findViewById(R.id.updateTransaksiBarang);
                                    final TextView inputJumlahTransaksi = (TextView) promptsView.findViewById(R.id.jumlahUpdateTransaksi);
                                    final Button btnTambahJumlah = (Button) promptsView.findViewById(R.id.tambahUpdateTransaksi);
                                    final Button btnKurangJumlah = (Button) promptsView.findViewById(R.id.kurangUpdateTransaksi);

                                    updateBarcodeTransaksi.setText(selection);
                                    updateBarangTransaksi.setText(selectionnama);
                                    inputJumlahTransaksi.setText(selectionJumlah);

                                    btnTambahJumlah.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            int jumlah = Integer.parseInt(inputJumlahTransaksi.getText().toString());
                                            jumlah = jumlah + 1;
                                            inputJumlahTransaksi.setText(String.valueOf(jumlah));
                                        }
                                    });
                                    btnKurangJumlah.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            int jumlah = Integer.parseInt(inputJumlahTransaksi.getText().toString());
                                            if (jumlah > 1) {
                                                jumlah = jumlah - 1;
                                                inputJumlahTransaksi.setText(String.valueOf(jumlah));
                                            }
                                        }
                                    });
                                    alertDialogBuilder.setCancelable(false)
                                            .setPositiveButton("Update",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            //TODO UPDATE LIST
                                                            String barcode = updateBarcodeTransaksi.getText().toString();
                                                            String nama = updateBarangTransaksi.getText().toString();
                                                            SQLiteDatabase cariData = dbcenter.getReadableDatabase();
                                                            cursor = cariData.rawQuery("SELECT * FROM tbl_rule WHERE barcode='" + barcode + "' ORDER BY jumlah DESC;", null);
                                                            if (cursor.getCount() > 0) {
                                                                int barangBeli = Integer.parseInt(inputJumlahTransaksi.getText().toString());
                                                                int tempBeli = barangBeli;
                                                                cursor.moveToFirst();
                                                                int total = 0;
                                                                for (int cc = 0; cc < cursor.getCount(); cc++) {
                                                                    cursor.moveToPosition(cc);
                                                                    int harga = Integer.parseInt(cursor.getString(3).toString());
                                                                    int jumlah = Integer.parseInt(cursor.getString(2).toString());
                                                                    if (barangBeli >= jumlah) {
                                                                        int sisa = barangBeli % jumlah;
                                                                        barangBeli = barangBeli - sisa;
                                                                        total = total + ((barangBeli / jumlah) * harga);
                                                                        barangBeli = sisa;
                                                                    }
                                                                }
                                                                inputBarangTransaksi.setText("");
                                                                inputBarcodeTransaksi.setText("");
                                                                inputJumlahTransaksi.setText("1");
                                                                for(Barang item:barang){
                                                                    if(item.getKode().equals(barcode)){
                                                                        item.setJumlah(tempBeli);
                                                                        item.setHarga(total);
                                                                    }
                                                                }
                                                                RefreshListTransaksi();
                                                            } else {
                                                                Alerter.create(ActivityScanner.this)
                                                                        .setTitle("Error")
                                                                        .setText("Rule Barang Tidak Ditemukan\nSilahkan ke menu Barang terlebih dahulu!")
                                                                        .setBackgroundColor(R.color.colorHintFalse)
                                                                        .show();
                                                            }
                                                        }
                                                    })
                                            .setNegativeButton("Tutup",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                        }
                                                    });
                                    final AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();

                                    break;
                                case 1:
                                    Alerter.create(ActivityScanner.this)
                                            .setTitle("Error")
                                            .setText("DELETE! " + selection)
                                            .setBackgroundColor(R.color.colorHintFalse)
                                            .show();
                                    for(int x = 0;x<barang.size();x++){
                                        if(barang.get(x).getKode().equals(selection)){
                                            barang.remove(x);
                                        }
                                    }
                                    RefreshListTransaksi();
                                    break;
                            }
                        }
                    });
                    builder.create().show();
                }
            });
            ((ArrayAdapter) listTransaksi.getAdapter()).notifyDataSetInvalidated();


            DecimalFormat formatter = new DecimalFormat("#,###.00");
            String hasilTotal = formatter.format(total);
            hasilTotal.replace(",", ".");
            totalPembayaran.setText("Rp. " + hasilTotal + ",-");

            for (Barang item : barang) {
                d("data", "Kode : " + item.getKode() + "Nama : " + item.getNama() + " Harga : " + item.getHarga() + " Jumlah :" + item.getJumlah());
            }
        } else {
            totalPembayaran.setText("Rp. 0 ,-");
            listTransaksi.setAdapter(null);
        }
    }
}
