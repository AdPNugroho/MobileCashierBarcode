package com.ournity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisActivity extends AppCompatActivity implements View.OnClickListener  {

    //Variables
    private Button btnRegister;
    private EditText iptNamaRegis;
    private EditText iptEmailRegis;
    private EditText iptPasswordRegis;
    private EditText iptKomPassword;
    private EditText iptPhone;
    private TextView txtSigin;
    private ProgressDialog loadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Menghilangkan toolbar title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        Memanggil layout
        setContentView(R.layout.activity_regis);


//        Deklarasi Variable
        btnRegister = (Button) findViewById(R.id.btnRegister);
        loadingDialog = new ProgressDialog(this);
        iptNamaRegis = (EditText) findViewById(R.id.iptNamaRegis);
        iptEmailRegis = (EditText) findViewById(R.id.iptEmailRegis);
        iptPasswordRegis = (EditText) findViewById(R.id.iptPasswordRegis);
        iptKomPassword = (EditText) findViewById(R.id.iptKomPassword);
        iptPhone = (EditText) findViewById(R.id.iptPhone);
        txtSigin = (TextView) findViewById(R.id.txtLogin);

//        Fungsi widget yang bisa diklik
        btnRegister.setOnClickListener(this);
        txtSigin.setOnClickListener(this);

    }

//    Input User Register
    private void registerUser(){
        String nama = iptNamaRegis.getText().toString().trim();
        String email = iptEmailRegis.getText().toString().trim();
        String password = iptPasswordRegis.getText().toString().trim();
        String kompass = iptKomPassword.getText().toString().trim();
        String phone = iptPhone.getText().toString().trim();

//        Loading Dialog
        loadingDialog.setMessage("Harap Tunggu...");
        loadingDialog.setTitle("Registrasi");
        loadingDialog.show();

//        Toast
        if(TextUtils.isEmpty(nama)){
            Toast.makeText(this, "Harap Masukan Nama", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Harap Masukan Email", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Harap Masukan Password", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(kompass)) {
            Toast.makeText(this, "Harap Masukan Password", Toast.LENGTH_SHORT).show();
        }else if(!isMatch(password,kompass)){
            loadingDialog.dismiss();
            Toast.makeText(this, "Password tidak cocok",Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Harap Masukan Nomor Handphone", Toast.LENGTH_SHORT).show();
        }
    }

//    Validasi Retype Password
    private boolean isMatch(String password, String kompass){
        return password.equals(kompass);
    }

//    Fungsi Tombol Back
    public void onBackPressed(){
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

//    Tombol Register
    @Override
    public void onClick(View view) {

        if(view == btnRegister){
            registerUser();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        if(view == txtSigin){
            finish();
            startActivity(new Intent(this, LoginActivity.class));

        }
    }

}
