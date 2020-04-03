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

package cl.ucn.disc.dsm.chatdisc.Adapter;

/**
 * @author Martin Osorio-Bugueño.
 */

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cl.ucn.disc.dsm.chatdisc.MessageActivity;
import cl.ucn.disc.dsm.chatdisc.Model.Chat;
import cl.ucn.disc.dsm.chatdisc.Model.User;
import cl.ucn.disc.dsm.chatdisc.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

  private Context mContext;
  private List<User> mUsers ;
  private boolean ischat;

  String theLastMessage;



  //Constructor
  public UserAdapter(Context mContext, List<User> mUsers, boolean ischat) {
    this.mUsers = mUsers;
    this.mContext = mContext;
    this.ischat = ischat;


  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    //inflate layout(user_item.xml)
    View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
    return new UserAdapter.ViewHolder(view);

  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    //get data
    final User user = mUsers.get(position);
    //set data
    holder.username.setText(user.getUsername());
    //if user image is default
    if(user.getImageURL().equals("default")){
      //asigned the dafault image for user
      holder.profile_image.setImageResource(R.mipmap.ic_launcher);
    }else{
      //if user have another image
      Glide.with(mContext).load(user.getImageURL()).into(holder.profile_image);
    }
    //if ischat,show the last message
    if (ischat){
      lastMessage(user.getId(), holder.last_msg);
    } else {
      ///if not,this won't show message
      holder.last_msg.setVisibility(View.GONE);
    }
    //if ischat
    if (ischat){
      //if user is online
      if (user.getStatus().equals("online")){
        //img_on is visible
        holder.img_on.setVisibility(View.VISIBLE);
        holder.img_off.setVisibility(View.GONE);
      } else {
        //img_on isnt visible
        holder.img_on.setVisibility(View.GONE);
        holder.img_off.setVisibility(View.VISIBLE);
      }
    } else {
      //if not ischat
      holder.img_on.setVisibility(View.GONE);
      holder.img_off.setVisibility(View.GONE);
    }

    //handle item click
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(mContext, MessageActivity.class);
        intent.putExtra("userid", user.getId());
        mContext.startActivity(intent);

      }
    });

  }



  @Override
  public int getItemCount() {
    return mUsers.size();
  }

  //View Holder Class
  public class ViewHolder extends RecyclerView.ViewHolder{
    //views from xml
    public TextView username;
    public ImageView profile_image;
    private TextView last_msg;
    ImageView img_on;
    ImageView img_off;


    public ViewHolder(@NonNull View itemView) {
      super(itemView);

      //Init Views
      username = itemView.findViewById(R.id.username);
      profile_image = itemView.findViewById(R.id.profile_image);
      img_on = itemView.findViewById(R.id.img_on);
      img_off = itemView.findViewById(R.id.img_off);
      last_msg = itemView.findViewById(R.id.last_msg);


    }
  }
  // last message method
  private void lastMessage(final String userid, final TextView last_msg){
    theLastMessage = "default";
    //Firebase get instance and currentUser
    final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    //get the Chats in the DataBase
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

    reference.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
          Chat chat = snapshot.getValue(Chat.class);
          //if firebaseUser and chat are not null
          if (firebaseUser != null && chat != null) {
            //validation
            if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())) {
              //get the last message
              theLastMessage = chat.getMessage();
            }
          }
        }

        switch (theLastMessage){
          case  "default":
            last_msg.setText("No Message");
            break;

          default:
            last_msg.setText(theLastMessage);
            break;
        }

        theLastMessage = "default";
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }
}
