package com.tasking.tasking;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter
        .ViewHolder> {

    ArrayList<String> listItems;

    public  MyRecyclerAdapter(ArrayList<String> listItems){
        this.listItems = listItems;
    }

    @Override
    public MyRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyRecyclerAdapter.ViewHolder holder, int position) {
        String item = listItems.get(position);
        holder.textView.setText(item);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private Button button;

        public ViewHolder(View parentView) {
            super(parentView);
            textView = (TextView) parentView.findViewById(R.id.list_item_text);
            button = (Button) parentView.findViewById(R.id.list_item_button);
        }
    }
}
