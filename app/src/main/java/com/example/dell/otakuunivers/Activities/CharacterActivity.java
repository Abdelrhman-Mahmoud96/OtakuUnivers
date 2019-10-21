package com.example.dell.otakuunivers.Activities;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.drm.DrmStore;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
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
import java.util.zip.Inflater;

public class CharacterActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<AnimeInfo>>,RecyclerViewItemClickListner {

    int CHARACTER_ID = 2;
    public static String state = "fromActivity";

    ArrayList<AnimeInfo> infos = new ArrayList<>();
    int pageLimit = 20;
    int currentPage = 0;
    boolean isLoading = false;
    Button moreBtn ;
    RecyclerListAdapter recyclerListAdapter = null;
    LoaderManager loaderManager;
    LinearLayoutManager linearLayoutManager;

    String link = "https://kitsu.io/api/edge/characters?page[limit]="+pageLimit+"&page[offset]="+currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null )
        {
            if(bundle.getString("character") != null)
            {
                link = bundle.getString("character");
                state = "fromIntent";
            }
            else if (bundle.getString("query") != null)
            {
                link="https://kitsu.io/api/edge/characters?filter[name]="+bundle.getString("query");
            }

        }

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo NI = connectivityManager.getActiveNetworkInfo();

        ProgressBar progressBar = new android.widget.ProgressBar(this,null,R.attr.progressBarStyle);
        final TextView message = findViewById(R.id.loadMessage);
        View loadingBar = findViewById(R.id.progressBar);
        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refresh);
        moreBtn = findViewById(R.id.loadMore);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary,R.color.colorPrimaryDark);


        if(NI!=null && NI.isConnected() )
        {
            loaderManager = getLoaderManager();
            loaderManager.initLoader(CHARACTER_ID, null, this);
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
                            loaderManager.initLoader(CHARACTER_ID, null,CharacterActivity.this);
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

        Intent searchIntent = getIntent();
        if(Intent.ACTION_SEARCH.equals(searchIntent.getAction()))
        {
            String query = searchIntent.getStringExtra(SearchManager.QUERY);
            link = "https://kitsu.io/api/edge/characters?filter[name]="+query;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_search,menu );
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
               if(!hasFocus)
               {
                   menu.findItem(R.id.search).collapseActionView();
               }
            }
        });
        return true;
    }

    @Override
    public Loader<List<AnimeInfo>> onCreateLoader(int id, Bundle args) {

        moreBtn.setVisibility(View.GONE);
        return new NetworkLoader(link,this,"character");
    }

    @Override
    public void onLoadFinished(Loader<List<AnimeInfo>> loader, List<AnimeInfo> data) {

        TextView message = findViewById(R.id.loadMessage);
        View loadingBar = findViewById(R.id.progressBar);
        final RecyclerView recyclerView = findViewById(R.id.animeList);
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
                link ="https://kitsu.io/api/edge/characters?page[limit]="+pageLimit+"&page[offset]="+currentPage;
                loaderManager = getLoaderManager();
                loaderManager.restartLoader(CHARACTER_ID, null,CharacterActivity.this );
            }
        });

    }


    @Override
    public void onLoaderReset(Loader<List<AnimeInfo>> loader) {
        infos.clear();
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(this,StaffDetailsActivity.class);
        intent.putExtra("name",infos.get(position).getTitle());
        intent.putExtra("image",infos.get(position).getPosters());

//      replacing space tags in the string
        String fixedDesc = System.getProperty("line.separator");
        String description  = infos.get(position).getDescription();
        description = description.replaceAll("<br>",fixedDesc );
        intent.putExtra("description",description);

        intent.putExtra("role",infos.get(position).getGenre());
        intent.putExtra("media",infos.get(position).getStreamLinks());
        startActivity(intent);
    }
}
