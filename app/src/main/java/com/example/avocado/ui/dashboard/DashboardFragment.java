package com.example.avocado.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.avocado.R;
import com.example.avocado.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment implements View.OnClickListener{

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        container.removeAllViews();

        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView typeOneQuiz = binding.typeOneQuiz;
        TextView typeTwoQuiz = binding.typeTwoQuiz;
        TextView typeThreeQuiz = binding.typeThreeQuiz;
        TextView addWord = binding.addWord;


        dashboardViewModel.getTypeOneQuiz().observe(getViewLifecycleOwner(), typeOneQuiz::setText);
        dashboardViewModel.getTypeTwoQuiz().observe(getViewLifecycleOwner(), typeTwoQuiz::setText);
        dashboardViewModel.getTypeThreeQuiz().observe(getViewLifecycleOwner(), typeThreeQuiz::setText);
        dashboardViewModel.addWord().observe(getViewLifecycleOwner(), addWord::setText);

        typeOneQuiz.setOnClickListener(this);
        typeTwoQuiz.setOnClickListener(this);
        typeThreeQuiz.setOnClickListener(this);
        addWord.setOnClickListener(this);
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

        TextView textView = (TextView) v;
        String quizType = textView.getText().toString();

        Fragment selectedFragment;

        // Choose the appropriate fragment based on the quiz type
        if (quizType.equals("TYPE 1 QUIZ")) {
            selectedFragment = new Quiz1Fragment();
        } else if (quizType.equals("TYPE 2 QUIZ")) {
            selectedFragment = new Quiz2Fragment();
        } else if (quizType.equals("TYPE 3 QUIZ")) {
            selectedFragment = new Quiz3Fragment();
        } //else if (quizType.equals("단어 추가하기")) {
            //selectedFragment = new AddWordFragment();
        //}
        else {
            // Default to a fallback fragment if needed
            selectedFragment = new Quiz1Fragment();
        }

        transaction.replace(R.id.nav_host_fragment_activity_main, selectedFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}