package com.example.volunteerapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.volunteerapp.MoreAboutEventActivity;
import com.example.volunteerapp.R;
import com.example.volunteerapp.VolunteerEventsActivity;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.text.SimpleDateFormat;
import java.util.List;

public class VolunteerAdapterEvents extends BaseAdapter {

    private List<ParseObject> list;

    private Context context;
    private LayoutInflater lInflater;

    public VolunteerAdapterEvents(Context context, List<ParseObject> list){
        this.context = context;
        this.list = list;
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ParseObject getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null) {
            v = lInflater.inflate(R.layout.custom_item_for_events, viewGroup, false);
        }

        TextView title = v.findViewById(R.id.titleOfEvent);
        TextView date = v.findViewById(R.id.dateOfEvent);
        Button more = v.findViewById(R.id.buttonMoreAboutEvent);

        more.setOnClickListener(view1 -> {
            Intent intent = new Intent(view1.getContext(), MoreAboutEventActivity.class);
            intent.putExtra("event", getItem(i));
            view1.getContext().startActivity(intent);
        });

        ImageView img = v.findViewById(R.id.imageOfEvent);
        title.setText(getItem(i).getString("title"));
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        date.setText(format.format(getItem(i).getDate("date")));

        return v;
    }
}
