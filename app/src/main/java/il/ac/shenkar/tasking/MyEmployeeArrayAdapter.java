package il.ac.shenkar.tasking;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MyEmployeeArrayAdapter extends ArrayAdapter<Employee> {

    public MyEmployeeArrayAdapter(Context context, int resource, List<Employee> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Employee rowItem = getItem(position);
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setText(rowItem.getUsername());
        Typeface typeFace = Typeface.createFromAsset(getContext().getAssets(), "fonts/Raleway-Regular.ttf");
        view.setTypeface(typeFace);
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        Employee rowItem = getItem(position);
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setText(rowItem.getUsername());
        Typeface typeFace = Typeface.createFromAsset(getContext().getAssets(), "fonts/Raleway-Regular.ttf");
        view.setTypeface(typeFace);
        return view;
    }
}
