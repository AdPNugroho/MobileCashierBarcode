package com.ournity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ResetActivity extends AppCompatActivity {

//    Variables
    private EditText iptEmailReset;
    private Button btnReset;
    private ProgressDialog loadingReset;

//    Dapetin tinggi statusbar
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Memanggil layout
        setContentView(R.layout.activity_reset);

//        Deklarasi variables
        iptEmailReset = (EditText) findViewById(R.id.iptReset);
        btnReset = (Button) findViewById(R.id.btnReset);
        loadingReset = new ProgressDialog(this);


//        Toolbar
        Toolbar ToolBarAtas2 = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(ToolBarAtas2);
        ToolBarAtas2.setNavigationIcon(R.drawable.ic_previous);

//        Tombol back di toolbar
        ToolBarAtas2.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

//         Set the padding to match the Status Bar height
        ToolBarAtas2.setPadding(0, getStatusBarHeight(), 0, 0);

//        Tombol reset
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = iptEmailReset.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Masukan Email Anda!", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });
  }

//    Tombol kembali
    public void onBackPressed(){
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

}
