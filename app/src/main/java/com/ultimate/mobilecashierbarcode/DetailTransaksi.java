package com.ultimate.mobilecashierbarcode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DetailTransaksi extends AppCompatActivity {
    private Button plus,min,submit;
    private EditText kode,nama,harga;
    private TextView jumlah,dummy;
    private ArrayList<Barang> tempBarang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaksi);
        tempBarang = new ArrayList<>();
        kode = (EditText) findViewById(R.id.txtKode);
        nama = (EditText) findViewById(R.id.txtNama);
        harga = (EditText) findViewById(R.id.txtHarga);
        jumlah = (TextView) findViewById(R.id.jumlah);
        dummy = (TextView) findViewById(R.id.dummyText);

        plus = (Button) findViewById(R.id.btnPlus);
        min = (Button) findViewById(R.id.btnMin);
        submit = (Button) findViewById(R.id.btnSubmit);

        String kd = getIntent().getStringExtra("kode").toString();
        String listBarang = getIntent().getStringExtra("barang");
        Gson gson = new Gson();
        Type type = new TypeToken<List<Barang>>(){}.getType();
        final List<Barang> dataBarang = gson.fromJson(listBarang,type);
        int s = dataBarang.size();
        kode.setText(kd);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dtKode = kode.getText().toString();
                String dtNama = nama.getText().toString();
                int dtHarga = Integer.parseInt(harga.getText().toString());
                int dtJumlah = Integer.parseInt(jumlah.getText().toString());
                dataBarang.add(new Barang(dtKode,dtNama,dtHarga,dtJumlah));
                Gson gsonBarang = new Gson();
                String jsonBarang = gsonBarang.toJson(dataBarang);
                Intent i = new Intent(getApplicationContext(),ActivityScanner.class);
                i.putExtra("jsonBarang",jsonBarang);
                startActivity(i);
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int j = Integer.parseInt(jumlah.getText().toString());
                j = j +1;
                jumlah.setText(String.valueOf(j));
            }
        });
        min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int j = Integer.parseInt(jumlah.getText().toString());
                if(j>0){
                    j = j - 1;
                    jumlah.setText(String.valueOf(j));
                }
            }
        });
    }
}
