package com.ournity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //Variables
    private Button btnLogin;
    private EditText iptEmailLogin;
    private EditText iptPasswordLogin;
    private TextView txtDaftar;
    private ProgressDialog loadingDialog;
    RelativeLayout mRelative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Menghilangkan Toolbar title
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        Memanggil Layout
        setContentView(R.layout.activity_login);

//        Deklarasi Variable
        loadingDialog = new ProgressDialog(this);
        iptEmailLogin = (EditText) findViewById(R.id.iptEmailLogin);
        iptPasswordLogin = (EditText) findViewById(R.id.iptPasswordLogin);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtDaftar = (TextView) findViewById(R.id.txtDaftar);
        mRelative = (RelativeLayout) findViewById(R.id.LoginRelative);

//        Text Link Lupa Sandi
        TextView txtLupa = (TextView) findViewById(R.id.txtLupa);
        txtLupa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ResetActivity.class));
                finish();
            }
        });


//        Fungsi widget yang bisa diklik
        btnLogin.setOnClickListener(this);
        txtDaftar.setOnClickListener(this);

    }

//    Input User Login
    private void userLogin(){
        String email = iptEmailLogin.getText().toString().trim();
        String password = iptPasswordLogin.getText().toString().trim();

//        Toast
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Email dan Password Tidak Cocok!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Email dan Password Tidak Cocok!", Toast.LENGTH_SHORT).show();
            return;
        }

//        Loading Dialog
        loadingDialog.setMessage("Harap Tunggu...");
        loadingDialog.setTitle("User Login");
        loadingDialog.show();
    }

//    Alert Dialog Exit
    public void onBackPressed() {
        exit();
    }
    private void exit(){
        Builder exitBulider = new Builder(this);
        exitBulider.setCancelable(false);
        exitBulider.setIcon(R.drawable.ic_exit);
        exitBulider.setTitle("Konfirmasi");
        exitBulider.setMessage("Yakin ingin keluar dari aplikasi?");
        exitBulider.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LoginActivity.this.finish();
            }
        });
        exitBulider.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();
    }

//    Tombol Login
    @Override
    public void onClick(View view) {
        if(view == btnLogin){
            userLogin();
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
        if(view == txtDaftar){
            finish();
            startActivity(new Intent(this, RegisActivity.class));
        }
    }

}


