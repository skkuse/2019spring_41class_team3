package com.example.flixtyle;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class RecommedationFragment extends Fragment {
    private DatabaseReference mPostReference;
    private FirebaseAuth mAuth;
    private ArrayList<HeartItem> recommendaion_list;
    private RecyclerView recyclerView;
    private HeartAdapter adapter;
    private String UID, itemUrl;


    private OnFragmentInteractionListener mListener;

    public RecommedationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recommedation, container, false);

        mPostReference = FirebaseDatabase.getInstance().getReference();
        recommendaion_list = new ArrayList<HeartItem>();
        recyclerView = view.findViewById(R.id.recycler_view);

        StaggeredGridLayoutManager staggeredGridLayoutManager
                = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        adapter = new HeartAdapter();
        mAuth= FirebaseAuth.getInstance();
        UID=mAuth.getCurrentUser().getUid();


        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setItemClick(new HeartAdapter.ItemClick() {
            @Override
            public void onClick(View view, int position) {

                //클릭시 실행될 함수 작성
            }
        });


        getFirebaseDatabase();
        return view;
    }

    public void getFirebaseDatabase() {
        final ValueEventListener postListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("onDataChange", "Data is Updated");
                recommendaion_list.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    DiscoveryFirebase get = postSnapshot.getValue(DiscoveryFirebase.class);
                    String[] info = {key, get.imageUrl, get.itemName, get.itemUrl};
                    String imageUrl = get.imageUrl;
                    String itemName = get.itemName;
                    itemUrl = get.itemUrl;
                    recommendaion_list.add(new HeartItem(imageUrl, itemName, itemUrl));
                    Log.d("getFirebaseDatabase", "key: " + key);
                    Log.d("getFirebaseDatabase", "info: " + info[0] + info[1] + info[2] + info[3]);
                }
                adapter.setItems(recommendaion_list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.child("Users").child(UID).child("recommendations").addValueEventListener(postListener);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
