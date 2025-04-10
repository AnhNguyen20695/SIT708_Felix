package com.example.task41;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PersonEditor extends AppCompatActivity {
    private int boardID;
    private Database database;
    private int personID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_person_editor);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        TextView id = findViewById(R.id.id);
        EditText firstName = findViewById(R.id.firstName);
        EditText lastName = findViewById(R.id.lastName);
        EditText email = findViewById(R.id.email);
        EditText tel = findViewById(R.id.tel);
        Button submit = findViewById(R.id.submit);
        TextView delete = findViewById(R.id.delete);

        boardID = getIntent().getIntExtra("Board ID", 0);
        database = new Database();

        if (getIntent().hasExtra("Person ID")) {
            delete.setVisibility(View.VISIBLE);
            personID = getIntent().getIntExtra("Person ID",0);
            Person person = database.getPerson(boardID, personID);
            id.setText(String.valueOf(person.getID()));
            firstName.setText(person.getFirstName());
            lastName.setText(person.getLastName());
            email.setText(person.getEmail());
            tel.setText(person.getTel());

            submit.setOnClickListener(v-> {
                person.setFirstName(firstName.getText().toString());
                person.setLastName(lastName.getText().toString());
                person.setEmail(email.getText().toString());
                person.setTel(tel.getText().toString());
                database.updatePerson(boardID, person);
                Toast.makeText(this, "Person updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            });

            delete.setOnClickListener(v-> {
                database.deletePerson(boardID, personID);
                Toast.makeText(this, "Person deleted successfully", Toast.LENGTH_SHORT).show();
                finish();
            });

        } else {
            personID = database.getNextPersonID(boardID);
            id.setText(String.valueOf(personID));

            submit.setOnClickListener(v-> {
                Person person = new Person();
                person.setID(personID);
                person.setFirstName(firstName.getText().toString());
                person.setLastName(lastName.getText().toString());
                person.setEmail(email.getText().toString());
                person.setTel(tel.getText().toString());
                database.addPerson(boardID, person);
                Toast.makeText(this, "Person added successfully", Toast.LENGTH_SHORT).show();
                finish();
            });
        }
    }
}