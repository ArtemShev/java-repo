package com.example.volunteerapp.adapters;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.volunteerapp.R;
import com.example.volunteerapp.model.User;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.SaveCallback;

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
        ImageButton acceptButton = v.findViewById(R.id.acceptButton_of_item);
        acceptButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ParseQuery<ParseObject> parseV = ParseQuery.getQuery("Event");
                parseV.getInBackground("kGgJq7jCKp", new GetCallback<ParseObject>() {
                    public void done(ParseObject event, ParseException e) {
                        if (e == null) {
                            ParseRelation<ParseObject> relation = event.getRelation("volunteers");
                                    relation.add(getItem(i));

                            event.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Log.d(TAG, "sucess" );
                                    } else {
                                        Log.d(TAG, "not sucess"+ e );
                                    }
                                }
                            });
                        } else {
                            Log.d(TAG, "not sucess" );
                        }
                    }
                });
            }
            });

//        Button cancelButton = v.findViewById(R.id.cancelButton_of_item);
//        userName.setText(getItem(i).getLastname()+ getItem(i).getFirstname()+ getItem(i).getPatronymic());
       userName.setText(getItem(i).getString("lastname")+ " "+ getItem(i).getString("firstname")+ " "+ getItem(i).getString("patronymic"));
//        userName.setText(getItem(i));
        return v;
    }





}
