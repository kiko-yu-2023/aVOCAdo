package com.example.avocado.ui.mypage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.avocado.R;
import com.example.avocado.databinding.FragmentMyPageBinding;
import com.example.avocado.ui.home.HomeFragment;

public class MyPageFragment extends Fragment implements View.OnClickListener{

    private FragmentMyPageBinding binding;

    ImageView quizScore, testScore;

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
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment selectedFragment;

        if (v.getId() == R.id.quiz_score) {
            selectedFragment = new QuizScoreFragment();
        } else if (v.getId() == R.id.test_score) {
            selectedFragment = new TestScoreFragment();
        } else{
            selectedFragment = new HomeFragment();
        }

        transaction.replace(R.id.nav_host_fragment_activity_main, selectedFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}