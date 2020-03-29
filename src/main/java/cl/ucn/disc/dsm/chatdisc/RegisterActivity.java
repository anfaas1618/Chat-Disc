package cl.ucn.disc.dsm.chatdisc;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.rengwuxian.materialedittext.MaterialEditText;

public class RegisterActivity extends AppCompatActivity {

  MaterialEditText username, email, password;
  Button btn_register;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle("Register");
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    username = findViewById(R.id.username);
    email = findViewById(R.id.email);
    password = findViewById(R.id.password);
    btn_register = findViewById(R.id.btn_register);


    btn_register.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        String txt_username = username.getText().toString();
        String txt_email = email.getText().toString();
        String txt_password = password.getText().toString();

        if(TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
          Toast.makeText(RegisterActivity.this, "All Filed are required", Toast.LENGTH_SHORT).show();
        }else if(txt_password.length()< 6) {
          Toast.makeText(RegisterActivity.this, "password must be at least 6 characters",
              Toast.LENGTH_SHORT).show();

          //else poner la restriccion si es igual en la base de datos 2 15:21
        }else{
          Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
          startActivity(intent);
          finish();
        }


      }
    });


  }
}