package com.example.avocado.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.avocado.MainActivity;
import com.example.avocado.databinding.FragmentHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public static Fragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
/*        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        container.removeAllViews();

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        final FloatingActionButton addMemo = binding.floatingActionButton;
        addMemo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                NewMemoFragment newMemoFragment = NewMemoFragment.newInstance();
                newMemoFragment.setFragmentInterfacer(new NewMemoFragment.FragmentInterfacer() {
                    @Override
                    public void onButtonClick(String input) {
                        textView.setText(input);
                    }
                });
                newMemoFragment.show(getActivity().getSupportFragmentManager(),"NewMemoFragment");
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}