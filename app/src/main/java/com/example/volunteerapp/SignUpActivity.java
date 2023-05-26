package com.example.volunteerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.vicmikhailau.maskededittext.MaskedEditText;

public class SignUpActivity extends AppCompatActivity {

    private EditText lastnameEditText, firstnameEditText, patronymicEditText, loginEditText, passwordEditText;
    private MaskedEditText ageEditText, phoneEditText;
    Button enterSigUpButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        lastnameEditText = findViewById(R.id.lastnameField);
        firstnameEditText = findViewById(R.id.firstnameField);
        patronymicEditText = findViewById(R.id.patronymicField);
        loginEditText = findViewById(R.id.loginField);
        passwordEditText = findViewById(R.id.password1Field);
        ageEditText = findViewById(R.id.ageField);
        phoneEditText = findViewById(R.id.phoneField);
        enterSigUpButton = findViewById(R.id.enterSignUpButton);

        enterSigUpButton.setOnClickListener(view -> signUp());

    }

    private void signUp() {
        if (!lastnameEditText.getText().toString().equals("") ||
                !firstnameEditText.getText().toString().equals("") ||
                !patronymicEditText.getText().toString().equals("") ||
                !loginEditText.getText().toString().equals("") ||
                !passwordEditText.getText().toString().equals("") ||
                !ageEditText.getText().toString().equals("") ||
                !phoneEditText.getText().toString().equals("")){
            ParseUser user = new ParseUser();
            // Set the user's username and password, which can be obtained by a forms
            user.setUsername( loginEditText.getText().toString());
            user.setPassword( passwordEditText.getText().toString());
            user.put("lastname", lastnameEditText.getText().toString());
            user.put("firstname", firstnameEditText.getText().toString());
            user.put("patronymic", patronymicEditText.getText().toString());
            user.put("date_of_birthday", ageEditText.getText().toString());
            user.put("phone", phoneEditText.getText().toString());
            user.signUpInBackground(e -> {
                if (e == null) {
                    Toast.makeText(SignUpActivity.this, "Вы успешно зарегестрировались!", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    ParseUser.logOut();
                    Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}