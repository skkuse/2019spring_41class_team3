package com.example.flixtyle;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class RecommedationFragment extends Fragment {
    private DatabaseReference mPostReference;
    private FirebaseAuth mAuth;
    private ArrayList<HeartItem> heart_list;
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
        heart_list = new ArrayList<HeartItem>();
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



        return view;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
