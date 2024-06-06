package com.tester.iotss;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.tester.iotss.Model.RingtoneList;
import com.tester.iotss.Session.SessionLogin;
import com.tester.iotss.Adapters.RingtoneAdapter;

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
        listRiwayat.clear();
        while (cursor.moveToNext()) {
            RingtoneList memberData = new RingtoneList();
            String ringtoneTitle = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            Uri ringtoneUri = ringtoneManager.getRingtoneUri(cursor.getPosition());
            Log.d("Ringtone", "Judul: " + ringtoneTitle + ", URI: " + ringtoneUri.toString());
            RingtoneList model = new RingtoneList();
            memberData.setTitle(ringtoneTitle);
            memberData.setUri(ringtoneUri);
            listRiwayat.add(memberData);
            recyclerViewadapter = new RingtoneAdapter(Setting.this,listRiwayat,Setting.this,this);
            recyclerView.setAdapter(recyclerViewadapter);
        }
        cursor.close();
    }

    @SuppressLint("NewApi")
    @Override
    public void onItemClick(int position) {
        SessionLogin sessionLogin = new SessionLogin();
        sessionLogin.setUrialarm(listRiwayat.get(position).getUri(),getApplicationContext());
        if(mediaPlayer!=null){
            ringtoneUri2 = listRiwayat.get(position).getUri();
            mediaPlayer.stop();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build());

            try {
                mediaPlayer.setDataSource(getApplicationContext(), ringtoneUri2);
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }
                });
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            ringtoneUri2 = listRiwayat.get(position).getUri();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build());

            try {
                mediaPlayer.setDataSource(getApplicationContext(), ringtoneUri2);
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }
                });
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer!=null){
            mediaPlayer.stop();
        }
    }

    @OnClick(R.id.backButton) void backButton(){
        onBackPressed();
    }
}