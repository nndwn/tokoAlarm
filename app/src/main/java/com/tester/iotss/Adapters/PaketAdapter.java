package com.tester.iotss.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.tester.iotss.Model.PaketList;
import com.tester.iotss.R;
import java.util.List;

public class PaketAdapter extends RecyclerView.Adapter<PaketAdapter.ViewHolder> {

    private Context context;
    private List<PaketList> list;
    private Activity activity;
    private int selectedItem = RecyclerView.NO_POSITION;
    private OnItemClickListener listener;
    public PaketAdapter(Context context, List<PaketList> list, Activity activity, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.activity=activity;
        this.listener=listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_paket, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PaketList getDataAdapter1 = list.get(position);
        try {
            holder.tvNamaPaket.setText(getDataAdapter1.getPeriode());
            holder.tvDurasi.setText(getDataAdapter1.getDayConvertion()+" Hari");
            holder.tvHarga.setText(getDataAdapter1.getBiayaRupiah());
            Drawable drawable;
            if (selectedItem == position) {
                drawable = ContextCompat.getDrawable(activity, R.drawable.orange_rect);
                holder.tvNamaPaket.setTextColor(Color.WHITE);
                holder.tvDurasi.setTextColor(Color.WHITE);
                holder.tvHarga.setTextColor(Color.WHITE);
                holder.tvTitleDurasi.setTextColor(Color.WHITE);
                holder.tvTitleHarga.setTextColor(Color.WHITE);
                ViewCompat.setBackgroundTintList(holder.dashLineAtas, ContextCompat.getColorStateList(activity, R.color.white));
                ViewCompat.setBackgroundTintList(holder.dashLineBawah, ContextCompat.getColorStateList(activity, R.color.white));
            } else {
                drawable = ContextCompat.getDrawable(activity, R.drawable.white_rect);
                holder.tvNamaPaket.setTextColor(Color.BLACK);
                holder.tvDurasi.setTextColor(Color.BLACK);
                holder.tvHarga.setTextColor(Color.BLACK);
                holder.tvTitleDurasi.setTextColor(Color.BLACK);
                holder.tvTitleHarga.setTextColor(Color.BLACK);
                ViewCompat.setBackgroundTintList(holder.dashLineAtas, ContextCompat.getColorStateList(activity, R.color.black));
                ViewCompat.setBackgroundTintList(holder.dashLineBawah, ContextCompat.getColorStateList(activity, R.color.black));
            }
            holder.lnCard.setBackground(drawable);

            // Attach click listener here
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        notifyItemChanged(selectedItem);
                        selectedItem = position;
                        notifyItemChanged(selectedItem);
                    }
                    listener.onItemClick(position);
                }
            });
        }catch (Exception e){
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvNamaPaket, tvDurasi, tvHarga,tvTitleHarga,tvTitleDurasi;
        public LinearLayout lnCard;
        public View dashLineAtas,dashLineBawah;
        public ViewHolder(View itemView) {

            super(itemView);
            tvNamaPaket = (TextView) itemView.findViewById(R.id.tvNamaPaket);
            tvDurasi = (TextView) itemView.findViewById(R.id.tvDurasi);
            tvHarga = (TextView) itemView.findViewById(R.id.tvHarga);
            lnCard =(LinearLayout) itemView.findViewById(R.id.lnCard);
            dashLineAtas = (View) itemView.findViewById(R.id.dashLineAtas);
            dashLineBawah = (View) itemView.findViewById(R.id.dashLineBawah);
            tvTitleHarga = (TextView) itemView.findViewById(R.id.tvTitleHarga);
            tvTitleDurasi = (TextView) itemView.findViewById(R.id.tvTitleDurasi);
        }

    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
