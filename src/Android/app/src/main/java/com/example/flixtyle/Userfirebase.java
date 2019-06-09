package com.example.flixtyle;

import java.util.HashMap;
import java.util.Map;


public class Userfirebase {
    public String user_name;
    public String user_email;
    public String UID;
    public String user_birth;
    public String user_gender;
    public String user_city;
    public String user_country;


    public Userfirebase(){
            // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }
    public Userfirebase(String UID, String user_email, String user_name, String user_birth, String user_gender, String user_country, String user_city) {
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
