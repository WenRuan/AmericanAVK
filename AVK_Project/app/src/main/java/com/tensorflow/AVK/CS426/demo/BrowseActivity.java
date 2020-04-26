package com.tensorflow.AVK.CS426.demo;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tensorflow.AVK.CS426.demo.env.Logger;

import org.tensorflow.lite.examples.demo.R;

import java.util.ArrayList;
import java.util.List;

public class BrowseActivity extends AppCompatActivity {

    public EditText add_name, add_desc, add_link;
    public Button add_button;
    public ListView manual_list;

    ArrayList<String> listItem;
    ArrayAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        add_name = findViewById(R.id.add_name);
        add_desc = findViewById(R.id.add_desc);
        add_link = findViewById(R.id.add_link);
        manual_list = findViewById(R.id.users_list);

        browseActivity();

        manual_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                String text = manual_list.getItemAtPosition(i).toString();
                DatabaseAccess db = DatabaseAccess.getInstance(getApplicationContext());
                db.open();
                String manLink = db.getLink(text);
                openWebActivity(manLink);
                db.close();
            }
        });

    }

    //Fill the ListView with the manual names and descriptions as subtext
    public void browseActivity() {

        DatabaseAccess db = DatabaseAccess.getInstance(getApplicationContext());
        db.open();
        ArrayList<String> nameList = db.getModelName();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, nameList);
        manual_list.setAdapter(adapter);

        db.close();

    }

    public void openWebActivity(String manual_link){
        Intent intent = new Intent(this, ManualActivity.class);
        intent.putExtra("MANUAL_LINK", manual_link);
        startActivity(intent);
    }


    //Allow adding to the database
}
