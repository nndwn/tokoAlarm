package com.tester.iotss.ui.activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tester.iotss.R;
import com.tester.iotss.domain.model.RingtoneList;
import com.tester.iotss.ui.adapter.RingtoneAdapter;
import com.tester.iotss.utils.sessions.SessionLogin;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Setting extends AppCompatActivity implements RingtoneAdapter.OnItemClickListener {

    private ArrayList<RingtoneList> listRiwayat;
    private RingtoneAdapter adapter;
    @BindView(R.id.list_ringtone)
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecyclerView.Adapter recyclerViewadapter;
    Uri ringtoneUri2;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        listRiwayat = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(Setting.this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);


        RingtoneManager ringtoneManager = new RingtoneManager(this);
        ringtoneManager.setType(RingtoneManager.TYPE_ALARM);
        Cursor cursor = ringtoneManager.getCursor();
        if (cursor != null) {
            try {
                listRiwayat.clear();
                while (cursor.moveToNext()) {
                    String ringtoneTitle = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
                    Uri ringtoneUri = ringtoneManager.getRingtoneUri(cursor.getPosition());

                    RingtoneList memberData = new RingtoneList();
                    memberData.setTitle(ringtoneTitle);
                    memberData.setUri(ringtoneUri);
                    listRiwayat.add(memberData);
                }
            } finally {
                cursor.close();
            }
        }


        addLocalRingtones();
        recyclerViewadapter = new RingtoneAdapter(Setting.this, listRiwayat, Setting.this, this);
        recyclerView.setAdapter(recyclerViewadapter);
    }

    private void addLocalRingtones() {
        addLocalRingtone("Suara Satu", R.raw.suara_satu);
        addLocalRingtone("Suara Dua", R.raw.suara_dua);
        addLocalRingtone("Suara Tiga", R.raw.suara_tiga);
        addLocalRingtone("Suara Empat", R.raw.suara_empat);
    }

    private void addLocalRingtone(String title, int resId) {
        RingtoneList localRingtone = new RingtoneList();
        localRingtone.setTitle(title);
        localRingtone.setUriFromResource(this,resId);
        listRiwayat.add(localRingtone);
    }

    @Override
    public void onItemClick(int position) {
        Uri ringtoneUri = listRiwayat.get(position).getUri();
        Log.d("RingtoneSelected", "Selected Ringtone URI: " + ringtoneUri.toString());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Suara Notifikasi");
        builder.setMessage("Ubah Suara Notifikasi ?");
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build());

        try {
            mediaPlayer.setDataSource(getApplicationContext(), ringtoneUri);
            mediaPlayer.setOnPreparedListener(MediaPlayer::start);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        builder.setPositiveButton("Ok" , (dialog, which) -> {
            SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("RingtoneUri", ringtoneUri.toString());
            editor.apply();
            Log.d("SharedPreferences", "Stored Ringtone URI: " + ringtoneUri);
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @OnClick(R.id.backButton) void backButton(){
        onBackPressed();
    }
}