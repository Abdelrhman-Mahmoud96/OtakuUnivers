package com.example.dell.otakuunivers.Adapters;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.example.dell.otakuunivers.Activities.CharacterActivity;
import com.example.dell.otakuunivers.Data.AnimeInfo;
import com.example.dell.otakuunivers.Data.NetworkHandler;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class NetworkLoader extends AsyncTaskLoader<List<AnimeInfo>>{

    String link ;
    Context context;
    String category;
    ArrayList<AnimeInfo> infos = new ArrayList<>();
    CharacterActivity activity = new CharacterActivity();

    public NetworkLoader(String c_link,Context c_context,String c_category) {
        super(c_context);
        link = c_link;
        context = c_context;
        category = c_category;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public ArrayList<AnimeInfo> loadInBackground() {

        if (link==null)
        {
            return null;
        }

            try {
                if(category == "staff")
                {
                    return infos = NetworkHandler.fetchDataFromStaffLink(link);
                }
                if (category == "anime" || category == "manga"||category =="episode")
                {

                    return infos = NetworkHandler.fetchDatafromServer(link,context);
                }

                if (category == "character" )
                {
                    if(activity.state == "fromActivity")
                    {
                        return infos = NetworkHandler.fetchDatafromServer(link,context);
                    }
                    else if (activity.state == "fromIntent")
                    {
                        return infos = NetworkHandler.fetchDataFromStaffLink(link);
                    }
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e("NETWORKLOADER ERROR",String.valueOf(infos.size()) );
            }

        return infos;
    }
}
