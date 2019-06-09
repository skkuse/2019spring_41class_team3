package com.example.flixtyle;

import java.util.HashMap;
import java.util.Map;

public class DiscoveryFirebase {

    public String image_url;
    public String item_name;
    public String item_url;

    public DiscoveryFirebase(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public DiscoveryFirebase(String image_url, String item_name, String item_url) {


        this.image_url = image_url;
        this.item_name = item_name;
        this.item_url = item_url;
    }


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("image_url", image_url);
        result.put("item_name", item_name);
        result.put("item_url", item_url);
        return result;
    }
}