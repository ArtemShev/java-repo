package com.example.volunteerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.volunteerapp.model.User;
import com.parse.ParseObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;


public class AdapterForEventTreatment extends BaseAdapter  {


//    private ArrayList<User> list ;
    private List<ParseObject> list;
    private LayoutInflater inflater ;
    private Context context;

    public AdapterForEventTreatment (Context context, List<ParseObject> list){
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;

    }

    @Override
    public ParseObject getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v = view;

        if (v == null) {

            v = inflater.inflate(R.layout.custom_lv_treatment_item, viewGroup, false);
        }
        TextView userName = v.findViewById(R.id.user_name_of_item);
        Button acceptButton = v.findViewById(R.id.acceptButton_of_item);
        Button cancelButton = v.findViewById(R.id.cancelButton_of_item);
        userName.setText(getItem(i).getString("EventName"));
//        userName.setText(getItem(i));
        return v;
    }





}
