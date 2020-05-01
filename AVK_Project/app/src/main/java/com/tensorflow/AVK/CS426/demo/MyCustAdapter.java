package com.tensorflow.AVK.CS426.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.tensorflow.lite.examples.demo.R;

import java.util.ArrayList;

public class MyCustAdapter extends BaseAdapter {

    Context context;
    ArrayList<HydrantInfo> arr;

    public MyCustAdapter(Context context, ArrayList<HydrantInfo> arr) {
        this.context = context;
        this.arr = arr;
    }

    @Override
    public int getCount() {
        return arr.size();
    }

    @Override
    public Object getItem(int i) {
        return arr.get(i).title;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.rowdesign, viewGroup, false);
        TextView title = view.findViewById(R.id.textView6);
        TextView description = view.findViewById(R.id.textView7);
        title.setText(arr.get(i).getTitle());
        description.setText(arr.get(i).getDescription());

        return view;
    }
}
