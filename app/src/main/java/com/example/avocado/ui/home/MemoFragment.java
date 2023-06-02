package com.example.avocado.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.avocado.databinding.FragmentMemoBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MemoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class MemoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "para1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private PaintView paintView;

    private FragmentMemoBinding binding;

    public MemoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Memo.
     */
    // TODO: Rename and change types and number of parameters
    public static MemoFragment newInstance(String param1, String param2) {
        MemoFragment fragment = new MemoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        container.removeAllViews();
        // Inflate the layout for this fragment

        binding = FragmentMemoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Bundle args = getArguments();
        if(args!=null)
        {
            String dictName = args.getString("dictName","");
        }

        paintView = binding.handWrittingView;

        return root;
    }

    @Override
    public void onResume() {
        paintView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        paintView.onPause();
        super.onPause();
    }
}