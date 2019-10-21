package com.example.dell.otakuunivers.Activities;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;


import com.example.dell.otakuunivers.Adapters.ProductionNetworkLoader;
import com.example.dell.otakuunivers.Data.NetworkHandler;
import com.example.dell.otakuunivers.Data.Staff;
import com.example.dell.otakuunivers.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Details extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<String>> {


    @SuppressLint("ClickableViewAccessibility")
    String LINK;
    ArrayList<String> names = new ArrayList<>();
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailes);

        /*fetching the date from the Activities*/

        Bundle bundle = getIntent().getExtras();
        String className = bundle.getString("className");
        String name = bundle.getString("name");
        String startDate =bundle.getString("start");
        String enddate =bundle.getString("end");
        String story =bundle.getString("story");
        String cover =bundle.getString("cover");
        String rate =bundle.getString("rate");
        String popularity =bundle.getString("popularity");
        String ageRating =bundle.getString("ageRating");
        String ageGuide =bundle.getString("ageGuide");
        String status =bundle.getString("status");
        String NoEpisodes =bundle.getString("noEpisodes");
        final String trailer =bundle.getString("trailer");
        String genre =bundle.getString("genre");
        final String episodes =bundle.getString("episodes");
        final String production =bundle.getString("production");
        LINK = production;
        String streamLinks =bundle.getString("streamLinks");
        final String staffLink = bundle.getString("staff");
        final String charactersLink = bundle.getString("character");

        /*Loader manger init*/
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(4, null, this);

        /*preparing the TextViews*/


        TextView startDateView = findViewById(R.id.detailsStart);
        TextView endDateView   = findViewById(R.id.detailsEnd);
        final TextView storyView     = findViewById(R.id.detailsDescription);
        TextView rateView      = findViewById(R.id.detailsRating);
        TextView popularityView= findViewById(R.id.detailsPopularity);
        TextView ageRatingView = findViewById(R.id.detailsAgeRating);
        TextView ageGuideView  = findViewById(R.id.detailsAgeGuide);
        TextView statusView    = findViewById(R.id.detailsStatus);
        TextView NoEpisodesView = findViewById(R.id.detailsEpisodsNo);
        final TextView genreView     = findViewById(R.id.detailsGenre);
        final TextView productionView = findViewById(R.id.detailsProduction);
        ScrollView scrollView = findViewById(R.id.scrollView);
        ImageView coverView = findViewById(R.id.detailsCover);

        Button trailerView   = findViewById(R.id.detaileTrailer);
        Button charactersView = findViewById(R.id.detaileCharacters);
        Button episodesView  = findViewById(R.id.detaileEpisodes);
        Button streamLinksView = findViewById(R.id.detaileStreaming);
        Button staffView = findViewById(R.id.detaileStaff);



    /*Assigning the data to the Views*/
        setTitle(name);
        startDateView.setText(startDate);

        endDateView.setText(enddate);

        storyView.setText(story);
        storyView.setMovementMethod(new ScrollingMovementMethod());
        genreView.setMovementMethod(new ScrollingMovementMethod());
        productionView.setMovementMethod(new ScrollingMovementMethod());

        /*fixing scrolling textView with ScrollView*/
        scrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                storyView.getParent().requestDisallowInterceptTouchEvent(false);
                genreView.getParent().requestDisallowInterceptTouchEvent(false);
                productionView.getParent().requestDisallowInterceptTouchEvent(false);

                return false;
            }
        });

        storyView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                storyView.getParent().requestDisallowInterceptTouchEvent(true);

                return false;
            }
        });

        genreView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                genreView.getParent().requestDisallowInterceptTouchEvent(true);
                productionView.getParent().requestDisallowInterceptTouchEvent(false);
                storyView.getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });


        productionView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                productionView.getParent().requestDisallowInterceptTouchEvent(true);
                storyView.getParent().requestDisallowInterceptTouchEvent(false);
                genreView.getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

        rateView.setText(rate);
        popularityView.setText(popularity);
        ageRatingView.setText(ageRating);
        ageGuideView.setText(ageGuide);
        statusView.setText(status);
        NoEpisodesView.setText(NoEpisodes);
        genreView.setText(genre);


        if(cover.equals(""))
        {
            coverView.setImageResource(R.drawable.detailesplaceholder);
        }
        else
        {
            Picasso.get().load(cover).into(coverView);
        }

        if(className.equals("manga"))
        {
            episodesView.setText("Chapters");
            trailerView.setVisibility(View.GONE);
            TextView label = findViewById(R.id.detailsEpisodsNoLabel);
            label.setText("No. Volumes:");
        }

        trailerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent youtube = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"+trailer));
                startActivity(youtube);
            }
        });


        staffView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent staff = new Intent(Details.this, StaffActivity.class);
                staff.putExtra("staffLink", staffLink);
                startActivity(staff);
            }
        });

        charactersView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent character = new Intent(Details.this, CharacterActivity.class);
                character.putExtra("character",charactersLink );
                startActivity(character);
            }
        });

        episodesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Details.this,EpisodesActivity.class);
                intent.putExtra("episode",episodes );
                startActivity(intent);
            }
        });


    }


    @Override
    public Loader<List<String>> onCreateLoader(int id, Bundle args) {
        return new ProductionNetworkLoader(this,LINK);
    }

    @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> data) {

        TextView productionView = findViewById(R.id.detailsProduction);

        if (names!=null)
        {

            names.clear();
            names.addAll(data);
            if(names.size()!=0) {
                String companies = NetworkHandler.listToString(names);
                productionView.setText(companies);
            }
            else
            {
                productionView.setText("Unknown");
            }

        }
    }

    @Override
    public void onLoaderReset(Loader<List<String>> loader) {

        names.clear();
    }
}
