package com.example.group3;

import android.app.Activity;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class CustomListView extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList maintitle;
    private final ArrayList subtitle;
    private final ArrayList imgid;
    private final ArrayList usernameArraylist;
    private final ArrayList timestamp;


    public CustomListView(ExploreActivity context,
                          ArrayList<String> maintitle,
                          ArrayList<String> subtitle,
                          ArrayList<Bitmap> imgid,
                          ArrayList<String> usernameArraylist,
                          ArrayList<String> timestamp) {
        super(context, R.layout.customlist, maintitle);

        this.context=context;
        this.maintitle=maintitle;
        this.subtitle=subtitle;
        this.imgid=imgid;
        this.usernameArraylist=usernameArraylist;
        this.timestamp=timestamp;

    }



    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.customlist, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.image);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.description);
        TextView postersUsername = (TextView) rowView.findViewById(R.id.postersUsername);
        TextView timestampTextView = (TextView) rowView.findViewById(R.id.timestamp);

        titleText.setText((CharSequence) maintitle.get(position));
        imageView.setImageBitmap((Bitmap) imgid.get(position));
        subtitleText.setText((CharSequence) subtitle.get(position));
        postersUsername.setText((CharSequence) usernameArraylist.get(position));
        timestampTextView.setText((CharSequence) timestamp.get(position));


        return rowView;

    };
}
