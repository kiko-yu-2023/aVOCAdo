package com.example.avocado.ui.mypage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.avocado.R;
import com.example.avocado.databinding.FragmentMyPageBinding;
import com.example.avocado.ui.home.HomeFragment;

public class MyPageFragment extends Fragment implements View.OnClickListener{

    private FragmentMyPageBinding binding;
    private ImageView quizScore, testScore;
    private int selectedButton;
    private String title;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        container.removeAllViews();

        binding = FragmentMyPageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        quizScore = binding.quizScore;
        testScore = binding.testScore;

        quizScore.setOnClickListener(this);
        testScore.setOnClickListener(this);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        selectedButton = v.getId();
        selectDict();
    }

    private void selectDict() {
        HomeFragment homeFragment = (new HomeFragment());
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.mypage_layout, homeFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        homeFragment.setOnItemClickListener(new HomeFragment.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String dictName) {
                title = dictName;

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                Fragment selectedFragment;

                if (selectedButton == R.id.quiz_score) {
                    selectedFragment = new QuizScoreFragment(title);
                } else if (selectedButton == R.id.test_score) {
                    selectedFragment = new TestScoreFragment(title);
                } else{
                    selectedFragment = new HomeFragment();
                }

                transaction.replace(homeFragment.getId(), selectedFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });
    }
}