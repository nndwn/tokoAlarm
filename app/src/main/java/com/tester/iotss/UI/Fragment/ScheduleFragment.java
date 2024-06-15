package com.tester.iotss.UI.Fragment;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.tester.iotss.FormJadwalActivity;
import com.tester.iotss.R;
import com.tester.iotss.UI.ViewModel.ScheduleViewModel;
import com.tester.iotss.databinding.FragmentScheduleBinding;

public class ScheduleFragment extends Fragment {

    private ScheduleViewModel mViewModel;
    private FragmentScheduleBinding fragmentScheduleBinding;

    public static ScheduleFragment newInstance() {
        return new ScheduleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentScheduleBinding = FragmentScheduleBinding.inflate(inflater, container, false);
        View view = fragmentScheduleBinding.getRoot();

        mViewModel = new ViewModelProvider(this).get(ScheduleViewModel.class);

        MaterialToolbar toolbar = fragmentScheduleBinding.topAppBar;

        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.tambah_jadwal) {
                startActivity(new Intent(requireContext(), FormJadwalActivity.class));
                return true;
            }
            return false;
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}