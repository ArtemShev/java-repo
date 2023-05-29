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
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventTreatmentActivity extends AppCompatActivity {

    private ListView listview;
    private List<ParseObject> listParse;
    public User user = new User();

   public Event event = new Event();

    public ArrayList<User> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_treatment);

        TextView textView = findViewById(R.id.textView2);
        listview = findViewById(R.id.ListViewTreatment);


//        event.setEventName("lalalal");
        ArrayList<Event> listEvent = new ArrayList<>();
        listEvent.add(event);



        ParseQuery<ParseObject> queryEvent = new ParseQuery<>("Event");
//        queryEvent.addAscendingOrder("title");
        queryEvent.findInBackground((objects, e) -> {
            if (e == null) {
                Log.d(TAG, "Objects: " + objects.get(1).getString("title"));
                event.setEventName(objects.get(1).getString("title"));
                textView.setText(event.getEventName());



                ParseRelation<ParseObject> relation = objects.get(0).getRelation("applications");
                        ParseQuery<ParseObject> queryA = relation.getQuery();
        queryA.findInBackground((objectsE, ex) -> {
            if (e == null) {

                if(objectsE != null){
//                    Log.d(TAG, "Objects: " + objectsE.get(0).getString("lastname"));
//                    Log.d(TAG, "Objects: " + objectsE.get(0).getString("firstname"));
//                    Log.d(TAG, "Objects: " + objectsE.get(0).getString("patronymic"));


                    AdapterForEventTreatment adapter = new AdapterForEventTreatment(this, objectsE);
                    listview.setAdapter(adapter);
                }
            }
        });
            } else {
                Log.e(TAG, "Parse Error: ", e);
            }

        });

//        User user = new User();

//        ParseQuery<ParseObject> queryUser = new ParseQuery<>("_User");
//        ParseQuery<ParseObject> query_event = new ParseQuery<ParseObject>("Event");
//        query_event.getInBackground("fHPefVDwMX", new GetCallback<ParseObject>() {
//            @Override
//            public void done(ParseObject object, ParseException ex) {
//
//
////                AdapterForEventTreatment adapter = new AdapterForEventTreatment(getBaseContext(), object.g);
////                listview.setAdapter(adapter);
//
//            }
//        });



//        queryUser.findInBackground((objects, e) -> {
//            if (e == null) {
//                Log.d(TAG, "Objects: " + objects.get(1).getString("lastname"));
//                Log.d(TAG, "Objects: " + objects.get(1).getString("firstname"));
//                Log.d(TAG, "Objects: " + objects.get(1).getString("patronymic"));
//
//
//                AdapterForEventTreatment adapter = new AdapterForEventTreatment(this, objects);
//                listview.setAdapter(adapter);
////
//
////
//            } else {
//                Log.e(TAG, "Parse Error: ", e);
//            }
//
//        });
        listview.setItemsCanFocus(false);
    }
}
////                user.setLastname(objects.get(1).getString("lastname"));
////                user.setFirstname(objects.get(1).getString("firstname"));
////                user.setPatronymic(objects.get(1).getString("patronymic"));