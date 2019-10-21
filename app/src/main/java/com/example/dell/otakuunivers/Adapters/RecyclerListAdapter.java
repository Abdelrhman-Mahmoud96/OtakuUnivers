package com.example.dell.otakuunivers.Adapters;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.otakuunivers.Activities.CharacterActivity;
import com.example.dell.otakuunivers.Activities.StaffActivity;
import com.example.dell.otakuunivers.Data.AnimeInfo;
import com.example.dell.otakuunivers.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ViewHolder> {


   ArrayList<AnimeInfo> infos;
   RecyclerViewItemClickListner recyclerViewItemClickListner;
   String parentName = "";
   public RecyclerListAdapter(ArrayList<AnimeInfo> c_infos,RecyclerViewItemClickListner c_recyclerViewItemClickListner)
   {
       infos = c_infos;
       recyclerViewItemClickListner = c_recyclerViewItemClickListner;
   }

    @NonNull
    @Override
    public RecyclerListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_list_card,parent,false);
        if(parent.getContext().getClass() == StaffActivity.class)
        {
            parentName = "staff";
        }
        else if (parent.getContext().getClass() == CharacterActivity.class)
        {
            parentName = "character";
        }
        else
        {
            parentName = "general";
        }

//        setHasStableIds(true);
        return new ViewHolder(view,recyclerViewItemClickListner);

    }

    @Override
    public long getItemId(int position) {
       AnimeInfo animeInfo = infos.get(position);
        return Long.parseLong(animeInfo.getId());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

       holder.bind(infos.get(position));
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ViewHolder(View itemView,RecyclerViewItemClickListner c_recyclerViewItemClickListner) {
            super(itemView);
            recyclerViewItemClickListner = c_recyclerViewItemClickListner;
        }

        public void bind(AnimeInfo info)
        {
            TextView name = itemView.findViewById(R.id.itemTitle);
            TextView category = itemView.findViewById(R.id.itemCategories);
           // TextView moreInfo = itemView.findViewById(R.id.itemMore);
            ImageView image = itemView.findViewById(R.id.itemImage);

            name.setText(info.getTitle());
            category.setText(info.getGenre());


            if(info.getPosters()=="")
            {
                if (parentName == "staff")
                {
                    image.setImageResource(R.drawable.staffplaceholder);
                }
                if (parentName == "character")
                {
                    image.setImageResource(R.drawable.characterplacholder);
                }
                if (parentName == "general")
                {
                    image.setImageResource(R.drawable.placeholder);
                }

            }
            else
            {
                Picasso.get().load(info.getPosters()).fit().into(image);

            }
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            recyclerViewItemClickListner.onClick(getAdapterPosition());
        }
    }

    @Override
    public int getItemCount() {
        return infos.size();
    }





}
