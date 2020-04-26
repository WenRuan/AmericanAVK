package com.tensorflow.AVK.CS426.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.tensorflow.lite.examples.demo.R;

import java.util.ArrayList;

public class LegacyBrowseActivity extends AppCompatActivity {

    LegacyDatabaseHelper db;

    Button add_data;
    EditText add_name, add_desc, add_link;

    ListView users_list;

    ArrayList<String> listItem;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        db = new LegacyDatabaseHelper(this);

        listItem = new ArrayList<>();

        add_data = findViewById(R.id.add_data);
        add_name = findViewById(R.id.add_name);
        add_desc = findViewById(R.id.add_desc);
        add_link = findViewById(R.id.add_link);
        users_list = findViewById(R.id.users_list);

        browseActivity();

        users_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                String text = users_list.getItemAtPosition(i).toString();
                Toast.makeText(LegacyBrowseActivity.this, "" + text, Toast.LENGTH_SHORT).show();
            }
        });

        add_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String name = add_name.getText().toString();
                String desc = add_desc.getText().toString();
                //String prod = add_prod.getText().toString();
                String links = add_link.getText().toString();
                if(!name.equals("") && db.insertData(name, desc, links)) {
                    Toast.makeText(LegacyBrowseActivity.this, "Data added", Toast.LENGTH_SHORT).show();
                    add_name.setText("");
                    add_desc.setText("");
                    //add_prod.setText("");
                    add_link.setText("");
                    listItem.clear();
                    browseActivity();
                }
                else {
                    Toast.makeText(LegacyBrowseActivity.this, "Data not added", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*
        setContentView(R.layout.activity_browse);
        browseActivity();*/
    }

    public void openHydrant(View view) {
        Intent intent = new Intent(this, HydrantActivity.class);
        startActivity(intent);
    }

    public void browseActivity() {
        Cursor cursor = db.browseActivity();
        if(cursor.getCount() == 0) {
            Toast.makeText(this, "No data to show", Toast.LENGTH_SHORT).show();
        }
        else {
            while(cursor.moveToNext()) {
                listItem.add(cursor.getString(0));
            }

            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItem);
            users_list.setAdapter(adapter);
        }
    }
}
