package com.example.flixtyle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;

public class arrayAdapter extends ArrayAdapter<cards> {
    Context context;

    public arrayAdapter(Context context, int resourceId, List<cards> items){
        super(context, resourceId, items);

    }
    public View getView(int position, View convertView, ViewGroup parent){
        cards cards_item = getItem(position);
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item,parent, false);

        }
        TextView name=(TextView)convertView.findViewById(R.id.name_item);
        ImageView image =(ImageView)convertView.findViewById(R.id.image);

        name.setText(cards_item.getName());
        image.setImageResource(R.mipmap.ic_launcher);



        return convertView;
    }

}
