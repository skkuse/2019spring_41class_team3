package com.example.flixtyle;

import java.util.HashMap;
import java.util.Map;

public class DiscoveryFirebase {

    public String imageUrl;
    public String itemName;
    public String itemUrl;

    public DiscoveryFirebase(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public DiscoveryFirebase(String imageUrl, String itemName, String itemUrl) {


        this.imageUrl = imageUrl;
        this.itemName = itemName;
        this.itemUrl = itemUrl;
    }


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("imageUrl", imageUrl);
        result.put("itemName", itemName);
        result.put("itemUrl", itemUrl);
        return result;
    }
}