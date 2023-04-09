package com.tester.svquickcount.Adapter;

import static com.tester.svquickcount.Config.Config.BASE_URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.vision.text.Line;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tester.svquickcount.DetailPaslon;
import com.tester.svquickcount.Model.ListMonitoring;
import com.tester.svquickcount.Model.ListPaslon;
import com.tester.svquickcount.R;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.List;

public class MonitoringAdapter extends RecyclerView.Adapter<MonitoringAdapter.ViewHolder> {
    Context context;
    Activity activity;

    List<ListMonitoring> listRiwayats;

    int layout;
    String background="#f2f2f2";
    public MonitoringAdapter(List<ListMonitoring> getDataAdapter, Context context, Activity activity, int layout) {

        super();

        this.listRiwayats = getDataAdapter;
        this.context = context;
        this.activity = activity;
        this.layout = layout;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final ListMonitoring getDataAdapter1 = listRiwayats.get(position);
        activity.runOnUiThread((Runnable) (new Runnable() {
            public final void run() {
                try {
                    if(background.equals("#f2f2f2")){
                        holder.lnMonitoring.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        background="#FFFFFF";
                    }else{
                        holder.lnMonitoring.setBackgroundColor(Color.parseColor("#f2f2f2"));
                        background="#f2f2f2";
                    }
                    holder.tvWilayah.setText(StringUtils.capitalize(getDataAdapter1.getWilayah().toLowerCase()));
                    String suarasah = new DecimalFormat("##,##0").format(Long.parseLong(getDataAdapter1.getSuarasah()));
                    String tidaksah = new DecimalFormat("##,##0").format(Long.parseLong(getDataAdapter1.getTidaksah()));
                    holder.tvSuaraSah.setText(suarasah);
                    holder.tvSuaraTidakSah.setText(tidaksah);
                }catch (Exception e){

                }

            }
        }));


    }

    @Override
    public int getItemCount() {

        return listRiwayats.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvWilayah, tvSuaraSah,tvSuaraTidakSah;
        public LinearLayout lnMonitoring;
        public ViewHolder(View itemView) {

            super(itemView);
            tvWilayah = (TextView) itemView.findViewById(R.id.tvWilayah);
            tvSuaraSah = (TextView) itemView.findViewById(R.id.tvSuaraSah);
            tvSuaraTidakSah=(TextView)itemView.findViewById(R.id.tvSuaraTidakSah);
            lnMonitoring=(LinearLayout) itemView.findViewById(R.id.lnMonitoring);
        }
    }





}

