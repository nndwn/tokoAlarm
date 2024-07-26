package com.tester.iotss.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.tester.iotss.domain.model.ListPromo;
import com.tester.iotss.R;
import java.util.List;

public class PromoAdapter extends PagerAdapter {
    Context context;
    private final LayoutInflater layoutInflater;
    private final List<ListPromo> listPromos;

    public PromoAdapter(List<ListPromo> listPromos, Context context) {
        this.listPromos = listPromos;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {

        return listPromos.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.list_promo,  container, false);
        ImageView imageView = view.findViewById(R.id.imageView);

        ListPromo listPromo = listPromos.get(position);
        Glide.with(context)
                .load(listPromo.getBanner())
                .into(imageView);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

}

