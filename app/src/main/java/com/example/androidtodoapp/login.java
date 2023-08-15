package com.example.androidtodoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class login extends AppCompatActivity {

    public static final String LOG_TAG = login.class.getSimpleName();

    private ShoppingMemoDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d(LOG_TAG, "Das Datenquellen-Objekt wird angelegt.");
        dataSource = new ShoppingMemoDataSource(this);

        Button ButtonSignUp = findViewById(R.id.buttonSignUp);
        Button ButtonHome = findViewById(R.id.buttonHome);
        Button ButtonLogin = findViewById(R.id.buttonLogin);

        final EditText editEmail = (EditText) findViewById(R.id.editTextTextEmailAddress);
        final EditText editPassword = (EditText) findViewById(R.id.editTextTextPassword);


        ButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, registration.class);
                startActivity(intent);

            }
        });
        ButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();
                editEmail.setText("");
                editPassword.setText("");

                if (loginUser(email, password)) {
                    Intent intent = new Intent(login.this, taskOverview.class);
                    intent.putExtra("email", email);
                    intent.putExtra("password", password);
                    startActivity(intent);
                } else {
                    Toast.makeText(login.this, "Anmeldung fehlgeschlagen", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ;
        ButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, mainPage.class);
                startActivity(intent);

            }
        });
    }
    private boolean loginUser(String email, String password) {
        dataSource.open();
        boolean loginSuccessful = dataSource.checkLoginCredentials(email, password);
        dataSource.close();
        return loginSuccessful;
    }
}