package com.example.androidtodoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class registration extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Button ButtonSignUp = findViewById(R.id.buttonSignUp);
        Button ButtonHome = findViewById(R.id.buttonHome);
        Button ButtonLogin = findViewById(R.id.buttonLogin);


        ButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText usernameEditText = findViewById(R.id.editTextTextName);
                EditText emailEditText = findViewById(R.id.editTextTextEmailAddress);
                EditText passwordEditText = findViewById(R.id.editTextTextPassword);

                String username = usernameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (validateInputs(username, email, password)) {
                    saveUserToDatabase(username, email, password);
                    Intent intent = new Intent(registration.this,taskOverview.class);
                    startActivity(intent);
                }
            }
        });
        ButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(registration.this,login.class);

                startActivity(intent);

            }
        });
        ButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(registration.this,mainPage.class);
                startActivity(intent);

            }
        });
    }
    private boolean validateInputs(String username, String email, String password) {
        return !username.isEmpty() && !email.isEmpty() && !password.isEmpty();
    }

    private void saveUserToDatabase(String username, String email, String password) {
        ShoppingMemoDataSource dataSource = new ShoppingMemoDataSource(this);
        dataSource.open();
        dataSource.createUser(username, email, Integer.parseInt(password));
        dataSource.close();
    }
}