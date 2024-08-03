package com.tester.iotss.utils.sessions;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

public class SessionLogin {

    String id;
    String nama;
    String nohp;
    String password;
    String id_alat;
    Uri urialarm;


    public Uri getUrialarm(Context context) {
        SharedPreferences sharedPreferences2=context.getSharedPreferences("urialarm", Context.MODE_PRIVATE);
        urialarm=Uri.parse(sharedPreferences2.getString("urialarm",""));
        return urialarm;
    }

    public void setUrialarm(Uri urialarm,Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("urialarm", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("urialarm", urialarm.toString());
        editor.apply();
        this.urialarm = urialarm;
    }

    public String getId_alat(Context context) {
        SharedPreferences sharedPreferences2=context.getSharedPreferences("id_alat", Context.MODE_PRIVATE);
        id_alat=sharedPreferences2.getString("id_alat","");
        return id_alat;
    }

    public void setId_alat(String id_alat,Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("id_alat", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id_alat", id_alat);
        editor.commit();
        this.id_alat = id_alat;
    }

    public String getPassword(Context context) {
        SharedPreferences sharedPreferences2=context.getSharedPreferences("password", Context.MODE_PRIVATE);
        password=sharedPreferences2.getString("password","");
        return password;
    }

    public void setPassword(String password,Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("password", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("password", password);
        editor.commit();
        this.password = password;
    }

    public String getId(Context context) {
        SharedPreferences sharedPreferences2=context.getSharedPreferences("id_users", Context.MODE_PRIVATE);
        id=sharedPreferences2.getString("id_users","");
        return id;
    }

    public void setId(String id,Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("id_users", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id_users", id);
        editor.commit();
        this.id = id;
    }

    public String getNama(Context context) {
        SharedPreferences sharedPreferences2=context.getSharedPreferences("nama", Context.MODE_PRIVATE);
        nama=sharedPreferences2.getString("nama","");
        return nama;
    }

    public void setNama(String nama,Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("nama", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nama", nama);
        editor.commit();
        this.nama = nama;
    }

    public String getNohp(Context context) {
        SharedPreferences sharedPreferences2=context.getSharedPreferences("nohp", Context.MODE_PRIVATE);
        nohp=sharedPreferences2.getString("nohp","");
        return nohp;
    }

    public void setNoHp(String nohp,Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("nohp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nohp", nohp);
        editor.commit();
        this.nohp = nohp;
    }


    public void logout(Context context){
        setId("",context);
        setNama("",context);
        setNoHp("",context);
        setPassword("",context);
    }
}
