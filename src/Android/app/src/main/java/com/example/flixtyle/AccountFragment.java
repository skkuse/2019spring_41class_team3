package com.example.flixtyle;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AccountFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private DatabaseReference mPostReference;
    private FirebaseAuth mAuth;


    private Button HeartButton;
    private Button ChangeButton;
    private String UID;
    private TextView TextName;
    private TextView TextGender;
    private TextView TextBirth;
    private TextView TextCity;
    private TextView TextCountry;


    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);
        // Inflate the layout for this fragment
        mAuth= FirebaseAuth.getInstance();
        UID=mAuth.getCurrentUser().getUid();
        mPostReference = FirebaseDatabase.getInstance().getReference();
        TextName = (TextView)view.findViewById(R.id.textName);
        TextGender = (TextView)view.findViewById(R.id.textGender);
        TextBirth = (TextView)view.findViewById(R.id.textBirth);
        TextCity = (TextView)view.findViewById(R.id.textCity);
        TextCountry = (TextView)view.findViewById(R.id.textCountry);
        HeartButton = (Button) view.findViewById(R.id.HeartButton);
        ChangeButton = (Button) view.findViewById(R.id.ChangeButton);


        getUserFirebaseDatabase();

        return view;
    }
    public void getUserFirebaseDatabase() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("onDataChange", "Data is Updated");

                Userfirebase get = dataSnapshot.getValue(Userfirebase.class);
                String[] info = {get.user_email, get.user_name, get.user_birth, get.user_gender, get.user_country, get.user_city};

                TextName.setText("Name: "+info[1]);
                TextGender.setText("Gender: "+info[2]);
                TextBirth.setText("Birth Year: "+info[3]);
                TextCountry.setText("Country: "+info[4]);
                TextCity.setText("City: "+info[5]);

                Log.d("getFirebaseDatabase", "key: " + info[0]);
                Log.d("getFirebaseDatabase", "info: " + info[1] + info[2] + info[3] + info[4]);

                HeartButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent1 = new Intent(getActivity(), HeartList.class);
                        startActivity(intent1);


                    }
                });

                ChangeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent2 = new Intent(getActivity(), InfoChangeActivity.class);
                        startActivity(intent2);


                    }
                });


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.child("Users").child(UID).addValueEventListener(postListener);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
