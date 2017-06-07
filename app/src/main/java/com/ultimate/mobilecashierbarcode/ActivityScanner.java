package com.ultimate.mobilecashierbarcode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ActivityScanner extends AppCompatActivity {
    private Button scan,restart;
    private TextView dataArray;
    private ArrayList<Barang> barang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        barang = new ArrayList<>();

        scan = (Button) findViewById(R.id.scan);
        restart = (Button) findViewById(R.id.restart);
        dataArray = (TextView) findViewById(R.id.arrayBarang);
        dataArray.setText("");

        String jsonBarang = getIntent().getStringExtra("jsonBarang");
        if(jsonBarang != null){
            Gson gsonTransaksi = new Gson();
            Type type = new TypeToken<ArrayList<Barang>>(){}.getType();
            barang = gsonTransaksi.fromJson(jsonBarang,type);
        }

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(ActivityScanner.this);
                integrator.setOrientationLocked(false);
                integrator.setCaptureActivity(CustomScanner.class);
                integrator.initiateScan();
            }
        });
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        for(Barang item:barang){
            dataArray.append(item.getKode() + " - " + item.getNama() + " - " + item.getHarga() + " - " + item.getJumlah() + "\n");
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Gson gson = new Gson();
                String jsonBarang = gson.toJson(barang);
                String barcode = result.getContents();
                Intent i = new Intent(getApplicationContext(),DetailTransaksi.class);
                i.putExtra("barang",jsonBarang);
                i.putExtra("kode",barcode);
                startActivity(i);
//                TODO CARI KE DATABASE
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
