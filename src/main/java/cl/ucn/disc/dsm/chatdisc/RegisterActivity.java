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

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

  //views
  MaterialEditText username, email, password;
  Button btn_register;

  //Declare am instance of FirebaseAuth
  FirebaseAuth auth;
  DatabaseReference reference;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);

    //action bar and its title
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle("Register");
    //enable back button
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    //init
    username = findViewById(R.id.username);
    email = findViewById(R.id.email);
    password = findViewById(R.id.password);
    btn_register = findViewById(R.id.btn_register);

    //In the onCreate() method, initialize the FirebaseAuth.instance.
    auth = FirebaseAuth.getInstance();


    //handle register
    btn_register.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
       //input email, password
        String txt_username = username.getText().toString();
        String txt_email = email.getText().toString();
        String txt_password = password.getText().toString();

        //validate
        if(TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
          //Set error
          Toast.makeText(RegisterActivity.this, "All Filed are required", Toast.LENGTH_SHORT).show();
        }else if(txt_password.length()< 6) {
          //Set error
          Toast.makeText(RegisterActivity.this, "password must be at least 6 characters",
              Toast.LENGTH_SHORT).show();


        }else{
          //register the user
          register(txt_username, txt_email, txt_password);
        }


      }
    });


  }


  private void register(final String username, String email, String password){
    //email password and username pattern is valid, show progress dialog and start registering user

    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()){
              //sign in success
              FirebaseUser firebaseUser = auth.getCurrentUser();
              assert firebaseUser != null;
              String userid = firebaseUser.getUid();

              reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

              HashMap<String, String> hashMap = new HashMap<>();
              hashMap.put("id", userid);
              hashMap.put("username", username);
              hashMap.put("imageURL", "default");


              reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                  if (task.isSuccessful()){
                    //sign success go to MainActivity
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                  }
                }
              });
            } else {
              //if sign in fails, display a message to the user.
              Toast.makeText(RegisterActivity.this, "You can't register with this email or password", Toast.LENGTH_SHORT).show();
            }
          }
        });
  }
}