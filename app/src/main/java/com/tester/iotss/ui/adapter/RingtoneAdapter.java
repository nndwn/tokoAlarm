package com.tester.iotss.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.tester.iotss.domain.model.RingtoneList;
import com.tester.iotss.R;
import java.util.List;

public class RingtoneAdapter extends RecyclerView.Adapter<RingtoneAdapter.ViewHolder> {

    private Context context;
    private List<RingtoneList> list;
    private Activity activity;
    private OnItemClickListener listener;
    private int selectedItem = RecyclerView.NO_POSITION;
    public RingtoneAdapter(Context context, List<RingtoneList> list, Activity activity, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.activity=activity;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_ringtone, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final RingtoneList getDataAdapter1 = list.get(position);
        try {
            holder.tvTitle.setText(getDataAdapter1.getTitle());
        }catch (Exception e){
        }
        if (selectedItem == position) {
            holder.lnRingtone.setBackgroundColor(Color.parseColor("#3c8dbc"));
            holder.tvTitle.setTextColor(Color.WHITE);
        } else {
            holder.lnRingtone.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.tvTitle.setTextColor(Color.BLACK);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvTitle;
        public LinearLayout lnRingtone;
        public ViewHolder(View itemView) {

            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            lnRingtone = (LinearLayout) itemView.findViewById(R.id.lnRingtone);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                notifyItemChanged(selectedItem); // Me-reset warna latar belakang item sebelumnya
                selectedItem = position; // Menyimpan posisi item yang dipilih
                notifyItemChanged(selectedItem); // Mengubah warna latar belakang item yang dipilih
                listener.onItemClick(position);
            }
        }
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
