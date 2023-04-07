package com.tester.svquickcount.Adapter;

import static com.tester.svquickcount.Config.Config.BASE_URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tester.svquickcount.DetailPaslon;
import com.tester.svquickcount.Model.ListPaslon;
import com.tester.svquickcount.R;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class PaslonAdapter extends RecyclerView.Adapter<PaslonAdapter.ViewHolder> {
    Context context;
    Activity activity;

    List<ListPaslon> listRiwayats;

    int layout;
    public PaslonAdapter(List<ListPaslon> getDataAdapter, Context context, Activity activity, int layout) {

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

        final ListPaslon getDataAdapter1 = listRiwayats.get(position);
        activity.runOnUiThread((Runnable) (new Runnable() {
            public final void run() {
                try {
                    String str1 = getDataAdapter1.getNamaPaslon();
                    if (str1.length() > 20) {
                        str1 = str1.substring(0, 20);
                        str1 += "...";
                    }
                    holder.tvNamaPaslon.setText(StringUtils.capitalize(str1.toLowerCase()));
                    String suarasah = new DecimalFormat("##,##0").format(Long.parseLong(getDataAdapter1.getSuaraSah()));
                    String tidaksah = new DecimalFormat("##,##0").format(Long.parseLong(getDataAdapter1.getSuaraTidaksah()));
                    holder.tvSuaraSah.setText(suarasah);
                    holder.tvSuaraTidakSah.setText(tidaksah);
                    holder.tvJenisPaslon.setText("Calon "+getDataAdapter1.getJenisPaslon());
                    Picasso.with(context).load(BASE_URL+"assets/paslon/"+getDataAdapter1.getImages())
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .into(holder.ivGambar);
                }catch (Exception e){

                }

            }
        }));


        holder.lnPaslon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, DetailPaslon.class);
                intent.putExtra("gambar",getDataAdapter1.getImages());
                intent.putExtra("nama_paslon",getDataAdapter1.getNamaPaslon());
                intent.putExtra("jenis_paslon",getDataAdapter1.getJenisPaslon());
                intent.putExtra("suarasah",getDataAdapter1.getSuaraSah());
                intent.putExtra("tidaksah",getDataAdapter1.getSuaraTidaksah());
                intent.putExtra("visi",getDataAdapter1.getVisi());
                intent.putExtra("misi",getDataAdapter1.getMisi());
                intent.putExtra("id_paslon",getDataAdapter1.getId());
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

        public TextView tvNamaPaslon, tvJenisPaslon,tvSuaraSah,tvSuaraTidakSah;
        public RoundedImageView ivGambar;
        public LinearLayout lnPaslon;
        public ViewHolder(View itemView) {

            super(itemView);
            tvNamaPaslon = (TextView) itemView.findViewById(R.id.tvNamaPaslon);
            tvJenisPaslon = (TextView) itemView.findViewById(R.id.tvJenisPaslon);
            tvSuaraSah = (TextView) itemView.findViewById(R.id.tvSuaraSah);
            tvSuaraTidakSah=(TextView)itemView.findViewById(R.id.tvSuaraTidakSah);
            ivGambar = (RoundedImageView) itemView.findViewById(R.id.ivGambar);
            lnPaslon = (LinearLayout) itemView.findViewById(R.id.lnPaslon);
        }
    }





}
