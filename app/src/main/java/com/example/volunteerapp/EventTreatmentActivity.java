package com.example.volunteerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.volunteerapp.adapters.AdapterForEventTreatment;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

public class EventTreatmentActivity extends AppCompatActivity {

    private ListView listview;
    private TextView title;
    public ParseObject event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_treatment);

        title = findViewById(R.id.titleTreatment);
        listview = findViewById(R.id.ListViewTreatment);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            event = (ParseObject) extras.get("event");
            title.setText(event.getString("title"));
            ParseQuery<ParseObject> applicationsQuery = new ParseQuery<>("Participant");
            applicationsQuery.whereEqualTo("event", event);
            applicationsQuery.whereEqualTo("application", true);
            applicationsQuery.findInBackground((objects, e) -> {
                if (e == null) {
                    if(objects != null){
                        AdapterForEventTreatment adapter = new AdapterForEventTreatment(this, objects, event.getObjectId());
                        listview.setAdapter(adapter);
                        listview.setItemsCanFocus(false);
                    }
                }
            });
//            ParseRelation<ParseObject> relation = event.getRelation("applications");
//            ParseQuery<ParseObject> queryA = relation.getQuery();
//            queryA.findInBackground((objectsE, ex) -> {
//                if (ex == null) {
//                    if(objectsE != null){
//                        AdapterForEventTreatment adapter = new AdapterForEventTreatment(this, objectsE, event.getObjectId());
//                        listview.setAdapter(adapter);
//                        listview.setItemsCanFocus(false);
//                    }
//                }
//            });
        }

    }
}
