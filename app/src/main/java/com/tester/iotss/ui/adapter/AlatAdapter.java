package com.tester.iotss.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tester.iotss.R;
import com.tester.iotss.domain.model.AlatList;
import com.tester.iotss.domain.model.Schedule;
import com.tester.iotss.ui.activity.Monitoring;

import java.util.List;

public class AlatAdapter extends RecyclerView.Adapter<AlatAdapter.ViewHolder> {

    private Context context;
    private List<AlatList> list;
    private Activity activity;
    private final List<Schedule> scheduleList;

    PerpanjangClickListener perpanjangClickListener;
    RenamePaketClickListener renamePaketClickListener;
    public AlatAdapter(Context context, List<Schedule> scheduleList, List<AlatList> list, Activity activity, PerpanjangClickListener perpanjangClickListener, RenamePaketClickListener renamePaketClickListener) {
        this.context = context;
        this.list = list;
        this.activity=activity;
        this.scheduleList =  scheduleList;
        this.perpanjangClickListener=perpanjangClickListener;
        this.renamePaketClickListener=renamePaketClickListener;
    }
//    public AlatAdapter(List<Schedule> scheduleList) {
//        this.scheduleList = scheduleList;
//    }


    public interface PerpanjangClickListener {
        void onPerpanjangClick(int position);
    }

    public interface RenamePaketClickListener {
        void onRenamePaketClick(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.alat_list, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final AlatList getDataAdapter1 = list.get(position);
        try {
            holder.tvIdAlat.setText(getDataAdapter1.getIdAlat());
            holder.tvPeriode.setText(getDataAdapter1.getPeriode());
            holder.tvTanggalMulai.setText(getDataAdapter1.getTanggalMulai());
            holder.tvTanggalSelesai.setText(getDataAdapter1.getTanggalSelesai());
            holder.tvSisaHari.setText("Sisa Masa Aktif "+getDataAdapter1.getSisaHari()+" Hari");
            holder.tvNomorPaket.setText("#"+getDataAdapter1.getNomorPaket());
            int tmpNumber = position +1;
            if(getDataAdapter1.getNamaPaket().equals("-") || getDataAdapter1.getNamaPaket().isEmpty()){
                holder.tvNomorPaket.setText("ALAT " +String.valueOf(tmpNumber));
            }else{
                holder.tvNomorPaket.setText(getDataAdapter1.getNamaPaket());
            }

            switch (getDataAdapter1.getStatus()) {
                case "Aktif" -> {
                    holder.tvStatus.setEnabled(true);
                    holder.tvStatus.setText(getDataAdapter1.getStatus());
                    holder.tvSisaHari.setVisibility(View.VISIBLE);
                    holder.btnLiveMonitoring.setVisibility(View.VISIBLE);
                    holder.btnPerpanjang.setVisibility(View.VISIBLE);
                }
                case "Non Aktif" -> {
                    holder.tvStatus.setEnabled(false);
                    holder.tvStatus.setHovered(false);
                    holder.tvStatus.setText("Non Aktif");
                    holder.tvSisaHari.setVisibility(View.VISIBLE);
                    holder.btnLiveMonitoring.setVisibility(View.VISIBLE);
                    holder.btnPerpanjang.setVisibility(View.VISIBLE);
                }
                case "Pending" -> {
                    holder.tvStatus.setEnabled(false);
                    holder.tvStatus.setHovered(true);
                    holder.tvStatus.setText("Pending");
                    holder.tvSisaHari.setVisibility(View.GONE);
                    holder.btnLiveMonitoring.setVisibility(View.GONE);
                    holder.btnPerpanjang.setVisibility(View.GONE);
                }
            }
        }catch (Exception ignored){}


        holder.btnLiveMonitoring.setOnClickListener(view -> {
            Intent intent = new Intent(activity, Monitoring.class);
            intent.putExtra("id_alat",getDataAdapter1.getIdAlat());
            intent.putExtra("nomor_paket",getDataAdapter1.getNomorPaket());
            if (scheduleList != null)
            {
                for (int i = 0 ; i < scheduleList.size(); i++ )
                {
                    Schedule schedule = scheduleList.get(i);
                    if (scheduleList.get(i).getId_alat().equals(getDataAdapter1.getIdAlat()))
                    {
                        intent.putExtra("schedule", schedule);
                    }
                }
            }

            activity.startActivity(intent);

        });

        holder.btnPerpanjang.setOnClickListener(view ->
                perpanjangClickListener.onPerpanjangClick(holder.getAdapterPosition()));
        
        holder.ivEditNamaPaketButton.setOnClickListener(v ->
                renamePaketClickListener.onRenamePaketClick(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void swapData(List<AlatList> newData) {
        list.clear();
        list.addAll(newData);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvIdAlat, tvStatus, tvTanggalMulai,tvTanggalSelesai, tvPeriode,tvSisaHari,tvNomorPaket;
        public Button btnLiveMonitoring,btnPerpanjang;
        public ImageView ivEditNamaPaketButton;
        public ViewHolder(View itemView) {
            super(itemView);
            tvIdAlat = itemView.findViewById(R.id.tvIdAlat);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvTanggalMulai = itemView.findViewById(R.id.tvTanggalMulai);
            tvPeriode = itemView.findViewById(R.id.tvPeriode);
            tvTanggalSelesai = itemView.findViewById(R.id.tvTanggalSelesai);
            tvSisaHari = itemView.findViewById(R.id.tvSisaHari);
            tvNomorPaket = itemView.findViewById(R.id.tvNomorPaket);
            btnLiveMonitoring = itemView.findViewById(R.id.btnMonitoring);
            btnPerpanjang = itemView.findViewById(R.id.btnPerpanjang);
            ivEditNamaPaketButton = itemView.findViewById(R.id.ivEditNamaPaket);
        }
    }
}
