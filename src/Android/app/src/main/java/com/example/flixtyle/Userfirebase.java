package com.example.flixtyle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class Userfirebase {

    interface UserReadyListener {
        void onUserReady(User user);
    }

    public static class User {
        private User instance = null;

        private String UID;
        private String user_name = null;
        private String user_email = null;
        private Long user_birth = null;
        private String user_gender = null;
        private String user_city = null;
        private String user_country = null;
        private Set<String> heart_list = null;

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(UID)
                    .child("user_name")
                    .setValue(user_name);
        }

        public String getUser_email() {
            return user_email;
        }

        public void setUser_email(String user_email) {
            FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(UID)
                    .child("user_email")
                    .setValue(user_email);
        }

        public String getUID() {
            return UID;
        }

        public Long getUser_birth() {
            return user_birth;
        }

        public void setUser_birth(Integer user_birth) {
            FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(UID)
                    .child("user_birth")
                    .setValue(user_birth);
        }

        public String getUser_gender() {
            return user_gender;
        }

        public void setUser_gender(String user_gender) {
            FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(UID)
                    .child("user_gender")
                    .setValue(user_gender);
        }

        public String getUser_city() {
            return user_city;
        }

        public void setUser_city(String user_city) {
            FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(UID)
                    .child("user_city")
                    .setValue(user_city);
        }

        public String getUser_country() {
            return user_country;
        }

        public void setUser_country(String user_country) {
            FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(UID)
                    .child("user_country")
                    .setValue(user_country);
        }

        public Set<String> getHeart_list() {
            return heart_list;
        }

        public void addHeart_list(String itemUID) {
            FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(UID)
                    .child("heart_list")
                    .child(itemUID)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            dataSnapshot.getRef().setValue(true);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }

        public void removeHeart_list(String itemUID) {
            FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(UID)
                    .child("heart_list")
                    .child(itemUID)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            dataSnapshot.getRef().removeValue();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }

        private void updateField(String key, Object value) {
            switch (key) {
                case "user_name":
                    user_name = (String) value;
                    break;
                case "user_email":
                    user_email = (String) value;
                    break;
                case "user_birth":
                    user_birth = (Long) value;
                    break;
                case "user_gender":
                    user_gender = (String) value;
                    break;
                case "user_city":
                    user_city = (String) value;
                    break;
                case "user_country":
                    user_country = (String) value;
                    break;
                case "heart_list":
                    Map<String, Boolean> hl = (Map<String, Boolean>) value;
                    heart_list = hl == null? null: hl.keySet();
                    break;
            }
        }

        public User(final boolean once, final UserReadyListener listener) {
            UID = FirebaseAuth.getInstance().getUid();
            final DatabaseReference userReference = FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(FirebaseAuth.getInstance().getUid());
            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        Map<String, Object> values = (Map<String, Object>) dataSnapshot.getValue();
                        for (String k: values.keySet()) {
                            updateField(k, values.get(k));
                        }
                    }
                    listener.onUserReady(User.this);
                    if (once) {
                        userReference.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                updateField(dataSnapshot.getKey(), dataSnapshot.getValue());
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                updateField(dataSnapshot.getKey(), dataSnapshot.getValue());
                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                                updateField(dataSnapshot.getKey(), null);
                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public String user_name;
    public String user_email;
    public String UID;
    public long user_birth;
    public String user_gender;
    public String user_city;
    public String user_country;


    public Userfirebase(){
            // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }
    public Userfirebase(String UID, String user_email, String user_name, long user_birth, String user_gender, String user_country, String user_city) {
        this.UID = UID;
        this.user_email = user_email;
        this.user_name = user_name;
        this.user_birth = user_birth;
        this.user_gender = user_gender;
        this.user_country = user_country;
        this.user_city = user_city;

    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("UID", UID);
        result.put("user_email", user_email);
        result.put("user_name", user_name);
        result.put("user_birth", user_birth);
        result.put("user_gender", user_gender);
        result.put("user_country", user_country);
        result.put("user_city", user_city);

        return result;
    }
}
