package com.example.task41;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EditTaskPersons extends AppCompatActivity {
    private Database database;
    private int boardID;
    private int taskID;
    private ArrayList<Person> boardPersons;
    private ArrayList<Integer> taskPersons;
    private ArrayList<Integer> insertPersons;
    private ArrayList<Integer> deletePersons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_edit_task_persons);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Button submit = findViewById(R.id.submit);
        ListView listview = findViewById(R.id.listview);
        database = new Database();
        boardID = getIntent().getIntExtra("Board ID",0);
        taskID = getIntent().getIntExtra("Task ID",0);
        boardPersons = database.getPersons(boardID);
        Task task = database.getTask(boardID, taskID);
        ArrayList<Person> taskPersons1 = task.getPersons();
        taskPersons = new ArrayList<>();
        for (Person p: taskPersons1) {
            taskPersons.add(p.getID());
        }
        insertPersons = new ArrayList<>();
        deletePersons = new ArrayList<>();

        listview.setAdapter(new ListAdapter());
        submit.setOnClickListener(v-> {
            database.addPersonsToTask(boardID, taskID, insertPersons);
            database.removePersonsFromTask(boardID, taskID, deletePersons);
            finish();
        });
    }

    private class ListAdapter extends BaseAdapter {
        public ListAdapter() {}

        @Override
        public int getCount() {
            return boardPersons.size();
        }

        @Override
        public Person getItem(int i) {
            return boardPersons.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint({"InflateParams", "ViewHolder"})
            View v = inflater.inflate(R.layout.list_item_checkbox,null);
            CheckBox text = v.findViewById(R.id.text);
            Person person = boardPersons.get(i);
            int personID = person.getID();
            text.setText(person.getFirstName()+" "+person.getLastName());
            text.setChecked(taskPersons.contains(personID));

            text.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    if (checked && !taskPersons.contains(personID)) {
                        addPerson(personID);
                    } else if (!checked && taskPersons.contains(personID)) {
                        removePerson(personID);
                    } else if (checked && taskPersons.contains(personID)) {
                        if (deletePersons.contains(personID) && deletePersons.size()!=1) {
                            deletePersons.remove(personID);
                        } else {
                            deletePersons = new ArrayList<>();
                        }
                    }
                }
            });

            return v;
        }
    }

    private void addPerson(int id) {
        if (!insertPersons.contains(id)) {
            insertPersons.add(id);
        }
        if (deletePersons.contains(id)) {
            deletePersons.remove(id);
        }
    }

    private void removePerson(int id) {
        if (insertPersons.contains(id)) {
            insertPersons.remove(id);
        }
        if (!deletePersons.contains(id)) {
            deletePersons.add(id);
        }
    }

}