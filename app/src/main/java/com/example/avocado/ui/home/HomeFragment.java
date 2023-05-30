package com.example.avocado.ui.home;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.avocado.db.AppDatabase;
import com.example.avocado.db.Dict;

import com.example.avocado.databinding.FragmentHomeBinding;
import com.example.avocado.db.DictDao;
import com.example.avocado.db.DictRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.functions.Consumer;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    public RecyclerView rc_dict;
    public DictAdapter adapter_dict;
    //원본 데이터 리스트
    public ArrayList<Dict> total_items = new ArrayList<>();
    //검색한 데이터 리스트
    public ArrayList<Dict> search_list = new ArrayList<>();
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

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //리사이클려뷰 초기화
        rc_dict = binding.recyclerviewDict;
        rc_dict.setHasFixedSize(true);

        TextView noMemoText = binding.noMemoText;

        adapter_dict = new DictAdapter(total_items);
        rc_dict.setLayoutManager(new GridLayoutManager(getActivity(),3));
        rc_dict.setAdapter(adapter_dict);

        DictDao dao = AppDatabase.getDatabase(getContext()).dictDao();
        //dictRepo를 private으로 클래스 oncreate 밖에 정의하는 걸 추천
        DictRepository dRepo = new DictRepository(dao);
        dRepo.getDictsByModified()
                .subscribe(new Consumer<List<Dict>>() {
                    @Override
                    public void accept(List<Dict> dicts) throws Throwable {
                        if(dicts.size()==0)
                        {

                        }
                        else
                        {
                            noMemoText.setText("");
                            Log.d("dicts size",Integer.toString(dicts.size()));
                            total_items.clear(); // total_items 리스트 초기화
                            total_items.addAll(dicts); // 데이터 추가
                            adapter_dict.setItems(total_items);
                            adapter_dict.notifyDataSetChanged();

                        }
                    }
                });


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



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}