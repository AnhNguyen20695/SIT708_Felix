package com.example.task41;

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
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Database database;
    private ArrayList<Board> boards;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ListView listView = findViewById(R.id.listview);
        Button new_board = findViewById(R.id.new_board);

        new_board.setOnClickListener(v-> {
            Intent intent = new Intent(MainActivity.this, BoardEditor.class);
            startActivity(intent);
        });

        database = new Database();
        boards = database.getAllBoards();
        listAdapter = new ListAdapter();
        listView.setAdapter(listAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        boards = database.getAllBoards();
        listAdapter.notifyDataSetChanged();
    }
    public class ListAdapter extends BaseAdapter {

        public ListAdapter() {}
        @Override
        public int getCount() {
            return boards.size();
        }

        @Override
        public Board getItem(int i) {
            return boards.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.list_item,null);
            TextView text = v.findViewById(R.id.text);
            text.setText(boards.get(i).getBoardName());

            text.setOnClickListener(v1-> {
                Intent intent = new Intent(MainActivity.this, ShowTasks.class);
                intent.putExtra("Board ID", boards.get(i).getID());
                intent.putExtra("Board Name", boards.get(i).getBoardName());
                startActivity(intent);
            });

            text.setOnLongClickListener(v1-> {
                Log.d("MAIN_ACTIVITY_ERROR","Switching to BoardEditor..");
                Intent intent = new Intent(MainActivity.this, BoardEditor.class);
                intent.putExtra("Board ID", boards.get(i).getID());
                intent.putExtra("Board Name", boards.get(i).getBoardName());
                startActivity(intent);
                return true;
            });
            return v;
        }
    }
}