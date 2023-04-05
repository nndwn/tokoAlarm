package com.tester.svquickcount.Session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionLogin {

    String id_pelanggan;
    String nama_pelanggan;
    String email_pelanggan;
    String nohp_pelanggan;
    String foto_pelanggan;


    public String getFoto_pelanggan(Context context) {
        SharedPreferences sharedPreferences2=context.getSharedPreferences("foto_pelanggan", Context.MODE_PRIVATE);
        foto_pelanggan=sharedPreferences2.getString("foto_pelanggan","");
        return foto_pelanggan;
    }

    public void setFoto_pelanggan(String foto_pelanggan,Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("foto_pelanggan", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("foto_pelanggan", foto_pelanggan);
        editor.commit();
        this.foto_pelanggan = foto_pelanggan;
    }

    public String getId_pelanggan(Context context) {
        SharedPreferences sharedPreferences2=context.getSharedPreferences("id_pelanggan", Context.MODE_PRIVATE);
        id_pelanggan=sharedPreferences2.getString("id_pelanggan","");
        return id_pelanggan;
    }

    public void setId_pelanggan(String id_pelanggan,Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("id_pelanggan", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id_pelanggan", id_pelanggan);
        editor.commit();
        this.id_pelanggan = id_pelanggan;
    }

    public String getNama_pelanggan(Context context) {
        SharedPreferences sharedPreferences2=context.getSharedPreferences("nama_pelanggan", Context.MODE_PRIVATE);
        nama_pelanggan=sharedPreferences2.getString("nama_pelanggan","");
        return nama_pelanggan;
    }

    public void setNama_pelanggan(String nama_pelanggan,Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("nama_pelanggan", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nama_pelanggan", nama_pelanggan);
        editor.commit();
        this.nama_pelanggan = nama_pelanggan;
    }

    public String getEmail_pelanggan(Context context) {
        SharedPreferences sharedPreferences2=context.getSharedPreferences("email_pelanggan", Context.MODE_PRIVATE);
        email_pelanggan=sharedPreferences2.getString("email_pelanggan","");
        return email_pelanggan;
    }

    public void setEmail_pelanggan(String email_pelanggan,Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("email_pelanggan", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email_pelanggan", email_pelanggan);
        editor.commit();
        this.email_pelanggan = email_pelanggan;
    }

    public String getNohp_pelanggan(Context context) {
        SharedPreferences sharedPreferences2=context.getSharedPreferences("nohp_pelanggan", Context.MODE_PRIVATE);
        nohp_pelanggan=sharedPreferences2.getString("nohp_pelanggan","");
        return nohp_pelanggan;
    }

    public void setNohp_pelanggan(String nohp_pelanggan,Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("nohp_pelanggan", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nohp_pelanggan", nohp_pelanggan);
        editor.commit();
        this.nohp_pelanggan = nohp_pelanggan;
    }


    public void logout(Context context){
        setId_pelanggan("",context);
        setNama_pelanggan("",context);
        setEmail_pelanggan("",context);
        setNohp_pelanggan("",context);
        setFoto_pelanggan("",context);
    }
}
