package com.example.flixtyle;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
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

    EditText userName;
    RadioButton maleButton;
    RadioButton femaleButton;
    EditText userBirth;
    EditText userCountry;
    EditText userCity;


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
        HeartButton = (Button) view.findViewById(R.id.HeartButton);
        ChangeButton = (Button) view.findViewById(R.id.ChangeButton);

        userName = view.findViewById(R.id.textName);
        maleButton = view.findViewById(R.id.buttonMale);
        femaleButton = view.findViewById(R.id.buttonFemale);
        userBirth = view.findViewById(R.id.textBirth);
        userCountry = view.findViewById(R.id.textCountry);
        userCity = view.findViewById(R.id.textCity);
        final Userfirebase.User user = new Userfirebase.User(true, new Userfirebase.UserReadyListener() {
            @Override
            public void onUserReady(final Userfirebase.User user) {
                new Userfirebase.User(false, new Userfirebase.UserReadyListener() {
                    @Override
                    public void onUserReady(Userfirebase.User user) {
                        refreshFields(user);
                    }
                });
                userName.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        user.setUser_name(userName.getText().toString());
                    }
                });
                userBirth.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        user.setUser_birth(Integer.valueOf(userBirth.getText().toString()));
                    }
                });
                userCountry.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        user.setUser_country(userCountry.getText().toString());
                    }
                });
                userCity.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        user.setUser_city(userCity.getText().toString());
                    }
                });
                maleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            user.setUser_gender("Male");
                        }
                    }
                });
                femaleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            user.setUser_gender("Female");
                        }
                    }
                });
            }
        });

        HeartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), HeartList.class);
                startActivity(intent1);
            }
        });

//        getUserFirebaseDatabase();

        return view;
    }

    void refreshFields(Userfirebase.User user) {
        if (user.getUser_name() != null) {
            userName.setText(user.getUser_name());
        }
        femaleButton.setChecked(false);
        maleButton.setChecked(false);
        if (user.getUser_gender() == null) {
        } else if (user.getUser_gender().equals("Male")) {
            maleButton.setChecked(true);
        } else if (user.getUser_gender().equals("Female")) {
            femaleButton.setChecked(true);
        }
        if (user.getUser_birth() != null) {
            userBirth.setText(String.valueOf(user.getUser_birth()));
        }
        if (user.getUser_country() != null) {
            userCity.setText(user.getUser_country());
        }
        if (user.getUser_city() != null) {
            userCountry.setText(user.getUser_city());
        }
    }

    public void getUserFirebaseDatabase() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("onDataChange", "Data is Updated");

                Userfirebase get = dataSnapshot.getValue(Userfirebase.class);
                String[] info = {get.user_email, get.user_name, get.user_birth, get.user_gender, get.user_country, get.user_city};



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

    public void logoutUser(View view) {
        mAuth.signOut();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
