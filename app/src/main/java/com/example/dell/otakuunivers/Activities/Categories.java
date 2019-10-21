package com.example.dell.otakuunivers.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.dell.otakuunivers.R;

public class Categories extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);


    }

    public void CategoryIntent(View view) {


        if (view.getId() == R.id.animeItem)
        {
            Intent intent = new Intent(this,AnimeActivity.class);
            startActivity(intent);
        }
        if (view.getId() == R.id.mangaItem)
        {
            Intent intent = new Intent(this,MangaActivity.class);
            startActivity(intent);
        }
        if (view.getId() == R.id.characterItem)
        {
            Intent intent = new Intent(this,CharacterActivity.class);
            startActivity(intent);
        }

    }
}
