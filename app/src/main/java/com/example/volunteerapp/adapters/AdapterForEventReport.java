package com.example.volunteerapp.adapters;


import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.volunteerapp.R;
import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdapterForEventReport extends BaseAdapter {
    private List<ParseObject> list;
    private LayoutInflater inflater ;
    private Context context;
    private ProgressDialog progressDialog;
    private String id;
    private ParseObject event;
    private ParseObject userPoints;

    public AdapterForEventReport (Context context, List<ParseObject> list, String id){
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
            v = inflater.inflate(R.layout.custom_lv_report_item, viewGroup, false);
        }
        TextView userName = v.findViewById(R.id.reportUserName);
        TextView userAge = v.findViewById(R.id.reportUserAge);
        TextView userRating = v.findViewById(R.id.reportUserRating);
        TextView userPhone = v.findViewById(R.id.reportUserPhone);


        ParseQuery<ParseObject> evQuery = new ParseQuery<>("Event");
        evQuery.getInBackground(id, (object, e) -> {
            event = object;
        });


        ParseQuery<ParseObject> userRatingQuery = new ParseQuery<>("Rating");
        userRatingQuery.whereEqualTo("user", getItem(i));
        try {
            userPoints = userRatingQuery.getFirst();
            userRating.setText("Рейтинг: " + userPoints.getInt("points"));
        } catch (com.parse.ParseException e) {
            throw new RuntimeException(e);
        }



        userName.setText(getItem(i).getString("lastname")+" "+getItem(i).getString("firstname")+" "+getItem(i).getString("patronymic"));
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date birthday = myFormat.parse(getItem(i).getString("date_of_birthday"));
            Long time = new Date().getTime() / 1000 - birthday.getTime() / 1000;
            int years = Math.round(time) / 31536000;
            userAge.setText("Возраст: "+years);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        userPhone.setText(getItem(i).getString("phone"));

        Spinner spinnerPoints = v.findViewById(R.id.spinnerPoints);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(v.getContext(), R.array.points, android.R.layout.simple_spinner_dropdown_item);
        spinnerPoints.setAdapter(spinnerAdapter);
        Button rateButton = v.findViewById(R.id.reportRateButton);

        rateButton.setOnClickListener(view1 -> {
            progressDialog = new ProgressDialog(view1.getContext());
            progressDialog.setMessage("Загрузка");
            progressDialog.setCancelable(false);
            progressDialog.show();


            ParseQuery<ParseObject> pointsQuery = new ParseQuery<>("Rating");
            pointsQuery.whereEqualTo("user", getItem(i));
            try {
                ParseObject rating = pointsQuery.getFirst();
                rating.increment("points", Integer.parseInt(spinnerPoints.getSelectedItem().toString()));
                rating.saveInBackground(e1 -> {
                    if(e1==null){
                        ParseQuery<ParseObject> query = new ParseQuery<>("Participant");
                        query.whereEqualTo("user", getItem(i));
                        query.whereEqualTo("event", event);
                        try {
                            ParseObject object = query.getFirst();
                            object.put("report", true);
                            object.saveInBackground(e3 -> {
                                if(e3==null){
                                    progressDialog.dismiss();
                                    Toast.makeText(view1.getContext(), "Успешно", Toast.LENGTH_SHORT).show();
                                    ParseRelation<ParseObject> relation = event.getRelation("volunteers");
                                    relation.remove(getItem(i));
                                    event.saveInBackground();
                                    remove(i);
                                    notifyDataSetChanged();
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(view1.getContext(), e3.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (com.parse.ParseException ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        progressDialog.dismiss();
                        Snackbar.make(view1, e1.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });
            } catch (com.parse.ParseException e) {
                throw new RuntimeException(e);
            }
        });

        return v;
    }
}