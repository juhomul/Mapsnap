package com.example.group3;

import android.app.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class CustomListView extends ArrayAdapter<ListViewItem> {
    private final Activity context;

    public CustomListView(Context context,
                          ArrayList<ListViewItem> listViewItems) {
        super(context, R.layout.customlist, listViewItems);

        this.context= (Activity) getContext();
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.customlist, null,true);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.image);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.description);
        TextView postersUsername = (TextView) rowView.findViewById(R.id.postersUsername);
        TextView timestampTextView = (TextView) rowView.findViewById(R.id.timestamp);

        ListViewItem l = getItem(position);

        imageView.setImageBitmap((Bitmap) l.getImgid());
        subtitleText.setText((CharSequence) l.getSubtitle());
        postersUsername.setText((CharSequence) l.getUsernameArrayList());
        timestampTextView.setText((CharSequence) l.getTimestamp());

        //titleText.setText((CharSequence) maintitle.get(getCountMaintitle() - position - 1));
        /*imageView.setImageBitmap((Bitmap) imgid.get(getCountImgid() - position - 1));
        subtitleText.setText((CharSequence) subtitle.get(getCountSubtitle() - position - 1));
        postersUsername.setText((CharSequence) usernameArraylist.get(getCountUsernameArraylist() - position - 1));
        timestampTextView.setText((CharSequence) timestamp.get(getCountTimestamp() - position - 1));*/

        return rowView;

    };

}
