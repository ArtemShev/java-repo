package com.example.volunteerapp;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.example.volunteerapp.adapters.AdapterForEventTreatment;
import com.example.volunteerapp.model.Event;
import com.example.volunteerapp.model.User;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.ArrayList;
import java.util.List;

public class EventTreatmentActivity extends AppCompatActivity {

    private ListView listview;
    public ParseObject event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_treatment);

        TextView textView = findViewById(R.id.textView2);
        listview = findViewById(R.id.ListViewTreatment);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            event = (ParseObject) extras.get("event");
            textView.setText(event.getString("title"));
            ParseRelation<ParseObject> relation = event.getRelation("applications");
            ParseQuery<ParseObject> queryA = relation.getQuery();
            queryA.findInBackground((objectsE, ex) -> {
                if (ex == null) {
                    if(objectsE != null){
                        AdapterForEventTreatment adapter = new AdapterForEventTreatment(this, objectsE, event.getObjectId());
                        listview.setAdapter(adapter);
                        listview.setItemsCanFocus(false);
                    }
                }
            });
        }

    }
}
