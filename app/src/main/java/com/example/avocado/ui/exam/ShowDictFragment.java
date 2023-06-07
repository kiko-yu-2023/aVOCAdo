package com.example.avocado.ui.exam;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.avocado.R;
import com.example.avocado.databinding.FragmentShowDictBinding;

public class ShowDictFragment extends Fragment {
    private FragmentShowDictBinding bindnig;

    public ShowDictFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_dict, container, false);
    }
}