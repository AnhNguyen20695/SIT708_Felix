package com.example.lostandfound.RecyclerAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lostandfound.R;
import com.example.lostandfound.models.FoundModel;
import com.example.lostandfound.models.LostModel;

import java.util.List;

public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.ItemViewHolder> {
    List<LostModel> lostItems;
    List<FoundModel> foundItems;
    String postType;
    private final ItemRecyclerInterface itemRecyclerInterface;

    public ItemRecyclerAdapter(List<LostModel> lostItems,
                        List<FoundModel> foundItems,
                        String postType,
                        ItemRecyclerInterface itemRecyclerInterface) {
        if (postType.equals("lost")) {
            this.lostItems = lostItems;
        } else if (postType.equals("found")) {
            this.foundItems = foundItems;
        }
        this.postType = postType;
        this.itemRecyclerInterface = itemRecyclerInterface;
    }

    @NonNull
    @Override
    public ItemRecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_row, parent, false);
        return new ItemViewHolder(view,
                                  itemRecyclerInterface,
                                  this.postType);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemRecyclerAdapter.ItemViewHolder holder, int position) {
        if (postType.equals("lost")) {
            LostModel item = lostItems.get(position);
            holder.itemNameView.setText("Lost " + item.getName());
        } else if (postType.equals("found")) {
            FoundModel item = foundItems.get(position);
            holder.itemNameView.setText("Found " + item.getName());
        }
    }

    @Override
    public int getItemCount() {
        if (postType.equals("lost")) {
            return lostItems.size();
        } else if (postType.equals("found")) {
            return foundItems.size();
        } else {return 0;}
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameView;
        public ItemViewHolder(@NonNull View itemView,
                              ItemRecyclerInterface itemRecyclerInterface,
                              String postType) {
            super(itemView);
            itemNameView = itemView.findViewById(R.id.item_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemRecyclerInterface != null) {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {
                            itemRecyclerInterface.onItemClick(position, postType);
                        }
                    }
                }
            });
        }
    }
}
