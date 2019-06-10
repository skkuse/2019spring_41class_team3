package com.example.flixtyle;

public class HeartItem {

    private String url;
    private String item_name;
    private String item_url;

    public HeartItem(String url, String item_name, String item_url) {

        this.url = url;
        this.item_name = item_name;
        this.item_url = item_url;

    }

    public String getUrl() {
        return url;
    }

    public String getItemName() {
        return item_name;
    }

    public String getitemUrl() {
        return item_url;
    }
}
