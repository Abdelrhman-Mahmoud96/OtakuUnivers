package com.example.dell.otakuunivers.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.otakuunivers.Data.AnimeInfo;
import com.example.dell.otakuunivers.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EpisodesRecyclerAdapter extends RecyclerView.Adapter<EpisodesRecyclerAdapter.ViewHolder>  {

    ArrayList<AnimeInfo> infos ;
    RecyclerViewItemClickListner recyclerViewItemClickListner;

    public EpisodesRecyclerAdapter(ArrayList<AnimeInfo> c_infos,RecyclerViewItemClickListner c_recyclerViewItemClickListner)
    {
        infos = c_infos;
        recyclerViewItemClickListner = c_recyclerViewItemClickListner;
    }

    @NonNull
    @Override
    public EpisodesRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.episode_card,parent,false);

        return new ViewHolder(view,recyclerViewItemClickListner);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(infos.get(position));
    }


    @Override
    public int getItemCount() {
        return infos.size();
    }





    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ViewHolder(View itemView, RecyclerViewItemClickListner c_recyclerViewItemClickListner) {
            super(itemView);
            recyclerViewItemClickListner = c_recyclerViewItemClickListner;

        }

        public void bind(AnimeInfo obj)
        {
            TextView name = itemView.findViewById(R.id.episodeName);
            ImageView imageView = itemView.findViewById(R.id.episodeThumb);

            name.setText(obj.getRating()+"."+obj.getTitle());

            if(obj.getPosters() == "" || obj.getPosters()== null)
            {
                imageView.setImageResource(R.drawable.detailesplaceholder);
            }
            else
            {
                Picasso.get().load(obj.getPosters()).into(imageView);
            }

        }

        @Override
        public void onClick(View v) {
            recyclerViewItemClickListner.onClick(getAdapterPosition());
        }
    }


}
