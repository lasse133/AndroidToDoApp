package com.example.androidtodoapp;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class taskOverview extends AppCompatActivity {

    private ArrayList<String> items;
    private ArrayAdapter <String> itemsAdapter;
    private ListView listView;

    public long userID;

    String email;

    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_task_overview);

        listView = findViewById(R.id.listViewTasks);
        Button ButtonLogout = findViewById(R.id.buttonLogout);
        Button ButtonAddTask = findViewById(R.id.buttonAddTask);
        items = new ArrayList<>();
        itemsAdapter = new ArrayAdapter<>(this,  R.layout.list_item_task, R.id.textViewTask, items);
        listView.setAdapter(itemsAdapter);


        Intent intent = getIntent();
        if (intent != null) {
            email = intent.getStringExtra("email");
            password = intent.getStringExtra("password");
            if (email != null && password != null) {
                // Use the email and password variables as needed
                // For example, you could pass them to your data source
                // when calling retrieveTasksFromDatabase() method
                retrieveTasksFromDatabase(email, password);
            }
        }

        ShoppingMemoDataSource dataSource = new ShoppingMemoDataSource(this);
        dataSource.open();
        userID = dataSource.getCurrentUserID(email, password);


        ButtonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(taskOverview.this,mainPage.class);
                startActivity(intent);

            }
        });
        ButtonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(view);
            }
        });

    }
    public void onDeleteButtonClick(View view) {
        int position = listView.getPositionForView((View) view.getParent());
        String itemText = itemsAdapter.getItem(position);
        if (itemText != null) {
            ShoppingMemoDataSource dataSource = new ShoppingMemoDataSource(this);
            dataSource.open();
            dataSource.deleteTask(itemText);
            dataSource.close();

            itemsAdapter.remove(itemText);
            itemsAdapter.notifyDataSetChanged();

            Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show();
        }
    }
    private void addItem(View view) {
        EditText input = findViewById(R.id.editTextTask);
        String itemText = input.getText().toString();

        if (!(itemText.isEmpty())) {
            addItemDatabase(itemText, userID);
            itemsAdapter.add(itemText);
            input.setText("");
        } else {
            Toast.makeText(getApplicationContext(), "Please enter a text...", Toast.LENGTH_LONG).show();
        }
    }

    private void addItemDatabase(String itemText, long userID) {
        ShoppingMemoDataSource dataSource = new ShoppingMemoDataSource(this);
        dataSource.open();
        dataSource.createTask(itemText, false, userID);
        dataSource.close();
    }
    private void retrieveTasksFromDatabase(String email, String password) {
        ShoppingMemoDataSource dataSource = new ShoppingMemoDataSource(this);
        dataSource.open();
        long currentUserID = dataSource.getCurrentUserID(email, password);
        if (currentUserID != -1) {
            ArrayList<TaskList> tasks = dataSource.getTasksForUser(currentUserID);
            for (TaskList task : tasks) {
                itemsAdapter.add(task.getText());
            }
            itemsAdapter.notifyDataSetChanged();
        }

        dataSource.close();
    }

}