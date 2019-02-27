package com.manoj.newschannels;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.List;



public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<Sources> sources;
    private Context context;
    public String url;

    public Adapter(List<Sources> sources, Context context) {
        this.sources = sources;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.news_item,viewGroup,false);

        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        viewHolder.textName.setText(sources.get(i).getName());

        viewHolder.textCategory.setText(sources.get(i).getCategory());

        //To get the images from source
        Picasso.with(context).load("https://besticon-demo.herokuapp.com/icon?url=" + sources.get(i).getUrl().replace("https://", "") + "&size=80..120..200").
                //Its an external source as there were no icons in API
                error(R.drawable.error).resize(100, 100).
                into(viewHolder.image, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });

        //To save in Cache
        Picasso.with(context).load("https://besticon-demo.herokuapp.com/icon?url=" + sources.get(i).getUrl().replace("https://", "") + "&size=80..120..200");

    }


    @Override
    public int getItemCount() {
        return sources.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textName;
        TextView textCategory;
        ImageView image;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textName= itemView.findViewById(R.id.name);
           textCategory=itemView.findViewById(R.id.category);
           image = itemView.findViewById(R.id.img);



        }


    }




}