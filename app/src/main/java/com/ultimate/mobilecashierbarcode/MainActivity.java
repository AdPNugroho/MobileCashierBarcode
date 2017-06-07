package com.ultimate.mobilecashierbarcode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button transaksi;
    private Button barang;
    private Button datatransaksi;
    private Button exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        transaksi = (Button) findViewById(R.id.btnTransaksi);
        barang = (Button) findViewById(R.id.btnBarang);
        datatransaksi = (Button) findViewById(R.id.btnData);
        exit = (Button) findViewById(R.id.btnExit);
        transaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ActivityScanner.class);
                startActivity(i);
            }
        });
        barang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ActivityBarang.class);
                startActivity(i);
            }
        });
        datatransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ActivityTransaksi.class);
                startActivity(i);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                finish();
            }
        });
    }
}
