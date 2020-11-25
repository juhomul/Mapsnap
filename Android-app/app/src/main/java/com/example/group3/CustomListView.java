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

public class CustomListView extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList maintitle;
    private final ArrayList subtitle;
    private final ArrayList imgid;


    public CustomListView(ExploreActivity context, ArrayList<String> maintitle, ArrayList<String> subtitle, ArrayList<Bitmap> imgid) {
        super(context, R.layout.customlist, maintitle);

        this.context=context;
        this.maintitle=maintitle;
        this.subtitle=subtitle;
        this.imgid=imgid;

    }



    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.customlist, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.image);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.description);

        titleText.setText((CharSequence) maintitle.get(position));
        imageView.setImageBitmap((Bitmap) imgid.get(position));
        subtitleText.setText((CharSequence) subtitle.get(position));

        return rowView;

    };
}
