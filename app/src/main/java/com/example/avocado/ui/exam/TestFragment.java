package com.example.avocado.ui.exam;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.avocado.R;
import com.example.avocado.databinding.FragmentTestBinding;

import java.util.Locale;

public class TestFragment extends Fragment implements TextToSpeech.OnInitListener{
    private FragmentTestBinding binding;
    private TextToSpeech tts;
    private Button button;
    private EditText editText;
    public TestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        container.removeAllViews();

        binding = FragmentTestBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        
        tts = new TextToSpeech(getActivity(), this);
        button = binding.button;
        editText = binding.editText;


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence text = editText.getText();
                tts.setPitch((float)1.0);
                tts.setSpeechRate((float)1.0);

                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "uid");
            }
        });

        return root;
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.ENGLISH);
            if(result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA){
                Log.e("TTS","Language not supported.");
            }else{
                button.setText("Ready to Speak");
            }
        }
        else {
            Log.e("TTS", "Initialization failed");
        }
    }
}