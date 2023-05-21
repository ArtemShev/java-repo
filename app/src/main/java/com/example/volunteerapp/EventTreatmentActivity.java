package com.example.volunteerapp;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ListAdapter;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.volunteerapp.model.Event;
import com.example.volunteerapp.model.User;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventTreatmentActivity extends AppCompatActivity {

    private ListView listview;
    private List<ParseObject> listParse;
private AdapterForEventTreatment adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_treatment);

        TextView textView = findViewById(R.id.textView2);

        Event event = new Event();
        event.setEventName("lalalal");
        ArrayList<Event> listEvent = new ArrayList<>();
        listEvent.add(event);
        textView.setText(event.getEventName());


        ParseQuery<ParseObject> query = new ParseQuery<>("FirstClass");
        query.addAscendingOrder("EventName");
        query.findInBackground((objects, e) -> {
            if (e == null) {
                Log.d(TAG, "Objects: " + objects.get(1).getString("EventName"));
                listParse = objects;
            } else {
                Log.e(TAG, "Parse Error: ", e);
            }

        });

//        User user = new User();
//        user.setLastname();
////        user.setFirstname("Павел");
////        user.setPatronymic("Алексеевич");


        ArrayList<User> list = new ArrayList<>();
//        list.add(user);




        listview = findViewById(R.id.ListViewTreatment);
        adapter = new AdapterForEventTreatment(this, listParse);
        listview.setItemsCanFocus(false);
        listview.setAdapter(adapter);

    }

}