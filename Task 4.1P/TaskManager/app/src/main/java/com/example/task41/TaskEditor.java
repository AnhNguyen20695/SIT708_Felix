package com.example.task41;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TaskEditor extends AppCompatActivity {
    private int boardID;
    private Database database;
    private int taskID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_task_editor);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        TextView id = findViewById(R.id.id);
        EditText taskTitle = findViewById(R.id.taskTitle);
        EditText taskDescription = findViewById(R.id.taskDescription);
        EditText date = findViewById(R.id.date);
        Button submit = findViewById(R.id.submit);
        TextView delete = findViewById(R.id.delete);
        TextView editPersons = findViewById(R.id.editPersons);

        boardID = getIntent().getIntExtra("Board ID",0);
        database = new Database();

        if (getIntent().hasExtra("Task ID")) {
            delete.setVisibility(View.VISIBLE);
            editPersons.setVisibility(View.VISIBLE);
            taskID = getIntent().getIntExtra("Task ID", taskID);
            id.setText(String.valueOf(taskID));
            Task task = database.getTask(boardID, taskID);
            taskTitle.setText(task.getTitle());
            taskDescription.setText(task.getDescription());
            date.setText(task.getDate());

            submit.setOnClickListener(v-> {
                task.setTitle(taskTitle.getText().toString());
                task.setDescription(taskDescription.getText().toString());
                task.setDate(date.getText().toString());
                database.updateTaskData(boardID, task);
                Toast.makeText(this,"Task updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            });

            delete.setOnClickListener(v-> {
                database.deleteTask(boardID, taskID);
                Toast.makeText(this,"Task deleted successfully", Toast.LENGTH_SHORT).show();
                finish();
            });

            editPersons.setOnClickListener(v-> {
                Intent intent = new Intent(TaskEditor.this, EditTaskPersons.class);
                intent.putExtra("Board ID", boardID);
                intent.putExtra("Task ID", taskID);
                startActivity(intent);
                finish();
            });

        } else {
            taskID = database.getNextTaskID(boardID);
            id.setText(String.valueOf(taskID));

            submit.setOnClickListener(v-> {
                Task task = new Task();
                task.setID(taskID);
                task.setTitle(taskTitle.getText().toString());
                task.setDescription(taskDescription.getText().toString());
                task.setDate(date.getText().toString());
                database.addTask(boardID, task);
                Toast.makeText(this,"Task added successfully", Toast.LENGTH_SHORT).show();
                finish();
            });
        }
    }
}