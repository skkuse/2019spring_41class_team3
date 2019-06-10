package com.example.flixtyle;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

public class Flinger {

    public static class Item {
        private final String uid;
        private final String imageUrl;
        private final String itemName;
        private final String itemUrl;

        Item(String uid, String imageUrl, String itemName, String itemUrl) {
            this.uid = uid;
            this.imageUrl = imageUrl;
            this.itemName = itemName;
            this.itemUrl = itemUrl;
        }

        public String getUid() {
            return uid;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getItemName() {
            return itemName;
        }

        public String getItemUrl() {
            return itemUrl;
        }
    }

    private final SwipeFlingAdapterView swipeView;
    private final ArrayAdapter<Item> cardAdapter;
    private final List<Item> items;

    Flinger(final Context context, SwipeFlingAdapterView view) {
        this.swipeView = view;
        this.items = new ArrayList<>();
        this.cardAdapter = new ArrayAdapter<Item>(context, R.layout.item, items) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                Item item = getItem(position);
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
                }
                ImageView imageView = convertView.findViewById(R.id.image);
                Glide.with(context).load(item.getImageUrl()).into(imageView);
                return convertView;
            }
        };
        view.setAdapter(cardAdapter);
    }

    public void setFlingListener(SwipeFlingAdapterView.onFlingListener listener) {
        swipeView.setFlingListener(listener);
    }

    List<Item> getArray() {
        return items;
    }

    View getView() {
        return swipeView;
    }

    void notifyDataSetChanged() {
        cardAdapter.notifyDataSetChanged();
    }
}
