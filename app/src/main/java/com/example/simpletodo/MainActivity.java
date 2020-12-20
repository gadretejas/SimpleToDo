package com.example.simpletodo;
import org.apache.commons.io.FileUtils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
// import android.os.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;
    List<String> items;

    Button btnAdd;
    EditText etItems;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        etItems = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);

        loadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener(){
            @Override
            public void onItemLongClicked(int position) {
                // delete the item from the model
                items.remove(position);
                // notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                // small message toast
                Toast.makeText(getApplicationContext(), "Item removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                // create new edit activity
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                // pass data to edit activity
                i.putExtra(KEY_ITEM_TEXT, items.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);
                // display the activity
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };

        itemsAdapter =  new ItemsAdapter(items, onLongClickListener, onClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = etItems.getText().toString();

                // add item to model
                items.add(todoItem);
                // notify adapter that item is inserted
                itemsAdapter.notifyItemInserted(items.size()-1);
                // set text box text to null string
                etItems.setText("");
                // small message toast to show item was added
                Toast.makeText(getApplicationContext(), "Item added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }

    // handle the result of other activity
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE)
        {
            // get updated text
            String newText = data.getStringExtra(KEY_ITEM_TEXT);
            // get original position of edited item from key position key
            int pos = data.getExtras().getInt(KEY_ITEM_POSITION);
            // update the model at the correct position
            items.set(pos, newText);
            // notify adapter
            itemsAdapter.notifyItemChanged(pos);
            // persist the changes
            saveItems();
            Toast.makeText(getApplicationContext(), "Item updated", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Log.w("MainActivity", "Unknown call to Main Activity");
        }
    }

    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }

    // this function will load items from the file
    private void loadItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error Reading Items", e);
            items = new ArrayList<>();
        }
    }
    // this function saves items to the file
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error Writing Items", e);
        }
    }
}