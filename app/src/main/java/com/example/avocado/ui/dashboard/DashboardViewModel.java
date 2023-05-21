package com.example.avocado.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DashboardViewModel extends ViewModel {

    private final MutableLiveData<String> typeOneQuiz = new MutableLiveData<>();
    private final MutableLiveData<String> typeTwoQuiz = new MutableLiveData<>();
    private final MutableLiveData<String> typeThreeQuiz = new MutableLiveData<>();

    public DashboardViewModel() {
        typeOneQuiz.setValue("TYPE 1 QUIZ");
        typeTwoQuiz.setValue("TYPE 2 QUIZ");
        typeThreeQuiz.setValue("TYPE 3 QUIZ");
    }

    public LiveData<String> getTypeOneQuiz() {
        return typeOneQuiz;
    }
    public LiveData<String> getTypeTwoQuiz() {
        return typeTwoQuiz;
    }
    public LiveData<String> getTypeThreeQuiz() {
        return typeThreeQuiz;
    }
}