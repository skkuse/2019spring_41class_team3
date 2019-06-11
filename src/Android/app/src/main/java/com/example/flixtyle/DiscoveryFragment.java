package com.example.flixtyle;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;

import java.util.List;


public class DiscoveryFragment extends Fragment {

    private DatabaseReference mPostReference;
    private FirebaseAuth mAuth;

    private ImageView iv;
    private String UID;
    private int i, j;
    private String image_url, item_name, item_url;

    //connect to main activity
    private cards cards_data[];

    private arrayAdapter arrayAdapter;


    private String currentUId;
    private DatabaseReference userDb;

    ListView listView;
    List<cards> rowItems;


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
        mAuth = FirebaseAuth.getInstance();

        checkUserSex();

        final Flinger flinger = new Flinger(getContext(), (SwipeFlingAdapterView) view.findViewById(R.id.frame));
        flinger.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                flinger.getArray().remove(0);
                flinger.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                Toast.makeText(contextRegister, "left", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Toast.makeText(contextRegister, "right", Toast.LENGTH_SHORT).show();
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

        mAuth.getAccessToken(true).addOnCompleteListener(
                new OnCompleteListener<GetTokenResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<GetTokenResult> task) {
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    if (task.isSuccessful()) {
                                        HttpURLConnection connection
                                                = (HttpURLConnection) new URL(
                                                        getString(R.string.server_address)
                                                                + "/discovery/"
                                                                + mAuth.getUid()).openConnection();
                                        connection.setRequestMethod("GET");
                                        connection.setRequestProperty("Authorization", "Bearer " + task.getResult().getToken());
                                        JsonObject root
                                                = new JsonParser().parse(new InputStreamReader(connection.getInputStream()))
                                                .getAsJsonObject();
                                        for (String key: root.keySet()) {
                                            JsonObject o = root.get(key).getAsJsonObject();
                                            flinger.getArray().add(new Flinger.Item(
                                                    key,
                                                    o.get("image_url").getAsString(),
                                                    o.get("item_name").getAsString(),
                                                    o.get("item_url").getAsString()
                                            ));
                                        }
                                        DiscoveryFragment.this.getActivity().runOnUiThread(
                                                new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        flinger.notifyDataSetChanged();
                                                    }
                                                }
                                        );
                                    } else {
                                        task.getException().printStackTrace();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    DiscoveryFragment.this.getActivity().runOnUiThread(
                                            new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(contextRegister, "Failed to connect to the server.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                    );
                                }
                            }
                        }.start();
                    }
                }
        );


/*
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
        */
        return view;
    }

    private String userSex;
    private String oppositeUserSex;

    public void checkUserSex() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference femaleDb = FirebaseDatabase.getInstance().getReference().child("Users").child("UID").child("Female");
        femaleDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getKey().equals(user.getUid())) {
                    userSex = "Female";
                    oppositeUserSex = "Male";
                    getClothing();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        DatabaseReference maleDb = FirebaseDatabase.getInstance().getReference().child("Users").child("UID").child("Male");
        maleDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getKey().equals(user.getUid())) {
                    userSex = "Male";
                    oppositeUserSex = "Female";
                    getClothing();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //get clothing depending on sex
    //important
    public void getClothing() {
        DatabaseReference clothingDb = FirebaseDatabase.getInstance().getReference().child("Users").child("UID").child(userSex);
        clothingDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    //cards item=new cards(dataSnapshot.getKey(),dataSnapshot.child("name").getValue().toString());
                    //rowItems.add(items)
                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}




/*
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

*/













