package com.tester.iotss.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tester.iotss.R;
import com.tester.iotss.domain.model.HistoryList;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context context;
    private List<HistoryList> list;
    private Activity activity;
    public HistoryAdapter(Context context, List<HistoryList> list, Activity activity) {
        this.context = context;
        this.list = list;
        this.activity=activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_history, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final HistoryList getDataAdapter1 = list.get(position);
        try {
            holder.tvNoreff.setText(getDataAdapter1.getNoreff());
            holder.tvKeterangan.setText(getDataAdapter1.getKeterangan());
            holder.tvTipe.setText(getDataAdapter1.getTipe());
            if(getDataAdapter1.getTipe().equals("Kredit")){
                holder.tvJumlah.setTextColor(Color.parseColor("#5CB489"));
                holder.tvJumlah.setText("+ "+getDataAdapter1.getJumlah());
            }else{
                holder.tvJumlah.setTextColor(Color.parseColor("#CB3A31"));
                holder.tvJumlah.setText("- "+getDataAdapter1.getJumlah());
            }

            holder.tvSaldoAwal.setText(getDataAdapter1.getSaldoAwal());
            holder.tvSaldoAkhir.setText(getDataAdapter1.getSaldoAkhir());
            holder.tvUpdatedAt.setText(getDataAdapter1.getUpdatedAt());
            if(getDataAdapter1.getStatus().equals("Success")){
                holder.tvStatus.setEnabled(true);
                holder.tvStatus.setText(getDataAdapter1.getStatus());
            }else if(getDataAdapter1.getStatus().equals("Failed")){
                holder.tvStatus.setEnabled(false);
                holder.tvStatus.setHovered(false);
                holder.tvStatus.setText("Failed");
            }else if(getDataAdapter1.getStatus().equals("Pending")){
                holder.tvStatus.setEnabled(false);
                holder.tvStatus.setHovered(true);
                holder.tvStatus.setText("Pending");
            }
        }catch (Exception e){
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNoreff, tvStatus, tvKeterangan,tvTipe, tvJumlah,tvSaldoAwal,tvSaldoAkhir,tvUpdatedAt;
        public ViewHolder(View itemView) {

            super(itemView);
            tvNoreff = (TextView) itemView.findViewById(R.id.tvNoreff);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
            tvKeterangan = (TextView) itemView.findViewById(R.id.tvKeterangan);
            tvTipe = (TextView) itemView.findViewById(R.id.tvTipe);
            tvJumlah = (TextView) itemView.findViewById(R.id.tvJumlah);
            tvSaldoAwal = (TextView) itemView.findViewById(R.id.tvSaldoAwal);
            tvSaldoAkhir = (TextView) itemView.findViewById(R.id.tvSaldoAkhir);
            tvUpdatedAt = (TextView) itemView.findViewById(R.id.tvUpdatedAt);


        }
    }



}
