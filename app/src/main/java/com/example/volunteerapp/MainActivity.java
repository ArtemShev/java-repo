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

public class MainActivity extends AppCompatActivity {
    private int n = 0;
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
            n = queryUser.getFirst().getInt("points");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        String nameString ="Здравствуй, "+ user.getLastname()+" "+user.getFirstname()+" "+user.getPatronymic();
        TextView fullnameView = findViewById(R.id.fullnameView);
        fullnameView.setText(nameString);

        String rateString ="Твой рейтинг: "+n;
//        TextView rateView = findViewById(R.id.rateView);
//        rateView.setText(rateString);
//
//        ImageView img = findViewById(R.id.rateImageView);

//        if (n>=0 && n<=200) {
//            img.setImageResource(R.drawable.wooden);
//        } else if (n>=201 && n<=400) {
//            img.setImageResource(R.drawable.bronze);
//        } else if (n>=401 && n<=600) {
//            img.setImageResource(R.drawable.silver);
//        } else if (n>=601 && n<=800){
//            img.setImageResource(R.drawable.gold);
//        } else if (n>=801 && n<=1000){
//            img.setImageResource(R.drawable.brilliant);
//        }


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