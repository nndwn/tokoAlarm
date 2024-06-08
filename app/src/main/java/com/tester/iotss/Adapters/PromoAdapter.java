package com.tester.iotss.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tester.iotss.Model.ListPromo;
import com.tester.iotss.PromoDetail;
import com.tester.iotss.R;
import java.util.List;

public class PromoAdapter extends RecyclerView.Adapter<PromoAdapter.ViewHolder> {
    Context context;
    Activity activity;

    List<ListPromo> listPromos;
    public PromoAdapter(List<ListPromo> getDataAdapter, Context context, Activity activity) {

        super();
        this.listPromos = getDataAdapter;
        this.context = context;
        this.activity = activity;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_promo, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final ListPromo getDataAdapter1 = listPromos.get(position);
        activity.runOnUiThread(() -> Picasso.get().load(getDataAdapter1.getBanner())
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .placeholder(R.drawable.bannerplaceholder)
                .into(holder.ivGambar));


        holder.ivGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, PromoDetail.class);
                intent.putExtra("title", getDataAdapter1.getTitle());
                intent.putExtra("banner", getDataAdapter1.getBanner());
                intent.putExtra("deskripsi", getDataAdapter1.getDeskripsi());
                intent.putExtra("created_at", getDataAdapter1.getCreatedAt());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_normal);
            }
        });


    }

    @Override
    public int getItemCount() {

        return listPromos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public RoundedImageView ivGambar;
        public ViewHolder(View itemView) {

            super(itemView);
            ivGambar = (RoundedImageView) itemView.findViewById(R.id.ivGambar);
        }
    }





}

