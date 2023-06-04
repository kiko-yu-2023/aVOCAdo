package com.example.avocado.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.avocado.MemoActivity;
import com.example.avocado.databinding.FragmentNewMemoBinding;
import com.example.avocado.db.AppDatabase;
import com.example.avocado.db.Converters;
import com.example.avocado.db.dict_with_words.Dict;
import com.example.avocado.db.dict_with_words.DictDao;
import com.example.avocado.db.dict_with_words.DictRepository;

import java.util.Date;

import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;

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

    interface FragmentInterfacer {
        void onButtonClick(String input);
    }

    private FragmentInterfacer fragmentInterfacer;

    public void setFragmentInterfacer(FragmentInterfacer fragmentInterfacer) {
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
                String dictName = null;


                dictName = inputMemoName.getText().toString();
                AppDatabase db= AppDatabase.getDatabase(getContext());
                DictDao dao = db.dictDao();
                //dictRepo를 private으로 클래스 oncreate 밖에 정의하는 걸 추천
                DictRepository dRepo = new DictRepository(dao,db.wordDao());

                //빤스 코드 스틸
                if(dictName==null||dictName.isEmpty())
                {
                    dictName= Converters.dateToTimestamp(new Date());
                }

                Dict dict1 = new Dict(dictName);
                dRepo.insertDict(dict1)
                        .subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                // Called when the operation is subscribed to
                            }

                            @Override
                            public void onComplete() {
                                // Called when the operation is completed successfully
                                Log.d("insertSuccess", "네");
                            }

                            @Override
                            public void onError(Throwable e) {
                                // Called when an error occurs during the operation
                                Log.e("로그 insert Dict", "same title");
                                Toast.makeText(getContext(), "이미 있는 단어장 이름입니다.", Toast.LENGTH_SHORT).show();
                            }
                        });

                fragmentInterfacer.onButtonClick(dictName);
                Log.e("change", dictName);

                Intent intent = new Intent(getActivity(), MemoActivity.class);

                //Bundle을 사용하여 데이터 전달(제목으로 가져옴.)
                intent.putExtra("dictName",dictName);
                Log.d("dictName",dictName);
                startActivity(intent);

                getDialog().dismiss();

            }
        });

        return root;
    }

    @Override
    public void onClick(View view) {

    }
}