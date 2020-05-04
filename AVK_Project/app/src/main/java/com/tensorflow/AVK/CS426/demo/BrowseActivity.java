/*
 * Copyright 2020 CS426 AVK Automation Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @file BrowseActivity.java
 *
 * @brief Shows the database of hydrants.
 *
 */

package com.tensorflow.AVK.CS426.demo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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
    MyCustAdapter myAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        add_name = findViewById(R.id.add_name);
        add_desc = findViewById(R.id.add_desc);
        add_link = findViewById(R.id.add_link);
        manual_list = findViewById(R.id.users_list);
        add_button = findViewById(R.id.add_data);

        browseActivity();

        manual_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                String text = manual_list.getItemAtPosition(i).toString();
                Log.d("testing",text);
                Log.d("testing", manual_list.getItemAtPosition(i).toString());
                DatabaseAccess db = DatabaseAccess.getInstance(getApplicationContext());
                db.open();
                String manLink = db.getLink(text);
                openWebActivity(manLink);
                db.close();
            }
        });

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String name = add_name.getText().toString();
                String desc = add_desc.getText().toString();
                //String prod = add_prod.getText().toString();
                String links = add_link.getText().toString();
                DatabaseAccess db = DatabaseAccess.getInstance(getApplicationContext());
                db.open();
                if(!name.equals("") && db.addToDatabase(name, desc, links)) {
                    Toast.makeText(BrowseActivity.this, "Data added", Toast.LENGTH_SHORT).show();
                    add_name.setText("");
                    add_desc.setText("");
                    add_link.setText("");
                    browseActivity();
                }
                else {
                    Toast.makeText(BrowseActivity.this, "Data not added", Toast.LENGTH_SHORT).show();
                }
                db.close();
            }
        });

    }

    //Fill the ListView with the manual names and descriptions as subtext
    public void browseActivity() {

        DatabaseAccess db = DatabaseAccess.getInstance(getApplicationContext());
        db.open();
        ArrayList<String> modelList = db.getModelName();
        db.close();
        db.open();
        ArrayList<String> descList = db.getDescription();
        db.close();
        ArrayList<HydrantInfo> hydrantList = new ArrayList<>();

        for(int index = 0; index < modelList.size(); index++)
        {
            Log.d("testing", "Its working!!!");
            HydrantInfo hydrant = new HydrantInfo(modelList.get(index), descList.get(index));
            hydrantList.add(hydrant);
        }

        myAdapter = new MyCustAdapter(this, hydrantList);
        manual_list.setAdapter(myAdapter);

    }

//    public void browseActivity() {
//
//        DatabaseAccess db = DatabaseAccess.getInstance(getApplicationContext());
//        db.open();
//        ArrayList<String> nameList = db.getModelName();
//        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, nameList);
//        manual_list.setAdapter(adapter);
//
//        db.close();
//
//    }

    public void openWebActivity(String manual_link){
        Intent intent = new Intent(this, ManualActivity.class);
        intent.putExtra("MANUAL_LINK", manual_link);
        startActivity(intent);
    }


}
