package com.ultimate.mobilecashierbarcode;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder> {
    private String[] id;
    private String[] barcode;
    private String[] nama;
    DataHelper dbHelper;
    public CardViewAdapter(String[] id,String[] barcode,String[] nama){
        this.id = id;
        this.barcode = barcode;
        this.nama = nama;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_barang, null);
        return new ViewHolder(itemLayout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.idBarang.setText(id[position]);
        holder.barcodeBarang.setText(barcode[position]);
        holder.namaBarang.setText(nama[position]);
    }

    @Override
    public int getItemCount() {
        return id.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        Context context;
        public TextView idBarang,barcodeBarang,namaBarang;
        public Button action,hapus;
        DataHelper dbHelper;
        public ViewHolder(final View itemView) {
            super(itemView);
            context = itemView.getContext();
            idBarang = (TextView) itemView.findViewById(R.id.dataID);
            barcodeBarang = (TextView) itemView.findViewById(R.id.dataBarcode);
            namaBarang = (TextView) itemView.findViewById(R.id.dataNama);
            action = (Button) itemView.findViewById(R.id.btnAction);
            hapus = (Button) itemView.findViewById(R.id.btnHapus);
            action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dbHelper = new DataHelper(itemView.getContext());
                    final String barcode = barcodeBarang.getText().toString();
                    final String nama = namaBarang.getText().toString();
                    final CharSequence[] dialogitem = {"Lihat Rule","Update Data","Hapus Data"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                    builder.setTitle(String.valueOf(nama));
                    builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final Intent intent;
                            switch(which){
                                case 0:
                                    intent = new Intent(context,ActivityRule.class);
                                    intent.putExtra("barcode",barcode);
                                    context.startActivity(intent);
                                    break;
                                case 1:
                                    intent = new Intent(context,ActivityUpdateData.class);
                                    intent.putExtra("barcode",barcode);
                                    context.startActivity(intent);
                                    break;
                                case 2:
                                    SQLiteDatabase deleteBarang = dbHelper.getWritableDatabase();
                                    deleteBarang.execSQL("DELETE FROM tbl_barang WHERE barcode='"+barcode+"';");
                                    SQLiteDatabase deleteRule = dbHelper.getWritableDatabase();
                                    deleteRule.execSQL("DELETE FROM tbl_rule WHERE barcode='"+barcode+"';");
                                    break;
                            }
                        }
                    });
                    //Toast.makeText(v.getContext(), idBarang.getText().toString(), Toast.LENGTH_SHORT).show();
                    //TODO ONCLICK DIALOG EDIT DELETE
                    builder.create().show();
                }
            });
        }
    }
}