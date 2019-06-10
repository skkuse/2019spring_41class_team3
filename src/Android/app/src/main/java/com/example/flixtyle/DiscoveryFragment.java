package com.example.flixtyle;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DiscoveryFragment extends Fragment {

    private DatabaseReference mPostReference;
    private FirebaseAuth mAuth;
    private ArrayList<String[]> al;
    private ArrayAdapter<String[]> arrayAdapter;
    private ImageView iv;
    private String UID;
    private int i, j;
    private  String image_url, item_name, item_url;

    public DiscoveryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Context contextRegister = container.getContext();
        View view = inflater.inflate(R.layout.activity_discovery, container, false);

        mPostReference = FirebaseDatabase.getInstance().getReference();
        mAuth= FirebaseAuth.getInstance();
        UID=mAuth.getCurrentUser().getUid();
        ImageView iv = (ImageView)view.findViewById(R.id.image);
        al = new ArrayList<String[]>();
        getFirebaseDatabase();

        arrayAdapter = new ArrayAdapter<String[]>(getContext(), R.layout.item, R.id.name_item, al);

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView)view.findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                al.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                Toast.makeText(contextRegister, "left", Toast.LENGTH_SHORT).show();
                j++;
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Toast.makeText(contextRegister, "right", Toast.LENGTH_SHORT).show();
                j++;
            }


            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                //al.add("XML ".concat(String.valueOf(i)));
                //arrayAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
            }

            @Override
            public void onScroll(float scrollProgressPercent) {


            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(contextRegister, "Heart!", Toast.LENGTH_SHORT).show();
                Map<String, Object> childUpdates = new HashMap<>();
                Map<String, Object> postValues = null;

                image_url = al.get(j)[1];
                item_name = al.get(j)[2];
                item_url = al.get(j)[3];

                DiscoveryFirebase post = new DiscoveryFirebase(image_url, item_name, item_url);
                postValues = post.toMap();

                String order_id = al.get(j)[0];


                childUpdates.put("/User/" + UID + "/heart_list/"+order_id, postValues);
                mPostReference.updateChildren(childUpdates);


            }
        });
        return view;
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }





    public void getFirebaseDatabase() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("onDataChange", "Data is Updated");
                al.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    DiscoveryFirebase get = postSnapshot.getValue(DiscoveryFirebase.class);
                    String[] info = {key, get.imageUrl, get.itemName, get.itemUrl};
                    al.add(new String[]{key, get.imageUrl, get.itemName, get.itemUrl});
                    Log.d("getFirebaseDatabase", "key: " + key);
                    Log.d("getFirebaseDatabase", "info: " + info[0] + info[1] + info[2] + info[3]);
                }
                arrayAdapter.clear();
                arrayAdapter.addAll(al);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.child("items").addValueEventListener(postListener);
    }

}





