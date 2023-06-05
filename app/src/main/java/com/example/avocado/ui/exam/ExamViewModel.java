package com.example.avocado.ui.exam;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ExamViewModel extends ViewModel {

    private final MutableLiveData<String> typeOneQuiz = new MutableLiveData<>();
    private final MutableLiveData<String> typeTwoQuiz = new MutableLiveData<>();
    private final MutableLiveData<String> typeThreeQuiz = new MutableLiveData<>();
    private final MutableLiveData<String> addWord = new MutableLiveData<>();

    public ExamViewModel() {
        typeOneQuiz.setValue("TYPE 1 QUIZ");
        typeTwoQuiz.setValue("TYPE 2 QUIZ");
        typeThreeQuiz.setValue("TYPE 3 QUIZ");
        addWord.setValue("단어 추가하기");
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
    public LiveData<String> addWord() { return addWord; }

}