package com.tester.svquickcount.Adapter;

import static com.tester.svquickcount.Config.Config.BASE_URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tester.svquickcount.Checkout;
import com.tester.svquickcount.DetailPaslon;
import com.tester.svquickcount.Model.ListHistory;
import com.tester.svquickcount.Model.ListHistoryInput;
import com.tester.svquickcount.Pembayaran;
import com.tester.svquickcount.R;

import java.text.DecimalFormat;
import java.util.List;

public class HistoryInputAdapter extends RecyclerView.Adapter<HistoryInputAdapter.ViewHolder> {
    Context context;
    Activity activity;

    List<ListHistoryInput> listRiwayats;

    public HistoryInputAdapter(List<ListHistoryInput> getDataAdapter, Context context, Activity activity) {

        super();
        this.listRiwayats = getDataAdapter;
        this.context = context;
        this.activity = activity;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_history_input, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final ListHistoryInput getDataAdapter1 = listRiwayats.get(position);
        activity.runOnUiThread((Runnable) (new Runnable() {
            public final void run() {
                holder.tvNamaPaslon.setText(getDataAdapter1.getNamaPaslon());
                holder.tvJenisPaslon.setText("Calon "+getDataAdapter1.getJenisPaslon());
                holder.tvTanggal.setText(getDataAdapter1.getCreatedAt());
                holder.tvSuaraSah.setText(": "+getDataAdapter1.getSuaraSah());
                holder.tvSuaraTidakSah.setText(": "+getDataAdapter1.getSuaraTidaksah());
                Picasso.with(context).load(BASE_URL+"assets/paslon/"+getDataAdapter1.getImages())
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(holder.ivGambar);
            }
        }));

        holder.lnItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, DetailPaslon.class);
                intent.putExtra("gambar",getDataAdapter1.getImages());
                intent.putExtra("nama_paslon",getDataAdapter1.getNamaPaslon());
                intent.putExtra("jenis_paslon",getDataAdapter1.getJenisPaslon());
                intent.putExtra("suarasah",getDataAdapter1.getSuaraSah());
                intent.putExtra("tidaksah",getDataAdapter1.getSuaraTidaksah());
                intent.putExtra("visi",getDataAdapter1.getVisi());
                intent.putExtra("misi",getDataAdapter1.getMisi());
                intent.putExtra("id_paslon",getDataAdapter1.getId());
                intent.putExtra("nama_dapil",getDataAdapter1.getNamaDapil());
                intent.putExtra("multi_dapil",getDataAdapter1.getMultiDapil());
                intent.putExtra("tingkatan",getDataAdapter1.getTingkatan());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_up,R.anim.slide_normal);
            }
        });



    }

    @Override
    public int getItemCount() {

        return listRiwayats.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvNamaPaslon, tvTanggal, tvJenisPaslon,tvSuaraSah,tvSuaraTidakSah;
        public LinearLayout lnItem;
        public RoundedImageView ivGambar;
        public ViewHolder(View itemView) {

            super(itemView);
            tvNamaPaslon = (TextView) itemView.findViewById(R.id.tvNamaPaslon);
            tvJenisPaslon = (TextView) itemView.findViewById(R.id.tvJenisPaslon);
            tvSuaraSah = (TextView) itemView.findViewById(R.id.tvSuaraSah);
            tvSuaraTidakSah = (TextView) itemView.findViewById(R.id.tvSuaraTidakSah);
            tvTanggal = (TextView) itemView.findViewById(R.id.tvTanggal);
            lnItem = (LinearLayout) itemView.findViewById(R.id.lnItem);
            ivGambar=(RoundedImageView) itemView.findViewById(R.id.ivGambar);
        }
    }





}