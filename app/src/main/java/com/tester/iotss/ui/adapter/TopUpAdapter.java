package com.tester.iotss.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tester.iotss.R;
import com.tester.iotss.domain.model.ListTopUp;

import java.text.NumberFormat;
import java.util.List;

public class TopUpAdapter extends RecyclerView.Adapter<TopUpAdapter.ViewHolder> {
    private final List<ListTopUp> listTopUp;
    private final View.OnClickListener onClickListener;
    private final NumberFormat numberFormat;

    public TopUpAdapter(List<ListTopUp> btn, NumberFormat format, View.OnClickListener click) {
        this.listTopUp = btn;
        this.numberFormat = format;
        this.onClickListener = click;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.btn,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListTopUp list = listTopUp.get(position);

        holder.btnText.setText(numberFormat.format(list.GetPrice()));
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return listTopUp.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView btnText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnText = itemView.findViewById(R.id.btnText);
        }
    }
}
