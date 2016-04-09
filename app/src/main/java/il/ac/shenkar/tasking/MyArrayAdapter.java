package il.ac.shenkar.tasking;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyArrayAdapter<String> extends ArrayAdapter<String> {

    public MyArrayAdapter(Context context, int resource, String[] objects) {
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
