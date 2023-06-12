package com.example.volunteerapp;

import static java.lang.Integer.parseInt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.volunteerapp.adapters.VolunteerAdapterEvents;
import com.example.volunteerapp.model.User;
import com.example.volunteerapp.preferences.UserPreferences;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.vicmikhailau.maskededittext.MaskedEditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class VolunteerEventsActivity extends AppCompatActivity {

    private ListView listView;
    private Spinner filter;
    private User user;
    private FloatingActionButton addEventButton;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        UserPreferences userPref = new UserPreferences(this);
        user = userPref.getUser();

        listView = findViewById(R.id.volunteerListView);
        filter = findViewById(R.id.spinnerFilter);
        addEventButton = findViewById(R.id.addEventActionButton);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Загрузка");
        progressDialog.setCancelable(false);
        
        ArrayAdapter<CharSequence> adapterVolunteerFilter = ArrayAdapter.createFromResource(this, R.array.items_for_filter_of_events_for_volunteer, android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> adapterEmployeeFilter = ArrayAdapter.createFromResource(this, R.array.items_for_filter_of_events_for_employee, android.R.layout.simple_spinner_dropdown_item);


        if(user.getPost().equals("student")){
            filter.setAdapter(adapterVolunteerFilter);
            addEventButton.setVisibility(View.GONE);
        } else {
            filter.setAdapter(adapterEmployeeFilter);
        }
        
        addEventButton.setOnClickListener(view -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Добавить мероприятие");
            alert.setMessage("Введите все необходимые данные");

            LayoutInflater inflater = LayoutInflater.from(this);
            View window_addevent = inflater.inflate(R.layout.window_add_event, null);
            alert.setView(window_addevent);

            EditText titleField = window_addevent.findViewById(R.id.eventEditTextTitle);
            EditText descriptionField = window_addevent.findViewById(R.id.eventEditTextDescription);
            EditText quantityField = window_addevent.findViewById(R.id.eventEditTextQuantityMax);
            EditText venueField = window_addevent.findViewById(R.id.eventEditTextVenue);
            MaskedEditText dateField = window_addevent.findViewById(R.id.eventEditTextDate);

            Spinner spinnerHours = window_addevent.findViewById(R.id.spinnerHours);
            ArrayAdapter<CharSequence> adapterHours = ArrayAdapter.createFromResource(this, R.array.hours, android.R.layout.simple_spinner_item);
            adapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerHours.setAdapter(adapterHours);

            Spinner spinnerMinutes = window_addevent.findViewById(R.id.spinnerMinutes);
            ArrayAdapter<CharSequence> adapterMinutes = ArrayAdapter.createFromResource(this, R.array.minutes, android.R.layout.simple_spinner_dropdown_item);
            spinnerMinutes.setAdapter(adapterMinutes);

            alert.setNegativeButton("Отменить", (dialogInterface, i) -> dialogInterface.dismiss());
            alert.setPositiveButton("Добавить", (dialogInterface, i) -> {
                UserPreferences uP = new UserPreferences(view.getContext());
                User user = uP.getUser();
                ParseObject event = new ParseObject("Event");
                if((TextUtils.isEmpty(titleField.getText().toString())) ||
                        (TextUtils.isEmpty(dateField.getText().toString())) ||
                        (TextUtils.isEmpty(descriptionField.getText().toString())) ||
                        (TextUtils.isEmpty(quantityField.getText().toString())) ||
                        (TextUtils.isEmpty(venueField.getText().toString()))) {
                    Toast.makeText(view.getContext(), "Заполните все поля!", Toast.LENGTH_SHORT).show();
                } else {
                    event.put("title", titleField.getText().toString());
                    event.put("description", descriptionField.getText().toString());
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    Date date;
                    try {
                        date = df.parse(dateField.getText().toString()+" "+spinnerHours.getSelectedItem()+":"+spinnerMinutes.getSelectedItem());
                    } catch (java.text.ParseException e) {
                        throw new RuntimeException(e);
                    }
                    event.put("date", date);
                    event.put("quantity_max", Integer.parseInt(quantityField.getText().toString()));
                    event.put("organizer", user.getLastname()+" "+user.getFirstname()+" "+user.getPatronymic());
                    event.put("venue", venueField.getText().toString());
                    event.saveInBackground(e -> {
                        if(e == null){
                            Toast.makeText(view.getContext(), "Мероприятие добавлено", Toast.LENGTH_SHORT).show();
                            ParseQuery<ParseObject> query = new ParseQuery<>("Event");
                            query.whereGreaterThan("date", new Date());
                            query.findInBackground((objects, ex) -> {
                                progressDialog.dismiss();
                                if (ex == null) {
                                    VolunteerAdapterEvents adapter = new VolunteerAdapterEvents(view.getContext(), objects);
                                    listView.setAdapter(adapter);
                                } else {

                                }
                            });
                        } else {
                            Toast.makeText(view.getContext(), "Ошибка", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialogInterface.dismiss();
                }
            });
            alert.show();
        });

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
                    case "Все мероприятия":
                    case "Рассмотреть заявки": {
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
                        return;
                    }
                    case "Сделать отчет": {
                        progressDialog.show();
                        query.whereLessThan("date", new Date());
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
    }
}