package com.example.flixtyle;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    private ArrayList<String[]> heart_list;
    private ArrayAdapter<String[]> arrayAdapter;
    private String UID;
    private ImageView iv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_list);

        mPostReference = FirebaseDatabase.getInstance().getReference();
        mAuth= FirebaseAuth.getInstance();
        UID=mAuth.getCurrentUser().getUid();
        ImageView iv = (ImageView)findViewById(R.id.image);
        heart_list = new ArrayList<String[]>();
        arrayAdapter = new ArrayAdapter<String[]>(this, R.layout.item, R.id.name_item, heart_list);
        getFirebaseDatabase();

    }


    public void getFirebaseDatabase() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("onDataChange", "Data is Updated");
                heart_list.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    DiscoveryFirebase get = postSnapshot.getValue(DiscoveryFirebase.class);
                    String[] info = {key, get.image_url, get.item_name, get.item_url};
                    heart_list.add(new String[]{key, get.image_url, get.item_name, get.item_url});
                    Log.d("getFirebaseDatabase", "key: " + key);
                    Log.d("getFirebaseDatabase", "info: " + info[0] + info[1] + info[2] + info[3]);
                }
                arrayAdapter.clear();
                arrayAdapter.addAll(heart_list);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.child(UID).child("heart_list").addValueEventListener(postListener);
    }

}
