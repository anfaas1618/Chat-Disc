/*
 * Copyright [2020] [Martin Osorio Bugueño]
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package cl.ucn.disc.dsm.chatdisc;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {

  //views
  Button login, register;

  //Declare am instance of FirebaseUser
  FirebaseUser firebaseUser;

  @Override
  protected void onStart() {
    super.onStart();

    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    //check if user is null
    if (firebaseUser != null){
      Intent intent = new Intent(StartActivity.this, MainActivity.class);
      startActivity(intent);
      finish();
    }
  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_start);

    //init views
    login = findViewById(R.id.login);
    register = findViewById(R.id.register);

    //handle login button click
    login.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //start LoginActivity
        startActivity(new Intent(StartActivity.this, LoginActivity.class));

      }
    });

    register.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View view) {
        //Start RegisterActivity
        startActivity(new Intent(StartActivity.this,RegisterActivity.class));

      }
    });


    }
  }



