package com.example.avocado.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.avocado.MemoActivity;
import com.example.avocado.R;
import com.example.avocado.databinding.FragmentHomeBinding;
import com.example.avocado.db.AppDatabase;

import com.example.avocado.db.dict_with_words.Dict;
import com.example.avocado.db.dict_with_words.DictDao;
import com.example.avocado.db.dict_with_words.DictRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.functions.Consumer;

public class HomeFragment extends Fragment implements DictAdapter.OnItemClickListener {

    private FragmentHomeBinding binding;
    public RecyclerView rc_dict;
    public DictAdapter adapter_dict;
    //원본 데이터 리스트
    public ArrayList<Dict> total_items = new ArrayList<>();
    //검색한 데이터 리스트
    public ArrayList<Dict> search_list = new ArrayList<>();
    public EditText editText;
    //리스너 오버라이딩
    private OnItemClickListener listener;


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

        container.removeAllViews();

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //리사이클려뷰 초기화
        rc_dict = binding.recyclerviewDict;
        rc_dict.setHasFixedSize(true);

        TextView noMemoText = binding.noMemoText;

        adapter_dict = new DictAdapter(total_items);
        adapter_dict.setOnItemClickListener(this);
        rc_dict.setLayoutManager(new GridLayoutManager(getActivity(),3));
        rc_dict.setAdapter(adapter_dict);

        AppDatabase db=AppDatabase.getDatabase(getContext());
        //dictRepo를 private으로 클래스 oncreate 밖에 정의하는 걸 추천
        DictRepository dRepo = new DictRepository(db.dictDao(),db.wordDao());
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
                        rc_dict.getAdapter().notifyDataSetChanged();
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

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public void onItemClick(int position, String dictName){
        //아이템 클릭 시 실행될 코드
        // MemoActivity로 이동하는 코드

        if(listener != null){
            listener.onItemClick(position, dictName);
        }else{
            Intent intent = new Intent(getActivity(), MemoActivity.class);

            //Bundle을 사용하여 데이터 전달(제목으로 가져옴.)
            intent.putExtra("dictName",dictName);
            Log.d("dictName",dictName);
            startActivity(intent);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, String dictName);
    }

}

