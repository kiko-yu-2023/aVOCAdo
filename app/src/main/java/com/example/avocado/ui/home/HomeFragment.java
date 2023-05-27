package com.example.avocado.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.avocado.db.dict;

import com.example.avocado.databinding.FragmentHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Array;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    public RecyclerView rc_dict;
    public dictAdapter adapter_dict;
    //원본 데이터 리스트
    public ArrayList<dict> total_items = new ArrayList<>();
    //검색한 데이터 리스트
    public ArrayList<dict> search_list = new ArrayList<>();
    public EditText editText;


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

        //리사이클려뷰 초기화
        rc_dict = binding.recyclerviewDict;
        rc_dict.setHasFixedSize(true);

        for(int i=0;i<5;i++)
        {
            total_items.add(new dict("abc"+i)); //임시데이터
            Log.e("dict",total_items.get(i).getDictID()+ " "+ total_items.get(i).getTitle()+" "+total_items.get(i).getDate());
        }

        editText = binding.searchText;

        // editText 리스터 작성
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchText = editText.getText().toString();
                search_list.clear();

                if(searchText.equals("")) {
                    adapter_dict.setItems(total_items);
                }
                else {
                    for(int i=0;i<total_items.size();i++){
                        if(total_items.get(i).getTitle().toLowerCase().contains(searchText.toLowerCase())){
                            search_list.add(total_items.get(i));
                        }
                    }
                    adapter_dict.setItems(search_list);
                }
            }
        });

        adapter_dict = new dictAdapter(total_items);
        RecyclerView.LayoutManager mlayoutManager = new GridLayoutManager(getActivity(),3);
        rc_dict.setLayoutManager(mlayoutManager);
        rc_dict.setAdapter(adapter_dict);



        final FloatingActionButton addMemo = binding.floatingActionButton;
        addMemo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                NewMemoFragment newMemoFragment = NewMemoFragment.newInstance();
                newMemoFragment.setFragmentInterfacer(new NewMemoFragment.FragmentInterfacer() {
                    @Override
                    public void onButtonClick(String input) {

                    }
                });
                newMemoFragment.show(getActivity().getSupportFragmentManager(),"NewMemoFragment");
            }
        });


        return root;
    }

//    public void onStart() {
//        super.onStart();
//        for(int i=0;i<5;i++)
//        {
//            total_items.add(new dict("abc"+i)); //임시데이터
//        }
//
//        adapter_dict = new dictAdapter(total_items);
//        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager((getActivity()));
//        rc_dict.setLayoutManager(mlayoutManager);
//        rc_dict.setAdapter(adapter_dict);
//    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}