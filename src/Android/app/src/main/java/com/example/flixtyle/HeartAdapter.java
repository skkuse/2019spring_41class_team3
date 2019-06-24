package com.example.flixtyle;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HeartAdapter extends RecyclerView.Adapter<HeartAdapter.ViewHolder> {
    Context context;
    private DatabaseReference mPostReference;
    private FirebaseAuth mAuth;
    private ArrayList<HeartItem> items = new ArrayList<>();
    private ArrayList<String[]> list = new ArrayList<>();
    private Random mRandom = new Random();
    private String UID, Heart_key;
    private Integer i = 0, k = 0;
    private ItemClick itemClick;

    public interface ItemClick {
        public void onClick(View view, int position);
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

        double mRandom = Math.random();

        final HeartItem item = items.get(position);

        Glide.with(viewHolder.itemView.getContext())
                .load(item.getUrl())
                .into(viewHolder.ivHeart);

        viewHolder.itemView.getLayoutParams().height = getRandomIntInRange(400, 350);
        viewHolder.tvName.setText(item.getItemName());


        mPostReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        UID = mAuth.getCurrentUser().getUid();
        i = 0;
        getFirebaseDatabase();
        final ImageButton heartButton = viewHolder.heartButton;
        for (int j = 0; j < list.size(); j++) {
            if (item.getUrl().equals(list.get(j)[1])) {
                i = 1;
                Heart_key = list.get(j)[0];
            }
        }
        if (HeartList.class.equals(context.getClass())) {
            heartButton.setImageResource(R.drawable.heart_after);
            heartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClick != null) {
                    itemClick.onClick(v, position);
                        Map<String, Object> childUpdates = new HashMap<>();
                        Map<String, Object> postValues = null;
                        DiscoveryFirebase post = new DiscoveryFirebase(null, null, null);
                        postValues = post.toMap();
                        childUpdates.put("/Users/" + UID + "/heart_list/" + Heart_key, postValues);
                        mPostReference.updateChildren(childUpdates);
                        Toast.makeText(v.getContext(), "unheart", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        if (MainActivity.class.equals(context.getClass())) {
            heartButton.setImageResource(R.drawable.heart_before);
            heartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClick != null) {
                        itemClick.onClick(v, position);
                            Map<String, Object> childUpdates = new HashMap<>();
                            Map<String, Object> postValues = null;
                            DiscoveryFirebase post = new DiscoveryFirebase(item.getUrl(), item.getItemName(), item.getitemUrl());
                            postValues = post.toMap();
                            String s = mPostReference.push().getKey();
                            childUpdates.put("/Users/" + UID + "/heart_list/" + s, postValues);
                            mPostReference.updateChildren(childUpdates);

                            Toast.makeText(v.getContext(), "heart", Toast.LENGTH_LONG).show();

                            }
                        }

                    });
        }



        final String itemUrl =item.getitemUrl();
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


    public void getFirebaseDatabase() {
        final ValueEventListener postListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                Log.d("onDataChange", "Data is Updated");
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    DiscoveryFirebase get = postSnapshot.getValue(DiscoveryFirebase.class);
                    String key = postSnapshot.getKey();
                    list.add(new String[]{key, get.imageUrl, get.itemName, get.itemUrl});
                    Log.d("getFirebaseDatabase", "key: " + key);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.child("Users").child(UID).child("heart_list").addValueEventListener(postListener);
    }



    protected int getRandomIntInRange(int max, int min){
        return mRandom.nextInt((max-min)+min)+min;
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
