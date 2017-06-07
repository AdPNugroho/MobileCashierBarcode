package com.ultimate.mobilecashierbarcode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ActivityRule extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule);
        TextView dumm = (TextView) findViewById(R.id.textView5);
        String barcode = getIntent().getStringExtra("barcode");
        dumm.setText(String.valueOf(barcode));

    }
}
