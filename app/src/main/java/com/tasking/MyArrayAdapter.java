package com.tasking;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Grisha on 3/26/2016.
 */
public class MyArrayAdapter<Object> extends ArrayAdapter<Object> {

    public MyArrayAdapter(Context context, int resource, List<Object> objects) {
        super(context, resource, objects);
    }

    public MyArrayAdapter(Context context, int resource, Object[] objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        Typeface typeFace = Typeface.createFromAsset(getContext().getAssets(), "fonts/Raleway-Regular.ttf");
        view.setTypeface(typeFace);
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        Typeface typeFace = Typeface.createFromAsset(getContext().getAssets(), "fonts/Raleway-Regular.ttf");
        view.setTypeface(typeFace);
        return view;
    }
}
