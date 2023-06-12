package com.example.volunteerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.volunteerapp.adapters.AdapterForEventReport;
import com.example.volunteerapp.adapters.AdapterForEventTreatment;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

public class EventReportActivity extends AppCompatActivity {

    private ListView listview;
    private TextView title;
    public ParseObject event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_report);

        title = findViewById(R.id.titleReport);
        listview = findViewById(R.id.ListViewReport);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            event = (ParseObject) extras.get("event");
            title.setText(event.getString("title"));
            ParseRelation<ParseObject> relation = event.getRelation("volunteers");
            ParseQuery<ParseObject> queryA = relation.getQuery();
            queryA.findInBackground((objectsE, ex) -> {
                if (ex == null) {
                    if(objectsE != null){
                        AdapterForEventReport adapter = new AdapterForEventReport(this, objectsE, event.getObjectId());
                        listview.setAdapter(adapter);
                        listview.setItemsCanFocus(false);
                    }
                }
            });
        }

    }
}