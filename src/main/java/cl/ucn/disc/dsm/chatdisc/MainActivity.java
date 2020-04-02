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
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import cl.ucn.disc.dsm.chatdisc.Fragments.ChatsFragment;
import cl.ucn.disc.dsm.chatdisc.Fragments.ProfileFragment;
import cl.ucn.disc.dsm.chatdisc.Fragments.UsersFragment;
import cl.ucn.disc.dsm.chatdisc.Model.User;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
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

public class MainActivity extends AppCompatActivity {

  //views
  CircleImageView profile_image;
  TextView username;

  //Declare am instance of FirebaseUser
  FirebaseUser firebaseUser;
  DatabaseReference reference;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    //action bar
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle("");

    //Init
    profile_image = findViewById(R.id.profile_image);
    username = findViewById(R.id.username);


    //fireBase Conection
    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());


    reference.addValueEventListener(new ValueEventListener(){

      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        User user = dataSnapshot.getValue(User.class);
        username.setText(user.getUsername());
        if (user.getImageURL().equals("default")){
          profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {

          //change this
          Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image);
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });

    TabLayout tabLayout =findViewById(R.id.tab_layout);
    ViewPager viewPager =findViewById(R.id.view_pager);

    ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
    //View pager Chat
    viewPagerAdapter.addFragment(new ChatsFragment(), " Chats");
    //View pager User
    viewPagerAdapter.addFragment(new UsersFragment(), "Users");
    //View pager Profile
    viewPagerAdapter.addFragment(new ProfileFragment(), "Profile");

    viewPager.setAdapter(viewPagerAdapter);

    tabLayout.setupWithViewPager(viewPager);
  }

  @Override
  /*Inflate options menu */
  public boolean onCreateOptionsMenu(Menu menu) {
    //Inflating menu
    getMenuInflater().inflate(R.menu.menu, menu);
    return true;
  }

  /* Handle menu items clicks */
  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()){
      //if press logout button
      case  R.id.logout:
        FirebaseAuth.getInstance().signOut();
        //warning podria crashear por culpa de esto,tratar de cambiar
        startActivity(new Intent(MainActivity.this, StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        return true;
        //return StartActivity
    }

    return false;
  }
 class ViewPagerAdapter extends FragmentPagerAdapter {

   private ArrayList<Fragment> fragments;
   private ArrayList<String> titles;

   ViewPagerAdapter(FragmentManager fm) {
     super(fm);
     this.fragments = new ArrayList<>();
     this.titles = new ArrayList<>();
   }

   @Override
   public Fragment getItem(int position) {
     return fragments.get(position);
   }

   @Override
   public int getCount() {
     return fragments.size();
   }

   public void addFragment(Fragment fragment, String title) {
     fragments.add(fragment);
     titles.add(title);
   }

   @Nullable
   @Override
   public CharSequence getPageTitle(int position) {
     return titles.get(position);
   }
 }

  //If User is On status is online, if User is off status offline
  private void status(String status){
    reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
    //Using HashMap
    HashMap<String, Object> hashMap = new HashMap<>();
    //Put Info in HashMap
    hashMap.put("status", status);

    reference.updateChildren(hashMap);
  }
  //Status Online
  @Override
  protected void onResume() {
    super.onResume();
    status("online");
  }
  //Status Offline
  @Override
  protected void onPause() {
    super.onPause();
    status("offline");
  }
}