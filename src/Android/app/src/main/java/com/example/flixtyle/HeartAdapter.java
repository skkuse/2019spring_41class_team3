package com.example.flixtyle;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class HeartAdapter extends RecyclerView.Adapter<HeartAdapter.ViewHolder> {

    private ArrayList<HeartItem> items = new ArrayList<>();
    Context context;

    @NonNull
    @Override
    public HeartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.heart_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        context = parent.getContext();



        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HeartAdapter.ViewHolder viewHolder, int position) {

        HeartItem item = items.get(position);

        Glide.with(viewHolder.itemView.getContext())
                .load(item.getUrl())
                .into(viewHolder.ivHeart);

        viewHolder.tvName.setText(item.getItemName());
        final String itemUrl = item.getitemUrl();

        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri url= Uri.parse(itemUrl);
                //Intent intent= new Intent(v.getContext(), url);
                //v.getContext().startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<HeartItem> items) {
        this.items = items;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivHeart;
        TextView tvName;
        final View mView;

        ViewHolder(View itemView) {
            super(itemView);

            ivHeart = itemView.findViewById(R.id.image);

            tvName = itemView.findViewById(R.id.item_name);
            mView = itemView;
        }
    }
}
