package com.example.volunteerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.volunteerapp.model.User;
import com.example.volunteerapp.preferences.UserPreferences;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";

    Button toEventButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UserPreferences ePref = new UserPreferences(this);
        User user = ePref.getUser();
        String nameString ="Здравствуй, "+ user.getLastname()+" "+user.getFirstname()+" "+user.getPatronymic();
        TextView fullnameView = findViewById(R.id.fullnameView);
        fullnameView.setText(nameString);
        Log.v(TAG, user.getPoints());
        String rateString ="Твой рейтинг: "+user.getPoints();
        TextView rateView = findViewById(R.id.rateView);
        rateView.setText(rateString);
        ImageView img=(ImageView) findViewById(R.id.rateImageView);
        int n = 900;
        if (n>=0 && n<=200) {
            img.setImageResource(R.drawable.wooden);
        } else if (n>=201 && n<=400) {
            img.setImageResource(R.drawable.bronze);
        } else if (n>=401 && n<=600) {
            img.setImageResource(R.drawable.silver);
        } else if (n>=601 && n<=800){
            img.setImageResource(R.drawable.gold);
        } else if (n>=801 && n<=1000){
            img.setImageResource(R.drawable.brilliant);
        }


        toEventButton = findViewById(R.id.toEventButton);

        toEventButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, EventsActivity.class);
            startActivity(intent);
        });
    }
}


//         0 до 200 – Медный
//        201 до 400 – Бронзовый
//        401-600 – Серебряный
//        601-800 – Золотой
//        801 – 1000 – Бриллиантовый