package com.example.volunteerapp.adapters;

import static android.content.ContentValues.TAG;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



public class AdapterForEventTreatment extends BaseAdapter  {

    private String id;
    private List<ParseObject> list;
    private LayoutInflater inflater ;
    private Context context;

    public AdapterForEventTreatment (Context context, List<ParseObject> list, String id){
        this.context = context;
        this.list = list;
        this.id = id;
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

            v = inflater.inflate(R.layout.custom_lv_treatment_item, viewGroup, false);
        }
        TextView userName = v.findViewById(R.id.user_name_of_item);
        TextView userAge = v.findViewById(R.id.ageTextCustomItem);
        TextView userRating = v.findViewById(R.id.ratingTextCustomItem);
        TextView userPhone = v.findViewById(R.id.phoneTextCustomItem);
        Button acceptButton = v.findViewById(R.id.acceptButton_of_item);
        Button cancelButton = v.findViewById(R.id.cancelButton_of_item);

        userName.setText(getItem(i).getString("lastname")+ " "+ getItem(i).getString("firstname")+ " "+ getItem(i).getString("patronymic"));
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthday;
        try {
            birthday = myFormat.parse(getItem(i).getString("date_of_birthday"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Long time = new Date().getTime() / 1000 - birthday.getTime() / 1000;
        int years = Math.round(time) / 31536000;
        userAge.setText("Возраст: "+years);
        userRating.setText("Рейтинг: " + getItem(i).getInt("points"));
        userPhone.setText(getItem(i).getString("phone"));

        acceptButton.setOnClickListener(view1 -> {
            ParseQuery<ParseObject> parseV = ParseQuery.getQuery("Event");
            parseV.getInBackground(id, (event, e) -> {
                if (e == null) {
                    ParseRelation<ParseObject> relationVolunteers = event.getRelation("volunteers");
                    ParseRelation<ParseObject> relationApplication = event.getRelation("applications");
                    relationApplication.remove(getItem(i));
                    relationVolunteers.add(getItem(i));
                    event.saveInBackground(e1 -> {
                        if (e1 == null) {
                            Log.d(TAG, "sucess" ); //Заменить на всплывающее окно
                            remove(i);
                            notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "not sucess"+ e1);
                        }
                    });
                } else {
                    Log.d(TAG, "not sucess" );
                }
            });
        });
        cancelButton.setOnClickListener(view1 -> {
            ParseQuery<ParseObject> parseV = ParseQuery.getQuery("Event");
            parseV.getInBackground(id, (event, e) -> {
                if (e == null) {
                    ParseRelation<ParseObject> relationApplication = event.getRelation("applications");
                    relationApplication.remove(getItem(i));
                    event.saveInBackground(e1 -> {
                        if (e1 == null) {
                            Log.d(TAG, "sucess" );
                            remove(i);
                            notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "not sucess"+ e1);
                        }
                    });
                } else {
                    Log.d(TAG, "not sucess" );
                }
            });
        });
        return v;
    }
}
