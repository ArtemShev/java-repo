package com.example.volunteerapp.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.volunteerapp.model.User;

public class UserPreferences {
    SharedPreferences myPrefs;
    SharedPreferences.Editor prefEditor;
    Context context;

    private static final String FILE_NAME = "CurrentUser";

    public UserPreferences(Context context) {
        this.context = context;
        myPrefs = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }


    public void setUser(User user){
        prefEditor = myPrefs.edit();
        prefEditor.putString("lastname", user.getLastname());
        prefEditor.putString("firstname", user.getFirstname());
        prefEditor.putString("patronymic", user.getPatronymic());
        prefEditor.putString("login", user.getLogin());
        prefEditor.putString("age", user.getAge());
        prefEditor.putString("phone", user.getPhone());
        prefEditor.putString("post", user.getPost());
        prefEditor.putString("division", user.getDivision());
        prefEditor.putString("scores", user.getPoints());
        prefEditor.apply();
    }

    public User getUser(){
        User user = new User();
        user.setLastname(myPrefs.getString("lastname", ""));
        user.setFirstname(myPrefs.getString("firstname", ""));
        user.setPatronymic(myPrefs.getString("patronymic", ""));
        user.setLogin(myPrefs.getString("login", ""));
        user.setAge(myPrefs.getString("age", ""));
        user.setPhone(myPrefs.getString("phone", ""));
        user.setPost(myPrefs.getString("post", ""));
        user.setDivision(myPrefs.getString("division", ""));
        user.setPoints(myPrefs.getString("scores", ""));
        return user;
    }

    public void setEntered(boolean bool) {
        prefEditor = myPrefs.edit();
        prefEditor.putBoolean("HAS_ENTERED", bool);
        prefEditor.apply();

    }

    public boolean getEntered() {
        return myPrefs.getBoolean("HAS_ENTERED", false);
    }
}
