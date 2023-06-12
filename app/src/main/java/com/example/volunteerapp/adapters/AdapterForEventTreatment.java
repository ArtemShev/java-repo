package com.example.volunteerapp.adapters;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.volunteerapp.R;
import com.example.volunteerapp.SignInActivity;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



public class AdapterForEventTreatment extends BaseAdapter  {
    private ProgressDialog progressDialog;
    private String id;
    private List<ParseObject> list;
    private LayoutInflater inflater ;
    private Context context;
    private ParseObject userPoints;

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

        ParseObject user = getItem(i).getParseObject("user");
        try {
            user.fetchIfNeeded();
        } catch (com.parse.ParseException e) {
            throw new RuntimeException(e);
        }

        ParseQuery<ParseObject> userRatingQuery = new ParseQuery<>("Rating");
        userRatingQuery.whereEqualTo("user", user);
        userRatingQuery.getFirstInBackground((object, e) -> {
            userRating.setText("Рейтинг: " + object.getInt("points"));
        });
//        try {
//
//            userRating.setText("Рейтинг: " + userPoints.getInt("points"));
//        } catch (com.parse.ParseException e) {
//            throw new RuntimeException(e);
//        }

        userName.setText(user.getString("lastname")+ " "+ user.getString("firstname")+ " "+ user.getString("patronymic"));
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthday;
        try {
            birthday = myFormat.parse(user.getString("date_of_birthday"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Long time = new Date().getTime() / 1000 - birthday.getTime() / 1000;
        int years = Math.round(time) / 31536000;
        userAge.setText("Возраст: "+years);
        userPhone.setText(user.getString("phone"));

        progressDialog = new ProgressDialog(v.getContext());
        progressDialog.setMessage("Загрузка");
        progressDialog.setCancelable(false);

        acceptButton.setOnClickListener(view1 -> {
            progressDialog.show();
            ParseQuery<ParseObject> parseV = ParseQuery.getQuery("Participant");
            parseV.getInBackground(getItem(i).getObjectId(), (part, e) -> {
                if (e == null) {
                    ParseQuery<ParseObject> evQ = new ParseQuery<>("Event");
                    evQ.getInBackground(id, (ev, e2) -> {
                        if(ev.getInt("quantity_current")<ev.getInt("quantity_max")){
                            ev.put("quantity_current", ev.getInt("quantity_current")+1);
                            ev.saveInBackground();
                            ParseRelation<ParseObject> relationVolunteers = ev.getRelation("volunteers");
                            relationVolunteers.add(user);
                            ev.saveInBackground();
                            part.put("application", false);
                            part.put("participant", true);
                            part.saveInBackground(e1 -> {
                                if (e1 == null) {
                                    progressDialog.dismiss();
                                    Toast.makeText(view1.getContext(), "Заявка принята", Toast.LENGTH_LONG).show();
                                    remove(i);
                                    notifyDataSetChanged();
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(view1.getContext(), e1.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            progressDialog.dismiss();
                            Snackbar.make(view1, "На мероприятии максимальное количество участников", Snackbar.LENGTH_SHORT).show();
                        }

                    });

                } else {
                    Toast.makeText(view1.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
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
