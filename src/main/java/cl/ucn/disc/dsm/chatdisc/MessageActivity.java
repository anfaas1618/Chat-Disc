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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cl.ucn.disc.dsm.chatdisc.Adapter.MessageAdapter;
import cl.ucn.disc.dsm.chatdisc.Model.Chat;
import cl.ucn.disc.dsm.chatdisc.Model.User;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    //views
    CircleImageView profile_image;
    TextView username;

    //FireBase
    FirebaseUser fuser;
    DatabaseReference reference;

    //Button
    ImageButton btn_send;

    EditText text_send;

    //MessageAdapter class
    MessageAdapter messageAdapter;

    //List
    List<Chat> mchat;

    RecyclerView recyclerView;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        //init views
        Toolbar toolbar = findViewById(R.id.toolbar);
        //set toolbar
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( MessageActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

            }
        });
        //assigned the id of the RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        //assigned The id of the TextView,Image and button in the layout xml
        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        btn_send =findViewById(R.id.btn_send);
        text_send =findViewById(R.id.text_send);

        intent = getIntent();
        final String userid = intent.getStringExtra("userid");


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the message
                String msg = text_send.getText().toString();
                if (!msg.equals("")){
                    //send the message
                    sendMessage(fuser.getUid(), userid, msg);
                } else {
                    //if there is a empty message
                    Toast.makeText(MessageActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                //set the text in the message
                text_send.setText("");
            }
        });
        // the current user
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        //reference users in teh fire base
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                //if user dont have a profile image
                if (user.getImageURL().equals("default")){
                    //the image for default
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                }else{
                    //if user have a profile image
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image);
                }
                //read  the messag whit his profile image
                readMesagges(fuser.getUid(),userid, user.getImageURL());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void sendMessage(String sender, String receiver, String message){
        //data base get instance and reference
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        final String userid = intent.getStringExtra("userid");
        String timestamp = String.valueOf(System.currentTimeMillis());

        //using HashMap
        HashMap<String, Object> hashMap = new HashMap<>();
        //Put info in HashMap
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message" , message);
        hashMap.put("timestamp", timestamp);

        reference.child("Chats").push().setValue(hashMap);
        //Reference ChatList in firebase
        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
            .child(fuser.getUid())
            .child(userid);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    //set the Id
                    chatRef.child("id").setValue(userid);
                }
            }


        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });


    }
    // Read Message
    private void readMesagges(final String myid, final String userid, final String imageurl){
        mchat = new ArrayList<>();
        //Reference Chats in the FireBase
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        //Add Values
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    //Get values in the Chat Class
                    Chat chat = snapshot.getValue(Chat.class);
                    //if the Id match
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                        chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                        //add
                        mchat.add(chat);
                    }
                    //Adapter
                    messageAdapter = new MessageAdapter(MessageActivity.this, mchat, imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void status(String status){
        //Refrence in Users in fireBase
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        //put status in the HashMap
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }

    @Override
    //if is online
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    //if is offline
    protected void onPause() {
        super.onPause();
        status("offline");
    }
}

