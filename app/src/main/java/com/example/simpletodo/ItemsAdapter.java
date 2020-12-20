package com.example.simpletodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// rsponsible for displaying data from the model into a row in the recycler viewer
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder>{

    public interface OnLongClickListener{
        void onItemLongClicked(int position);
    }

    public interface OnClickListener{
        void onItemClicked(int position);
    }

    List<String> items;
    OnLongClickListener longClickListener;
    OnClickListener clickListener;
    public ItemsAdapter(List<String> itemList, OnLongClickListener longClickListener, OnClickListener clickListener) {
        items = itemList;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // use layout inflator to inflate the view
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        // wrap it inside the view holder and return it
        return new ViewHolder(todoView);
    }

    // respomsible for binding data to a particular view holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // grab item at the position
        String item = items.get(position);
        // bind item to a specific view holder
        holder.bind(item);
    }

    // tells recycler view how many items are present
    @Override
    public int getItemCount() {
        return items.size();
    }

    // container to provide easy access to views that represent each row of the list
    class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView tvItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }

        // update the view inside the view holder with the data of String item
        public void bind(String item) {
            tvItem.setText(item);

            // remove item on long press
            tvItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Notify the listener which position was long pressed
                    longClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });

            // for editing the items
            tvItem.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });
        }
    }
}
