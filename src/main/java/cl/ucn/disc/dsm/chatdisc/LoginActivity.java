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

/**
 * @author Martin Osorio-Bugueño.
 */

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LoginActivity extends AppCompatActivity {

  //views
  MaterialEditText email, password;
  Button btn_login;

  //Declare am instance of FirebaseAuth
  FirebaseAuth auth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    //Action and its tittle
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle("Login");
    //enable back button
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    auth = FirebaseAuth.getInstance();

    //init
    email = findViewById(R.id.email);
    password = findViewById(R.id.password);
    btn_login = findViewById(R.id.btn_login);


    //login button click
    btn_login.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //input data
        String txt_email = email.getText().toString();
        String txt_password = password.getText().toString();

        //if email or password is empty ,shows a toast whit the error
        if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
          Toast.makeText(LoginActivity.this, "All fileds are required", Toast.LENGTH_SHORT).show();
        } else {
          //valid email pattern
          auth.signInWithEmailAndPassword(txt_email, txt_password)
              .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                  if (task.isSuccessful()){
                    //sign success go to MainActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                  } else {
                    //if email and his password are not in the DataBase,show a toast whit the error
                    Toast.makeText(LoginActivity.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
                  }
                }
              });
        }
      }
    });
  }
}