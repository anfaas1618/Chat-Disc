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

package cl.ucn.disc.dsm.chatdisc.Fragments;

import android.media.session.MediaSession.Token;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cl.ucn.disc.dsm.chatdisc.Adapter.UserAdapter;
import cl.ucn.disc.dsm.chatdisc.Model.Chat;
import cl.ucn.disc.dsm.chatdisc.Model.Chatlist;
import cl.ucn.disc.dsm.chatdisc.Model.User;
import cl.ucn.disc.dsm.chatdisc.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

  private RecyclerView recyclerView;

  private UserAdapter userAdapter;
  private List<User> mUsers;

  FirebaseUser fuser;
  DatabaseReference reference;

  private List<Chatlist> usersList;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_chats, container, false);

    recyclerView = view.findViewById(R.id.recycler_view);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    fuser = FirebaseAuth.getInstance().getCurrentUser();

    usersList = new ArrayList<>();

    reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(fuser.getUid());
    reference.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        usersList.clear();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
          Chatlist chatlist = snapshot.getValue(Chatlist.class);
          usersList.add(chatlist);
        }

        chatList();
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });


    return view;
  }


  private void chatList() {
    mUsers = new ArrayList<>();
    reference = FirebaseDatabase.getInstance().getReference("Users");
    reference.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        mUsers.clear();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
          User user = snapshot.getValue(User.class);
          for (Chatlist chatlist : usersList){
            if (user.getId().equals(chatlist.getId())){
              mUsers.add(user);
            }
          }
        }
        userAdapter = new UserAdapter(getContext(), mUsers, true);
        recyclerView.setAdapter(userAdapter);
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }

}
