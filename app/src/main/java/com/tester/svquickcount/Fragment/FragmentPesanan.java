package com.tester.svquickcount.Fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.tester.svquickcount.R;
import com.tester.svquickcount.SubFragment.SubFragmentBayar;
import com.tester.svquickcount.SubFragment.SubFragmentKirim;
import com.tester.svquickcount.SubFragment.SubFragmentSelesai;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPesanan extends Fragment {


    public FragmentPesanan() {
        // Required empty public constructor
    }

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.result_tabs)
    TabLayout tabs;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_pesanan, container, false);
        ButterKnife.bind(this,view);
        setupViewPager(viewPager);
        tabs.setupWithViewPager(viewPager);
        return  view;
    }


    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {


        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new SubFragmentBayar(), "Aktif");
        adapter.addFragment(new SubFragmentKirim(), "Dikirim");
        adapter.addFragment(new SubFragmentSelesai(), "Selesai");
        viewPager.setAdapter(adapter);



    }


    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
