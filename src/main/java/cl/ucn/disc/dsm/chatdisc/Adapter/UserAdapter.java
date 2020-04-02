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





  public UserAdapter(Context mContext, List<User> mUsers, boolean ischat) {
    this.mUsers = mUsers;
    this.mContext = mContext;


  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
    return new UserAdapter.ViewHolder(view);

  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    final User user = mUsers.get(position);
    holder.username.setText(user.getUsername());
    if(user.getImageURL().equals("default")){
      holder.profile_image.setImageResource(R.mipmap.ic_launcher);
    }else{
      Glide.with(mContext).load(user.getImageURL()).into(holder.profile_image);
    }

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

  public class ViewHolder extends RecyclerView.ViewHolder{

    public TextView username;
    public ImageView profile_image;


    public ViewHolder(@NonNull View itemView) {
      super(itemView);

      username = itemView.findViewById(R.id.username);
      profile_image = itemView.findViewById(R.id.profile_image);


    }
  }




}
