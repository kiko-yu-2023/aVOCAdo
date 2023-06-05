package com.example.avocado;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.avocado.db.AppDatabase;
import com.example.avocado.ui.home.HomeFragment;
import com.example.avocado.ui.home.NewMemoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.avocado.databinding.ActivityMainBinding;

import java.util.List;

import io.reactivex.rxjava3.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private int NUM_PAGES= -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Memo 몇개 만들지 pageIndicator 받아오기?

        //배경화면 보이도록 패딩넣기
        ConstraintLayout mainLayout = binding.container;
        int padding = getResources().getDimensionPixelSize(R.dimen.fragment_padding);
        mainLayout.setPadding(padding,0,padding,padding);


        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setItemIconTintList(null);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container,fragment).commit();
    }

    //키보드 나왔을 때 다른 곳을 터치하면 키보드 내리기
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        if (view != null && (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_MOVE) &&
                view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int[] sourceCoordinates = new int[2];
            view.getLocationOnScreen(sourceCoordinates);
            float x = event.getRawX() + view.getLeft() - sourceCoordinates[0];
            float y = event.getRawY() + view.getTop() - sourceCoordinates[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom()) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
        return super.dispatchTouchEvent(event);
    }



}