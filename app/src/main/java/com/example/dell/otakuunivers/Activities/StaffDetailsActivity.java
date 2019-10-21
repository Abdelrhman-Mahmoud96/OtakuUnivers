package com.example.dell.otakuunivers.Activities;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.dell.otakuunivers.R;
import com.squareup.picasso.Picasso;

public class StaffDetailsActivity extends AppCompatActivity {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_details);

        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("name");
        String image = bundle.getString("image");
        String description = bundle.getString("description");
        String role = bundle.getString("role");
        String media = bundle.getString("media");

        setTitle(name);

        TextView nameView = findViewById(R.id.staffName);
        TextView roleView = findViewById(R.id.staffRole);
        final TextView descView = findViewById(R.id.staffDescription);
        descView.setMovementMethod(new ScrollingMovementMethod());
        ImageView imageView = findViewById(R.id.staffImage);
        Button mediaBtn = findViewById(R.id.staffMedia);
        ScrollView scrollView = findViewById(R.id.detailsScrollView);

        if(image == "" || image == null)
        {
            imageView.setImageResource(R.drawable.characterplacholder);
        }
        else
        {
            Picasso.get().load(image).into(imageView);
        }

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                 descView.getParent().requestDisallowInterceptTouchEvent(false);
                 return false;
            }
        });

        descView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                descView.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        nameView.setText(name);
        descView.setText(description);
        roleView.setText(role);

    }
}
