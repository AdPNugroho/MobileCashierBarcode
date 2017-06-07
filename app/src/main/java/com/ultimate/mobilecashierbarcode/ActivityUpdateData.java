package com.ultimate.mobilecashierbarcode;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ActivityUpdateData extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);
        TextView dumm = (TextView) findViewById(R.id.textView8);
        String barcode = getIntent().getStringExtra("barcode");
        dumm.setText(String.valueOf(barcode));
    }
}
