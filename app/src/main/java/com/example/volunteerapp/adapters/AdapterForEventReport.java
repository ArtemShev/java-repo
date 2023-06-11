package com.example.volunteerapp.adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.volunteerapp.R;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.List;

public class AdapterForEventReport extends BaseAdapter {
    private List<ParseObject> list;
    private LayoutInflater inflater ;
    private Context context;

    public AdapterForEventReport (Context context, List<ParseObject> list){
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public void remove(int position){
        list.remove(list.get(position));
    }
    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public ParseObject getItem(int pos) {
        return list.get(pos);
    }


    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null) {
            v = inflater.inflate(R.layout.custom_lv_report_item, viewGroup, false);
        }



        return v;
    }
}