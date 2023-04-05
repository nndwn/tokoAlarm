package com.tester.svquickcount.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tester.svquickcount.DetailPromo;
import com.tester.svquickcount.Model.ListPromo;
import com.tester.svquickcount.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import static android.app.Activity.RESULT_OK;

public class PromoAdapter extends RecyclerView.Adapter<PromoAdapter.ViewHolder> {
    Context context;
    Activity activity;

    List<ListPromo> listPromos;
    String from;
    int layout;
    public PromoAdapter(List<ListPromo> getDataAdapter, Context context, Activity activity,int layout,String from) {

        super();
        this.layout=layout;
        this.from=from;
        this.listPromos = getDataAdapter;
        this.context = context;
        this.activity = activity;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final ListPromo getDataAdapter1 = listPromos.get(position);
        activity.runOnUiThread((Runnable) (new Runnable() {
            public final void run() {
                Picasso.with(context).load(getDataAdapter1.getGambar())
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .placeholder(R.drawable.bannerplaceholder)
                        .into(holder.ivGambar);
                if(layout==R.layout.list_promo_detail){
                    holder.tvJudul.setText(getDataAdapter1.getJudul());
                    holder.tvTanggalBerakhir.setText(getDataAdapter1.getTanggalberakhir());
                }

            }
        }));

        if(layout==R.layout.list_promo) {
            holder.ivGambar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, DetailPromo.class);
                    intent.putExtra("id_promo", getDataAdapter1.getId_promo());
                    intent.putExtra("judul", getDataAdapter1.getJudul());
                    intent.putExtra("kodepromo", getDataAdapter1.getKodepromo());
                    intent.putExtra("deskripsi", getDataAdapter1.getDeskripsi());
                    intent.putExtra("tanggalberakhir", getDataAdapter1.getTanggalberakhir());
                    intent.putExtra("gambar", getDataAdapter1.getGambar());
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_normal);
                }
            });
        }else if(layout==R.layout.list_promo_detail){
            if(from.equals("promo")){
                holder.button.setText("Detail");
                holder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, DetailPromo.class);
                        intent.putExtra("id_promo", getDataAdapter1.getId_promo());
                        intent.putExtra("judul", getDataAdapter1.getJudul());
                        intent.putExtra("kodepromo", getDataAdapter1.getKodepromo());
                        intent.putExtra("deskripsi", getDataAdapter1.getDeskripsi());
                        intent.putExtra("tanggalberakhir", getDataAdapter1.getTanggalberakhir());
                        intent.putExtra("gambar", getDataAdapter1.getGambar());
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_normal);
                    }
                });
            }else if(from.equals("checkout")){
                holder.button.setText("Klaim");
                holder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("kodepromo",getDataAdapter1.getKodepromo());
                        activity.setResult(RESULT_OK,intent);
                        activity.finish();
                        activity.overridePendingTransition(R.anim.slide_normal,R.anim.slide_out_down);

                    }
                });
            }
        }



    }

    @Override
    public int getItemCount() {

        return listPromos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public RoundedImageView ivGambar;
        public TextView tvJudul,tvTanggalBerakhir;
        public Button button;
        public ViewHolder(View itemView) {

            super(itemView);
            ivGambar = (RoundedImageView) itemView.findViewById(R.id.ivGambar);
            if(layout==R.layout.list_promo_detail){
                tvJudul = (TextView) itemView.findViewById(R.id.tvJudul);
                tvTanggalBerakhir = (TextView) itemView.findViewById(R.id.tvTanggalBerakhir);
                button = (Button) itemView.findViewById(R.id.btnKlaim);
            }
        }
    }





}

