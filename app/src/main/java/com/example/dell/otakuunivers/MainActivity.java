package com.example.dell.otakuunivers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.dell.otakuunivers.Activities.Categories;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTheme(R.style.SplashScreen);

        Intent intent = new Intent(MainActivity.this, Categories.class);
        startActivity(intent);
        finish();

    }
}
