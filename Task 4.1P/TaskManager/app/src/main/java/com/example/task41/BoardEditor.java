package com.example.task41;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class BoardEditor extends AppCompatActivity {
    private int boardID;
    private Database database;
    private ArrayList<Person> persons;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_board_editor);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        EditText board_name = findViewById(R.id.board_name);
        Button add_persons = findViewById(R.id.add_persons);
        Button create = findViewById(R.id.create);
        Button delete = findViewById(R.id.delete);
        ListView listview = findViewById(R.id.listview);

        database = new Database();
        Log.e("BOARD_EDITOR","DB connected");

        if (getIntent().hasExtra("Board ID")) {
            board_name.setText(getIntent().getStringExtra("Board Name"));
            boardID = getIntent().getIntExtra("Board ID",0);
            board_name.setEnabled(false);
            create.setVisibility(View.GONE);
            Log.e("BOARD_EDITOR","If logic");
        } else {
            Log.e("BOARD_EDITOR","no Board ID");
            add_persons.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
            boardID = database.getNextBoardID();
            Log.e("BOARD_EDITOR","Else logic");
        }
        Log.e("BOARD_EDITOR","Finished setting elements");

        persons = database.getPersons(boardID);
        Log.e("BOARD_EDITOR","Finished pulling persons");

        listAdapter = new ListAdapter();
        listview.setAdapter(listAdapter);

        Log.e("BOARD_EDITOR","List pulled");

        add_persons.setOnClickListener(v-> {
            Intent intent = new Intent(BoardEditor.this, PersonEditor.class);
            intent.putExtra("Board ID", boardID);
            startActivity(intent);
        });
        Log.e("BOARD_EDITOR","Added persons on click");

        create.setOnClickListener(v-> {
            Board b = new Board(database.getNextBoardID(), board_name.getText().toString());
            database.addBoard(b);
            board_name.setEnabled(false);
            create.setVisibility(View.GONE);
            add_persons.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
        });
        Log.e("BOARD_EDITOR","Added create on click");

        delete.setOnClickListener(v-> {
            database.deleteBoard(boardID);
            finish();
        });

        Log.e("BOARD_EDITOR","Added delete on click");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("BOARD_EDITOR","Resuming..");
        persons = database.getPersons(boardID);
        listAdapter.notifyDataSetChanged();
        Log.e("BOARD_EDITOR","Resumed..");
    }

    private class ListAdapter extends BaseAdapter {
        public ListAdapter() {}
        @Override
        public int getCount() {
            return persons.size();
        }

        @Override
        public Person getItem(int i) {
            return persons.get(i);
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
            Log.e("BOARD_EDITOR","Getting list of persons in the board.");
            text.setText(persons.get(i).getFirstName()+persons.get(i).getLastName());
            Log.e("BOARD_EDITOR","Text set: "+persons.get(i).getFirstName());
            text.setOnClickListener(v1-> {
                Intent intent = new Intent(BoardEditor.this, PersonEditor.class);
                intent.putExtra("Board ID", boardID);
                intent.putExtra("Person ID", persons.get(i).getID());
                startActivity(intent);
            });
            return v;
        }
    }
}