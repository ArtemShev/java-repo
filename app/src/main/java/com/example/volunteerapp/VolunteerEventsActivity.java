package com.example.volunteerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.volunteerapp.adapters.VolunteerAdapterEvents;
import com.example.volunteerapp.model.User;
import com.example.volunteerapp.preferences.UserPreferences;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;

public class VolunteerEventsActivity extends AppCompatActivity {

//    Button logoutButton;
    private ListView listView;
    private Spinner filter;
    private User user;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        UserPreferences userPref = new UserPreferences(this);
        user = userPref.getUser();

        listView = findViewById(R.id.volunteerListView);
        filter = findViewById(R.id.spinnerFilter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Загрузка");
        progressDialog.setCancelable(false);

        ArrayAdapter<CharSequence> adapterFilter = ArrayAdapter.createFromResource(this, R.array.items_for_filter_of_events_for_volunteer, android.R.layout.simple_spinner_dropdown_item);
        filter.setAdapter(adapterFilter);
        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                progressDialog.show();

                ParseObject userObj;
                ParseQuery<ParseObject> queryUser = new ParseQuery<>("_User");
                queryUser.whereEqualTo("username", user.getLogin());
                queryUser.findInBackground();
                try {
                    userObj = queryUser.getFirst();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }


                ParseQuery<ParseObject> query = new ParseQuery<>("Event");
                switch (filter.getSelectedItem().toString()){
                    case "Мои мероприятия": {
                        query.whereEqualTo("volunteers", userObj);
                        query.whereGreaterThan("date", new Date());
                        query.findInBackground((objects, e) -> {
                            progressDialog.dismiss();
                            if (e == null) {
                                VolunteerAdapterEvents adapter = new VolunteerAdapterEvents(view.getContext(), objects);
                                listView.setAdapter(adapter);
                            } else {

                            }
                        });
                        return;
                    }
                    case "Все мероприятия": {
                        progressDialog.show();
                        query.whereGreaterThan("date", new Date());
                        query.findInBackground((objects, e) -> {
                            progressDialog.dismiss();
                            if (e == null) {
                                VolunteerAdapterEvents adapter = new VolunteerAdapterEvents(view.getContext(), objects);
                                listView.setAdapter(adapter);
                            } else {

                            }
                        });
                        return;
                    }
                    case "Посещённые мероприятия": {
                        progressDialog.show();
                        query.whereLessThan("date", new Date());
                        query.whereEqualTo("volunteers", userObj);
                        query.findInBackground((objects, e) -> {
                            progressDialog.dismiss();
                            if (e == null) {
                                VolunteerAdapterEvents adapter = new VolunteerAdapterEvents(view.getContext(), objects);
                                listView.setAdapter(adapter);
                            } else {

                            }
                        });
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        query.findInBackground((objects, e) -> {
//            if (e == null) {
//                VolunteerAdapterEvents adapter = new VolunteerAdapterEvents(this, objects);
//                listView.setAdapter(adapter);
//            } else {
//            }
//        });
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
}