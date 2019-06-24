package com.example.flixtyle;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HeartList extends AppCompatActivity {
    private DatabaseReference mPostReference;
    private FirebaseAuth mAuth;
    private ArrayList<HeartItem> heart_list;
    private RecyclerView recyclerView;
    private HeartAdapter adapter;
    private String UID, itemUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_list);

        mPostReference = FirebaseDatabase.getInstance().getReference();
        heart_list = new ArrayList<HeartItem>();
        recyclerView = findViewById(R.id.recycler_view);

        StaggeredGridLayoutManager staggeredGridLayoutManager
                = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        adapter = new HeartAdapter();
        mAuth= FirebaseAuth.getInstance();
        UID=mAuth.getCurrentUser().getUid();


        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(adapter);



        //아이템 로드
        getFirebaseDatabase();

    }

//

    public void getFirebaseDatabase() {
        final ValueEventListener postListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("onDataChange", "Data is Updated");
                heart_list.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    DiscoveryFirebase get = postSnapshot.getValue(DiscoveryFirebase.class);
                    String[] info = {key, get.imageUrl, get.itemName, get.itemUrl};
                    String imageUrl = get.imageUrl;
                    String itemName = get.itemName;
                    itemUrl = get.itemUrl;
                    heart_list.add(new HeartItem(imageUrl, itemName, itemUrl));
                    Log.d("getFirebaseDatabase", "key: " + key);
                    Log.d("getFirebaseDatabase", "info: " + info[0] + info[1] + info[2] + info[3]);
                }
                adapter.setItems(heart_list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.child("Users").child(UID).child("heart_list").addValueEventListener(postListener);
    }

}
