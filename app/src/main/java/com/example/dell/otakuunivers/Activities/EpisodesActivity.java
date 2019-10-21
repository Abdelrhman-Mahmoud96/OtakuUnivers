package com.example.dell.otakuunivers.Activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dell.otakuunivers.Adapters.EpisodesRecyclerAdapter;
import com.example.dell.otakuunivers.Adapters.NetworkLoader;
import com.example.dell.otakuunivers.Adapters.RecyclerViewItemClickListner;
import com.example.dell.otakuunivers.Data.AnimeInfo;
import com.example.dell.otakuunivers.R;

import java.util.ArrayList;
import java.util.List;

public class EpisodesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<AnimeInfo>>,RecyclerViewItemClickListner {


    EpisodesRecyclerAdapter episodesRecyclerAdapter = null;
    ArrayList<AnimeInfo> infos = new ArrayList<>();
    String link ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime);

        Bundle bundle = getIntent().getExtras();
        link = bundle.getString("episode");

        ProgressBar progressBar = new android.widget.ProgressBar(this,null,R.attr.progressBarStyle);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo NI = connectivityManager.getActiveNetworkInfo();

        View loadingBar = findViewById(R.id.progressBar);
        TextView message = findViewById(R.id.loadMessage);
        RecyclerView itemsList = findViewById(R.id.animeList);

        if(NI!=null && NI.isConnected())
        {
            LoaderManager loaderManager = getLoaderManager() ;
            loaderManager.initLoader(6, null, this);
            message.setText("");
        }
        else {
            message.setText(R.string.internet_connection);
            loadingBar.setVisibility(View.GONE);
        }
    }

    @Override
    public Loader<List<AnimeInfo>> onCreateLoader(int id, Bundle args) {
        return new NetworkLoader(link,this,"episode");
    }

    @Override
    public void onLoadFinished(Loader<List<AnimeInfo>> loader, List<AnimeInfo> data) {

        View loadingBar = findViewById(R.id.progressBar);
        TextView message = findViewById(R.id.loadMesage);
        RecyclerView itemsList = findViewById(R.id.animeList);

        loadingBar.setVisibility(View.GONE);

        if(infos!=null) {


            infos.clear();
            infos.addAll(data);

            episodesRecyclerAdapter = new EpisodesRecyclerAdapter(infos,this);
            episodesRecyclerAdapter.notifyDataSetChanged();
            itemsList.setAdapter(episodesRecyclerAdapter);

            itemsList.setLayoutManager(new LinearLayoutManager(this));



        }
        else
        {
            message.setText(R.string.loading_failed);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<AnimeInfo>> loader) {infos.clear(); }

    @Override
    public void onClick(int position) {
        Intent episode = new Intent(EpisodesActivity.this, EpisodesDetailsActivity.class);
        episode.putExtra("name",infos.get(position).getTitle());
        episode.putExtra("thump",infos.get(position).getPosters());
        episode.putExtra("description",infos.get(position).getDescription());
        episode.putExtra("length",infos.get(position).getNoOfEpisodes());
        episode.putExtra("airDate",infos.get(position).getStartedAt());

        startActivity(episode);
    }
}
