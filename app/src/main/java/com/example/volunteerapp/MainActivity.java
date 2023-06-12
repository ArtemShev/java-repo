package com.example.volunteerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
    private int rating;
    private ParseObject userParse;
    private TextView fullnameView, rateView, ageView;
    private ProgressBar barView;
    Button toEventButton;
    ImageButton logoutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rateView = findViewById(R.id.mainView_rate);
        barView = findViewById(R.id.progressBar2);
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
                rating = object.getInt("points");
                barView.setMax(1000);
                barView.setProgress(rating);
                String rateString ="Баллов активности: " + rating;
                rateView.setText(rateString);
                if (rating<=200){
                    barView.getProgressDrawable().setColorFilter(Color.rgb(255, 155, 82), android.graphics.PorterDuff.Mode.SRC_IN);
                } else if (rating>200 && rating<=400) {
                    barView.getProgressDrawable().setColorFilter(Color.rgb(142, 85, 0), android.graphics.PorterDuff.Mode.SRC_IN);
                } else if (rating>400 && rating<=600) {
                    barView.getProgressDrawable().setColorFilter(Color.rgb(114, 114, 114), android.graphics.PorterDuff.Mode.SRC_IN);
                } else if (rating>600 && rating<=800) {
                    barView.getProgressDrawable().setColorFilter(Color.rgb(255, 206, 82 ), android.graphics.PorterDuff.Mode.SRC_IN);
                } else if (rating>800 && rating<=1000) {
                    barView.getProgressDrawable().setColorFilter(Color.rgb(111, 199, 199), android.graphics.PorterDuff.Mode.SRC_IN);
                }


            });
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        String nameString = user.getLastname()+" "+user.getFirstname()+" "+user.getPatronymic();
        fullnameView = findViewById(R.id.fullnameView);

        ageView = findViewById(R.id.mainView_age);
        fullnameView.setText(nameString);

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