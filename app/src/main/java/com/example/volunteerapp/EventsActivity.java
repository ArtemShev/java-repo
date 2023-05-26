package com.example.volunteerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.volunteerapp.adapters.VolunteerAdapterEvents;
import com.example.volunteerapp.preferences.UserPreferences;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class EventsActivity extends AppCompatActivity {

//    Button logoutButton;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        listView = findViewById(R.id.volunteerListView);

        ParseQuery<ParseObject> query = new ParseQuery<>("Event");
        query.findInBackground((objects, e) -> {
            if (e == null) {
                //We are initializing Todo object list to our adapter
                VolunteerAdapterEvents adapter = new VolunteerAdapterEvents(this, objects);
                listView.setAdapter(adapter);
            } else {
            }
        });
        // Код для создании кнопки выхода из аккаунта
//        logoutButton = findViewById(R.id.logoutButton);
//        logoutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                UserPreferences uPref = new UserPreferences(view.getContext());
//                uPref.setEntered(false);
//                Intent intent = new Intent(EventsActivity.this, SignInActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });


    }
//    public void mySetAdapter(List<ParseObject> list){
//        VolunteerAdapterEvents adapter = new VolunteerAdapterEvents(this, list);
//        listView.setAdapter(adapter);
//    }
}