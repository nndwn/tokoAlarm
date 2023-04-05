package com.tester.svquickcount.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tester.svquickcount.R;

import java.util.List;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class SliderAdapter extends PagerAdapter {

    private Context context;
    private List<String> colorName;

    public SliderAdapter(Context context, List<String> colorName) {
        this.context = context;
        this.colorName = colorName;
    }

    @Override
    public int getCount() {
        return colorName.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_slider, null);

        RoundedImageView imageView = (RoundedImageView) view.findViewById(R.id.imageView);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);

        String imageResource = colorName.get(position);
        //imageView.setImageResource(imageResource);
        Picasso.with(context).load(imageResource)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .placeholder(R.drawable.bannerplaceholder)
                .into(imageView);
        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}