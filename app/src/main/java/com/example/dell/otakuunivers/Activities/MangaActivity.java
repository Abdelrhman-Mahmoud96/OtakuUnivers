package com.example.dell.otakuunivers.Activities;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dell.otakuunivers.Adapters.NetworkLoader;
import com.example.dell.otakuunivers.Adapters.RecyclerListAdapter;
import com.example.dell.otakuunivers.Adapters.RecyclerViewItemClickListner;
import com.example.dell.otakuunivers.Data.AnimeInfo;
import com.example.dell.otakuunivers.R;

import java.util.ArrayList;
import java.util.List;

public class MangaActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<AnimeInfo>> , RecyclerViewItemClickListner {

    int MANGA_ID = 3;
    ArrayList<AnimeInfo> infos = new ArrayList<>();
    int pageLimit = 20;
    int currentPage = 0;
    boolean isLoading = false;
    Button moreBtn ;
    RecyclerListAdapter recyclerListAdapter = null;
    LoaderManager loaderManager;
    LinearLayoutManager linearLayoutManager;

    String link ="https://kitsu.io/api/edge/manga?page[limit]="+pageLimit+"&page[offset]="+currentPage+"&sort=popularityRank";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            String searchQuery = bundle.getString("query");
            if(searchQuery != null)
            {
                link = "https://kitsu.io/api/edge/manga?filter[text]="+searchQuery;
            }
        }

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo NI = connectivityManager.getActiveNetworkInfo();

        ProgressBar progressBar = new android.widget.ProgressBar(this,null,R.attr.progressBarStyle);
        final TextView message = findViewById(R.id.loadMessage);
        View loadingBar = findViewById(R.id.progressBar);
        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary,R.color.colorPrimaryDark);
        moreBtn = findViewById(R.id.loadMore);


        if(NI!=null && NI.isConnected() )
        {
            loaderManager = getLoaderManager();
            loaderManager.initLoader(MANGA_ID, null, this);
            message.setText("");


        }
        else {
            message.setText(R.string.internet_connection);
            loadingBar.setVisibility(View.GONE);

        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                final NetworkInfo NI = connectivityManager.getActiveNetworkInfo();
                message.setText("Loading...");
                final View loadingBar = findViewById(R.id.progressBar);
                swipeRefreshLayout.setRefreshing(true);

                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(NI!=null && NI.isConnected())
                        {
                            loaderManager = getLoaderManager() ;
                            loaderManager.initLoader(MANGA_ID, null, MangaActivity.this);
                            swipeRefreshLayout.setRefreshing(false);
//                           message.setText("");
                        }
                        else {
                            message.setText(R.string.internet_connection);
                            swipeRefreshLayout.setRefreshing(false);
                            loadingBar.setVisibility(View.GONE);

                        }
                    }
                }, 3000);

            }
        });


        RecyclerView recyclerView = findViewById(R.id.animeList);
        recyclerListAdapter = new RecyclerListAdapter(infos,this);
        recyclerView.setAdapter(recyclerListAdapter);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String searchQuery = intent.getStringExtra(SearchManager.QUERY);
            link = "https://kitsu.io/api/edge/manga?filter[text]="+searchQuery;

        }

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_search,menu );

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean queryTextFocused) {
                if(!queryTextFocused) {
                    menu.findItem(R.id.search).collapseActionView();


                }
            }
        });

        return true;
    }

    @Override
    public Loader<List<AnimeInfo>> onCreateLoader(int id, Bundle args) {

        moreBtn.setVisibility(View.GONE);
        return new NetworkLoader(link,this,"manga");
    }

    @Override
    public void onLoadFinished(Loader<List<AnimeInfo>> loader, List<AnimeInfo> data) {

        TextView message = findViewById(R.id.loadMessage);
        View loadingBar = findViewById(R.id.progressBar);
        RecyclerView recyclerView = findViewById(R.id.animeList);

        message.setText("");
        loadingBar.setVisibility(View.GONE);
        isLoading = false;

        if(infos!=null)
        {
//            infos.clear();
            infos.addAll(data);

            recyclerListAdapter.notifyDataSetChanged();

        }

        else {
            message.setText(R.string.loading_failed);
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                if(!isLoading)
                {
                    if(linearLayoutManager != null &&
                            linearLayoutManager.findLastVisibleItemPosition() == infos.size()-1 )
                    {
                        isLoading=true;
                        moreBtn.setVisibility(View.VISIBLE);

                    }

                }
            }
        });

        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPage +=20;
                link ="https://kitsu.io/api/edge/manga?page[limit]="+pageLimit+"&page[offset]="+currentPage+"&sort=popularityRank";
                loaderManager = getLoaderManager();
                loaderManager.restartLoader(MANGA_ID, null,MangaActivity.this );
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<List<AnimeInfo>> loader) {
        infos.clear();
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(this,Details.class);
        intent.putExtra("className", "manga");
        intent.putExtra("name", infos.get(position).getTitle());
        intent.putExtra("start", infos.get(position).getStartedAt());
        intent.putExtra("end", infos.get(position).getEndAt());
        intent.putExtra("story", infos.get(position).getDescription());
        intent.putExtra("cover", infos.get(position).getCoverImage());
        intent.putExtra("rate", infos.get(position).getRating());
        intent.putExtra("popularity", infos.get(position).getPopularity());
        intent.putExtra("ageRating", infos.get(position).getAgeRating());
        intent.putExtra("ageGuide", infos.get(position).getRatingGuid());
        intent.putExtra("status", infos.get(position).getStatus());
        intent.putExtra("noEpisodes", infos.get(position).getNoOfEpisodes());
        intent.putExtra("genre", infos.get(position).getGenre());
        intent.putExtra("episodes", infos.get(position).getEpisodes());
        intent.putExtra("production", infos.get(position).getProduction());
        intent.putExtra("staff", infos.get(position).getStaff());
        intent.putExtra("character",infos.get(position).getCharacters());
        startActivity(intent);
    }
}
