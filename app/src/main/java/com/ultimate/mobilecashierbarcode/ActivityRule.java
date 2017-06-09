package com.ultimate.mobilecashierbarcode;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;

import com.tapadoo.alerter.Alerter;

public class ActivityRule extends AppCompatActivity {
    private String[] idBarang, barcode, jumlah, text,harga;
    private String idBarcode, nama,selectionHarga,selectionJumlah;
    ListView listRule;
    Menu menu;
    protected Cursor cursor;
    DataHelper dbcenter;
    Context context;
    final Context contextDialog = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule);
        nama = getIntent().getStringExtra("nama");
        idBarcode = getIntent().getStringExtra("barcode");
        dbcenter = new DataHelper(this);
        final TextView title = (TextView) findViewById(R.id.textView5);
        title.setText(String.valueOf(nama));
        Button addRule = (Button) findViewById(R.id.btnAddRuleHarga);
        addRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(contextDialog);
                final View promptsView = li.inflate(R.layout.custom_dialog_addcode, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(contextDialog);
                alertDialogBuilder.setTitle("Tambah Rule Harga");
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
                final Button plus = (Button) promptsView.findViewById(R.id.tambahJumlah);
                final Button minus = (Button) promptsView.findViewById(R.id.kurangJumlah);
                final TextView jumlah = (TextView) promptsView.findViewById(R.id.jumlahRule);
                plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int val = Integer.parseInt(jumlah.getText().toString());
                        val = val + 1;
                        jumlah.setText(String.valueOf(val));
                    }
                });
                minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int val = Integer.parseInt(jumlah.getText().toString());
                        if (val > 1) {
                            val = val - 1;
                            jumlah.setText(String.valueOf(val));
                        }
                    }
                });
                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton("Simpan",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        SQLiteDatabase tambahRule = dbcenter.getWritableDatabase();
                                        tambahRule.execSQL("insert into tbl_rule(barcode,jumlah,harga) values('" + idBarcode + "','" + jumlah.getText().toString() + "','" + userInput.getText().toString() + "')");
                                        Alerter.create(ActivityRule.this)
                                                .setTitle("Information")
                                                .setText("Tambah Rule Berhasil")
                                                .setBackgroundColor(R.color.colorHint)
                                                .show();
                                        RefreshRule();
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
        });
        RefreshRule();
    }

    private void RefreshRule() {
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM tbl_rule WHERE barcode='" + idBarcode + "';", null);

        idBarang = new String[cursor.getCount()];
        barcode = new String[cursor.getCount()];
        jumlah = new String[cursor.getCount()];
        harga = new String[cursor.getCount()];
        text = new String[cursor.getCount()];
        cursor.moveToFirst();


        for (int cc = 0; cc < cursor.getCount(); cc++) {
            cursor.moveToPosition(cc);
            idBarang[cc] = cursor.getString(0).toString();
            barcode[cc] = cursor.getString(1).toString();
            jumlah[cc] = cursor.getString(2).toString();
            harga[cc] = cursor.getString(3).toString();
            text[cc] = "Jumlah : " + cursor.getString(2).toString() + "\n" + "Harga : " + cursor.getString(3).toString();
        }

        listRule = (ListView) findViewById(R.id.listRuleBarang);
        listRule.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, text));
        listRule.setSelected(true);
        listRule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                final String selection = idBarang[position];
                final String selectionharga = harga[position];
                final String selectionJumlah = jumlah[position];
                final CharSequence[] dialogitem = {"Update Rule", "Hapus Rule"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityRule.this);
                builder.setTitle("Pilihan");
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:

                                LayoutInflater li = LayoutInflater.from(contextDialog);
                                final View promptsView = li.inflate(R.layout.custom_dialog_updatebarang, null);

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(contextDialog);
                                alertDialogBuilder.setTitle("Update Rule Harga");
                                alertDialogBuilder.setView(promptsView);

                                final EditText userInput = (EditText) promptsView.findViewById(R.id.updateHargaRule);
                                final Button plus = (Button) promptsView.findViewById(R.id.tambahUpdateRule);
                                final Button minus = (Button) promptsView.findViewById(R.id.kurangUpdateRule);
                                final TextView jumlah = (TextView) promptsView.findViewById(R.id.jumlahUpdateRule);

                                userInput.setText(String.valueOf(selectionharga));
                                jumlah.setText(String.valueOf(selectionJumlah));

                                plus.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        int val = Integer.parseInt(jumlah.getText().toString());
                                        val = val + 1;
                                        jumlah.setText(String.valueOf(val));
                                    }
                                });
                                minus.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        int val = Integer.parseInt(jumlah.getText().toString());
                                        if (val > 1) {
                                            val = val - 1;
                                            jumlah.setText(String.valueOf(val));
                                        }
                                    }
                                });
                                alertDialogBuilder.setCancelable(false)
                                        .setPositiveButton("Update",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        String dataIdRule = selection;
                                                        String dataHargaInput = userInput.getText().toString();
                                                        String dataJumlahInput = jumlah.getText().toString();
                                                        SQLiteDatabase updateRule = dbcenter.getWritableDatabase();
                                                        updateRule.execSQL("UPDATE tbl_rule SET jumlah='"+dataJumlahInput+"', harga='"+dataHargaInput+"' WHERE id='"+dataIdRule+"';");
                                                        Alerter.create(ActivityRule.this)
                                                                .setTitle("Information")
                                                                .setText("Update Rule Berhasil")
                                                                .setBackgroundColor(R.color.colorHint)
                                                                .show();
                                                        RefreshRule();
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

                                break;
                            case 1:
                                final CharSequence[] pertanyaan = {"Yes", "No"};
                                AlertDialog.Builder question = new AlertDialog.Builder(ActivityRule.this);
                                question.setTitle("Yakin Menghapus ?");
                                question.setItems(pertanyaan, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case 0:
                                                SQLiteDatabase tbl_barang = dbcenter.getWritableDatabase();
                                                tbl_barang.execSQL("DELETE FROM tbl_rule WHERE id='" + selection + "'");
                                                Alerter.create(ActivityRule.this)
                                                    .setTitle("Information")
                                                    .setText("Delete Rule Berhasil")
                                                    .setBackgroundColor(R.color.colorHintFalse)
                                                    .show();
                                                RefreshRule();
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
        ((ArrayAdapter) listRule.getAdapter()).notifyDataSetInvalidated();
    }
}
