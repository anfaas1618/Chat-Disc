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
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cl.ucn.disc.dsm.chatdisc.Model.Chat;
import cl.ucn.disc.dsm.chatdisc.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
  //Message Type
  public static  final int MSG_TYPE_LEFT = 0;
  public static  final int MSG_TYPE_RIGHT = 1;

  //The chat whit his context and profile image
  private Context mContext;
  private List<Chat> mChat;
  private String imageurl;

  //FireBase
  FirebaseUser fuser;

  //Constructor
  public MessageAdapter(Context mContext, List<Chat> mChat, String imageurl){
    this.mChat = mChat;
    this.mContext = mContext;
    this.imageurl = imageurl;
  }

  @NonNull
  @Override
  public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    //Inflate layouts: row_chat_left.xml for receiver, row_chat_right.xml fir sender
    if (viewType == MSG_TYPE_RIGHT) {
      View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
      return new MessageAdapter.ViewHolder(view);
    } else {
      View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
      return new MessageAdapter.ViewHolder(view);
    }
  }

  @Override
  public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
    //get data
    Chat chat = mChat.get(position);
    String timeStamp = mChat.get(position).getTimestamp();

    //convert time stamp to dd/mm/yyyy hh:mm am/pm
    Calendar cal = Calendar.getInstance(Locale.ENGLISH);
    cal.setTimeInMillis(Long.parseLong(timeStamp));
    String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString();



    //set data
    holder.show_message.setText(chat.getMessage());
    holder.timeTv.setText(dateTime);

    if (imageurl.equals("default")){
      holder.profile_image.setImageResource(R.mipmap.ic_launcher);
    } else {
      Glide.with(mContext).load(imageurl).into(holder.profile_image);
    }
    //set seen/delivered status of message
    if (position == mChat.size()-1){
      if (chat.isIsseen()){
        holder.txt_seen.setText("Seen");
      } else {
        holder.txt_seen.setText("Delivered");
      }
    } else {
      holder.txt_seen.setVisibility(View.GONE);
    }

  }

  @Override
  public int getItemCount() {
    return mChat.size();
  }

  //view holder class
  public  class ViewHolder extends RecyclerView.ViewHolder{
    //views
    public TextView show_message;
    public ImageView profile_image;
    public TextView txt_seen;
    public TextView timeTv;

    public ViewHolder(View itemView) {
      super(itemView);

      //init views
      show_message = itemView.findViewById(R.id.show_message);
      profile_image = itemView.findViewById(R.id.profile_image);
      txt_seen = itemView.findViewById(R.id.txt_seen);
      timeTv = itemView.findViewById(R.id.timeTv);
    }
  }

  @Override
  public int getItemViewType(int position) {
    fuser = FirebaseAuth.getInstance().getCurrentUser();
    //Depend of the user who has send the message
    if (mChat.get(position).getSender().equals(fuser.getUid())){
      return MSG_TYPE_RIGHT;
    } else {
      return MSG_TYPE_LEFT;
    }
  }
}