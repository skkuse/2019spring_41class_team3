package com.example.flixtyle;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class HeartAdapter extends RecyclerView.Adapter<HeartAdapter.ViewHolder> {

    private ArrayList<HeartItem> items = new ArrayList<>();
    Context context;

    private ItemClick itemClick;
    public interface ItemClick {
        public void onClick(View view,int position);
    }

    //아이템 클릭시 실행 함수 등록 함수
    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public HeartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.heart_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        context = parent.getContext();



        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HeartAdapter.ViewHolder viewHolder, final int position) {

        HeartItem item = items.get(position);

        Glide.with(viewHolder.itemView.getContext())
                .load(item.getUrl())
                .into(viewHolder.ivHeart);

        viewHolder.tvName.setText(item.getItemName());
        final String itemUrl =item.getitemUrl();
        final ImageButton heartButton = viewHolder.heartButton;
        heartButton.setOnClickListener(new View.OnClickListener() {
            int i= 1;
            @Override
            public void onClick(View v) {
                if(itemClick != null){
                    itemClick.onClick(v, position);
                    if(i%2==1)
                    {heartButton.setImageResource(R.drawable.heart_after);
                        i++;}
                    else
                    { heartButton.setImageResource(R.drawable.heart_before);
                        i--;  }
                    Toast.makeText(v.getContext(),"heart", Toast.LENGTH_LONG).show();
                }
            }
        });
        ImageView ivHeart = viewHolder.ivHeart;
        ivHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClick != null){
                    itemClick.onClick(v, position);
                    Uri url= Uri.parse(itemUrl);
                    Intent intent= new Intent(Intent.ACTION_VIEW, url);
                    if (intent.resolveActivity(v.getContext().getPackageManager()) != null){
                        v.getContext().startActivity(intent);
                    }
                    Toast.makeText(v.getContext(),"image", Toast.LENGTH_LONG).show();
                }
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
        ImageButton heartButton;
        ImageView ivHeart;
        TextView tvName;
        final View mView;

        ViewHolder(View itemView) {
            super(itemView);

            ivHeart = itemView.findViewById(R.id.image);
            tvName = itemView.findViewById(R.id.item_name);
            heartButton = itemView.findViewById(R.id.heartbutton);
            mView = itemView;
        }
    }
}
