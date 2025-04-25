package com.example.lostandfound.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lostandfound.DatabaseHelper;
import com.example.lostandfound.R;
import com.example.lostandfound.RecyclerAdapters.ItemRecyclerAdapter;
import com.example.lostandfound.RecyclerAdapters.ItemRecyclerInterface;
import com.example.lostandfound.models.FoundModel;
import com.example.lostandfound.models.LostModel;
import com.example.lostandfound.activities.RemoveItemActivity;

import java.util.List;

public class ShowAllActivity extends AppCompatActivity implements ItemRecyclerInterface {
    List<LostModel> lostItems;
    List<FoundModel> foundItems;
    ItemRecyclerAdapter lostItemRecyclerAdapter;
    ItemRecyclerAdapter foundItemRecyclerAdapter;
    RecyclerView lostItemsRecyclerView;
    RecyclerView foundItemsRecyclerView;
    private DatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all);

        database = new DatabaseHelper(this);

        lostItems = database.retrieveLostItems();
        Log.i("MAIN_LOG","Retrieved number of lost items: "+lostItems.size());
        foundItems = database.retrieveFoundItems();

        lostItemsRecyclerView = findViewById(R.id.lost_items_recycler_view);
        lostItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        lostItemRecyclerAdapter = new ItemRecyclerAdapter(lostItems, foundItems, "lost", this);
        lostItemsRecyclerView.setAdapter(lostItemRecyclerAdapter);

        foundItemsRecyclerView = findViewById(R.id.found_items_recycler_view);
        foundItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        foundItemRecyclerAdapter = new ItemRecyclerAdapter(lostItems, foundItems, "found", this);
        foundItemsRecyclerView.setAdapter(foundItemRecyclerAdapter);
    }




    @Override
    public void onItemClick(int position, String postType) {
        Intent intent = new Intent(this, RemoveItemActivity.class);
        if (postType.equals("lost")) {
            intent.putExtra("ID",lostItems.get(position).getID());
            intent.putExtra("PostType","lost");
            intent.putExtra("Name",lostItems.get(position).getName());
            intent.putExtra("PhoneNumber",lostItems.get(position).getPhoneNumber());
            intent.putExtra("Description",lostItems.get(position).getDescription());
            intent.putExtra("ReportedDate",lostItems.get(position).getReportedDate());
            intent.putExtra("Location",lostItems.get(position).getLocation());
            startActivity(intent);
        } else if (postType.equals("found")) {
            intent.putExtra("ID",foundItems.get(position).getID());
            intent.putExtra("PostType","found");
            intent.putExtra("Name",foundItems.get(position).getName());
            intent.putExtra("PhoneNumber",foundItems.get(position).getPhoneNumber());
            intent.putExtra("Description",foundItems.get(position).getDescription());
            intent.putExtra("ReportedDate",foundItems.get(position).getReportedDate());
            intent.putExtra("Location",foundItems.get(position).getLocation());
            startActivity(intent);
        }
    }
}