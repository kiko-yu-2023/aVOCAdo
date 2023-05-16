package com.example.avocado.ui.home;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.avocado.MainActivity;
import com.example.avocado.databinding.FragmentNewMemoBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewMemoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewMemoFragment extends DialogFragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentNewMemoBinding binding;

    public NewMemoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment NewMemoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewMemoFragment newInstance() {
        NewMemoFragment fragment = new NewMemoFragment();
        Bundle args = new Bundle();
/*        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    interface FragmentInterfacer{
        void onButtonClick(String input);
    }

    private FragmentInterfacer fragmentInterfacer;

    public void setFragmentInterfacer(FragmentInterfacer fragmentInterfacer){
        this.fragmentInterfacer = fragmentInterfacer;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentNewMemoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final Button createNewMemo = binding.createNewMemo;
        createNewMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText inputMemoName = binding.inputMemoName;
                String memoName = null;

                if(inputMemoName.getText().toString().length()==0)
                {
                    memoName = "imsi";
                }
                else {
                    memoName=inputMemoName.getText().toString();
                }
                fragmentInterfacer.onButtonClick(memoName);
                getDialog().dismiss();

            }
        });

        return root;
    }

    @Override
    public void onClick(View view) {

    }
}