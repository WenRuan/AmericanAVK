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

package com.tensorflow.AVK.CS426.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.tensorflow.lite.examples.demo.R;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    Button logInButton, createButton;
    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logInButton = findViewById(R.id.button4);
        createButton = findViewById(R.id.CreateAccountbutton);
        username = findViewById(R.id.UsernameeditText);
        password = findViewById(R.id.PasswordeditText);

        logInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                //Variables for database and entered text
                DatabaseAccess db = DatabaseAccess.getInstance(getApplicationContext());
                db.open();
                ArrayList<String> userList = db.getUserNames();
                ArrayList<String> pwList = db.getPasswords();
                String userText = username.getText().toString();
                String passText = password.getText().toString();

                //First check if username exists using check function created below
                if(checkExist(userText, userList) != -1){
                    if(passText.equals(pwList.get(checkExist(userText, userList)))){
                        menuScreen();
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(LoginActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();
                }
            db.close();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuScreen();
            }
        });

    }
    public void menuScreen() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    public int checkExist(String string, ArrayList<String> list){
        //Loop through array to check match
        for(int index = 0; index < list.size(); index++)
        {
            if(list.get(index).equals(string)){
                return index;
            }
        }
        return -1;
    }


}
