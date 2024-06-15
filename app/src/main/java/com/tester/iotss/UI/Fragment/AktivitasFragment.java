package com.tester.iotss.UI.Fragment;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tester.iotss.R;
import com.tester.iotss.UI.ViewModel.AktivitasViewModel;

public class AktivitasFragment extends Fragment {

    private AktivitasViewModel mViewModel;


    public static AktivitasFragment newInstance() {
        return new AktivitasFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_aktivitas, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AktivitasViewModel.class);
        // TODO: Use the ViewModel
    }

}