package com.example.volunteerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.volunteerapp.model.User;
import com.example.volunteerapp.preferences.UserPreferences;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private int n = 0;
    private ParseObject userParse;
    private TextView fullnameView, rateView, ageView;
    Button toEventButton;
    ImageButton logoutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserPreferences ePref = new UserPreferences(this);
        User user = ePref.getUser();
        ParseQuery<ParseObject> queryUser = new ParseQuery<>("_User");
        queryUser.whereEqualTo("username", user.getLogin());
        queryUser.findInBackground();
        try {
            userParse = queryUser.getFirst();
            ParseQuery<ParseObject> pointsQuery = new ParseQuery<>("Rating");
            pointsQuery.whereEqualTo("user", userParse);
            pointsQuery.getFirstInBackground((object, e) -> {
                n = object.getInt("points");
            });
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        String nameString = user.getLastname()+" "+user.getFirstname()+" "+user.getPatronymic();
        fullnameView = findViewById(R.id.fullnameView);
        rateView = findViewById(R.id.mainView_rate);
        ageView = findViewById(R.id.mainView_age);
        fullnameView.setText(nameString);
        String rateString ="Твой рейтинг: "+n;
        rateView.setText(rateString);
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthday;
        try {
            birthday = myFormat.parse(userParse.getString("date_of_birthday"));
        } catch (java.text.ParseException e) {
            throw new RuntimeException(e);
        }
        Long time = new Date().getTime() / 1000 - birthday.getTime() / 1000;
        int years = Math.round(time) / 31536000;
        ageView.setText("Возраст: "+years);



        toEventButton = findViewById(R.id.toEventButton);
        logoutButton = findViewById(R.id.imageButtonExit);
        logoutButton.setOnClickListener(view -> {
            ParseUser.logOutInBackground();
            UserPreferences uPref = new UserPreferences(view.getContext());
                uPref.setEntered(false);
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
        });
        toEventButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, VolunteerEventsActivity.class);
            startActivity(intent);
        });
    }
}