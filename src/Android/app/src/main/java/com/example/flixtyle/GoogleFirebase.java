package com.example.flixtyle;

import java.util.HashMap;
import java.util.Map;

public class GoogleFirebase {
    public String user_email;
    public String UID;
    public GoogleFirebase(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }
    public GoogleFirebase(String email, String UID) {
        this.user_email = email;
        this.UID = UID;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("user_email", user_email);
        result.put("UID", UID);
        return result;
    }
}