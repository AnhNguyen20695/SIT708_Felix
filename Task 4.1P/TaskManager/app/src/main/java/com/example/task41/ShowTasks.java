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
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ShowTasks extends AppCompatActivity {
    private int boardID;
    private Database database;
    private ArrayList<Task> tasks;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_show_tasks);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ListView listView = findViewById(R.id.listview);
        boardID = getIntent().getIntExtra("Board ID",0);
        database = new Database();
        tasks = database.getTasks(boardID);
        listAdapter = new ListAdapter();
        listView.setAdapter(listAdapter);

        Button new_task = findViewById(R.id.new_task);
        new_task.setOnClickListener(v-> {
            Intent intent = new Intent(ShowTasks.this, TaskEditor.class);
            intent.putExtra("Board ID", boardID);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        tasks = database.getTasks(boardID);
        listAdapter.notifyDataSetChanged();
    }

    private class ListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return tasks.size();
        }

        @Override
        public Task getItem(int i) {
            return tasks.get(i);
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
            View v = inflater.inflate(R.layout.list_item,null);
            TextView text = v.findViewById(R.id.text);
            text.setText(tasks.get(i).getTitle()+" - "+tasks.get(i).getDate());

            text.setOnLongClickListener(v1-> {
                Intent intent = new Intent(ShowTasks.this, TaskEditor.class);
                intent.putExtra("Board ID", boardID);
                intent.putExtra("Task ID", tasks.get(i).getID());
                startActivity(intent);
                return true;
            });

            text.setOnClickListener(v1-> {
                Intent intent = new Intent(ShowTasks.this, ViewTask.class);
                intent.putExtra("Board ID", boardID);
                intent.putExtra("Task ID",tasks.get(i).getID());
                startActivity(intent);
            });

            return v;
        }
    }
}