package com.example.volunteerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.vicmikhailau.maskededittext.MaskedEditText;

public class SignUpActivity extends AppCompatActivity {

    private EditText lastnameEditText, firstnameEditText, patronymicEditText, loginEditText, passwordEditText;
    private MaskedEditText ageEditText, phoneEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }
}