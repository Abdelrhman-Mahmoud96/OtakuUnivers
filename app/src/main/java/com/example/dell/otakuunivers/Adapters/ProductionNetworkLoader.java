package com.example.dell.otakuunivers.Adapters;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.dell.otakuunivers.Data.NetworkHandler;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class ProductionNetworkLoader extends AsyncTaskLoader<List<String>> {

    String link;
    ArrayList<String> comanies = new ArrayList<>();

    public ProductionNetworkLoader(Context context,String c_link) {
        super(context);
        link = c_link;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
    @Override
    public List<String> loadInBackground() {
        try {
            comanies =  NetworkHandler.fetchDatafromSingleLink("production", link);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return comanies;
    }
}
