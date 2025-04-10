package com.example.task41;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ViewTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_view_task);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        int boardID = getIntent().getIntExtra("Board ID",0);
        int taskID = getIntent().getIntExtra("Task ID",0);
        Database database = new Database();
        Task task = database.getTask(boardID, taskID);
        ArrayList<Person> allPersons = database.getPersons(boardID);
        ArrayList<Person> assignedPersons = task.getPersons();
        ArrayList<Integer> assignedIDs = new ArrayList<>();
        ArrayList<Person> unassignedPersons = new ArrayList<>();

        for (Person p: assignedPersons) {
            assignedIDs.add(p.getID());
        }
        for (Person p : allPersons) {
            if (!assignedIDs.contains(p.getID())) {unassignedPersons.add(p);}
        }

        ListView assigned = findViewById(R.id.assigned);
        assigned.setAdapter(new ListAdapter(assignedPersons));
        setListHeight(assigned);

        ListView unassigned = findViewById(R.id.unassigned);
        unassigned.setAdapter(new ListAdapter(unassignedPersons));
        setListHeight(unassigned);
    }

    private class ListAdapter extends BaseAdapter {
        private ArrayList<Person> data;

        public ListAdapter(ArrayList<Person> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.list_item, null);
            TextView text = v.findViewById(R.id.text);
            text.setText(data.get(i).getFirstName()+" "+data.get(i).getLastName());
            return v;
        }
    }

    private void setListHeight(ListView list) {
        android.widget.ListAdapter adapter = list.getAdapter();
        if (adapter == null) {
            return;
        }
        int height = 0;

        for (int i=0; i<adapter.getCount(); i++) {
            View listitem = adapter.getView(i, null, list);
            listitem.measure(0,0);
            height += listitem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = list.getLayoutParams();
        par.height = height + (list.getDividerHeight() * (adapter.getCount()-1));
        list.setLayoutParams(par);
        list.requestLayout();
    }
}