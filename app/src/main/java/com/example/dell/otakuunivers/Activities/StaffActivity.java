package com.example.dell.otakuunivers.Activities;

import android.app.LoaderManager;
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

import com.example.dell.otakuunivers.Adapters.NetworkLoader;
import com.example.dell.otakuunivers.Adapters.RecyclerListAdapter;
import com.example.dell.otakuunivers.Adapters.RecyclerViewItemClickListner;
import com.example.dell.otakuunivers.Data.AnimeInfo;
import com.example.dell.otakuunivers.R;

import java.util.ArrayList;
import java.util.List;

public class StaffActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<AnimeInfo>>,RecyclerViewItemClickListner {

    RecyclerListAdapter recyclerListAdapter = null;
    ArrayList<AnimeInfo> staffArrayList = new ArrayList<>();
    String LINK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime);

        Bundle bundle = getIntent().getExtras();
        LINK = bundle.getString("staffLink");


        ProgressBar progressBar = new android.widget.ProgressBar(this,null,R.attr.progressBarStyle);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo NI = connectivityManager.getActiveNetworkInfo();

        View loadingbar = findViewById(R.id.progressBar);
        TextView message = findViewById(R.id.loadMessage);
        if(NI!= null && NI.isConnected())
        {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(5, null, this);
            message.setText("");
        }
        else
        {
            message.setText(R.string.internet_connection);
            loadingbar.setVisibility(View.GONE);
        }


    }

    @Override
    public Loader<List<AnimeInfo>> onCreateLoader(int id, Bundle args) {

        return new NetworkLoader(LINK,this,"staff");

    }

    @Override
    public void onLoadFinished(Loader<List<AnimeInfo>> loader, List<AnimeInfo> data) {

        View loadingbar = findViewById(R.id.progressBar);
        TextView message = findViewById(R.id.loadMessage);

        message.setText("");
        loadingbar.setVisibility(View.GONE);

        if (staffArrayList!=null)
        {
            staffArrayList.clear();
            staffArrayList.addAll(data);
            RecyclerView staffList = findViewById(R.id.animeList);
            recyclerListAdapter = new RecyclerListAdapter(staffArrayList,this);
            staffList.setAdapter(recyclerListAdapter);
            staffList.setLayoutManager(new LinearLayoutManager(this));
        }
        else
        {
            message.setText(R.string.loading_failed);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<AnimeInfo>> loader) {
        staffArrayList.clear();
    }

    @Override
    public void onClick(int position) {

    }
}
