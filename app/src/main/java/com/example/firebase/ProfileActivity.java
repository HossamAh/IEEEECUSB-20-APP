package com.example.firebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebase.models.ChatNode;
import com.example.firebase.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private TextView Name, CommitteeName1, CommitteeName2, CommitteeName3, PositionName, privateChat, committeeChat1, committeeChat2, committeeChat3, clubChat;
    private ImageView ProfileImage;
    private String Profile_Uid, UserName, Committee2Name;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Users user;
    ProgressBar progressBar;
    private ArrayList<String> chatIDs;
    private static final String TAG = "ProfileActivity";
    private FrameLayout ChattingList;
    private Fragment mChattingFragment;
    private android.support.v7.widget.Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private TextView NavHeaderUserName;
    private ImageView NavHeaderUserImage;
    private Button ViewChatting;
    private View Line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        Line = findViewById(R.id.view5);
        ViewChatting = (Button)findViewById(R.id.ShowPreviousChatting_button);
        Name = (TextView) findViewById(R.id.UserName_textView);
        CommitteeName1 = (TextView) findViewById(R.id.UserCommittee1_textView);
        CommitteeName2 = (TextView) findViewById(R.id.userCommittee2_textView);
        PositionName = (TextView) findViewById(R.id.position_textView);
        privateChat = (TextView) findViewById(R.id.Message_textView);
        committeeChat1 = (TextView) findViewById(R.id.CommitteChat1_textView);
        committeeChat2 = (TextView) findViewById(R.id.committeChat2_textView);
        clubChat = (TextView) findViewById(R.id.IEEECUSB_Chat_textView);
        ProfileImage = (ImageView) findViewById(R.id.User_imageView);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");
        progressBar = (ProgressBar) findViewById(R.id.Profile_progressBar);
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        View Header = navigationView.getHeaderView(0);

        progressBar.setVisibility(ProgressBar.VISIBLE);
        Name.setVisibility(View.INVISIBLE);
        CommitteeName1.setVisibility(View.INVISIBLE);
        ProfileImage.setVisibility(View.INVISIBLE);
        privateChat.setVisibility(View.INVISIBLE);
        committeeChat1.setVisibility(View.INVISIBLE);
        clubChat.setVisibility(View.INVISIBLE);
        PositionName.setVisibility(View.INVISIBLE);
        Line.setVisibility(View.GONE);
        ViewChatting.setVisibility(View.GONE);

        NavHeaderUserName = (TextView)Header.findViewById(R.id.NavHeaderUserName_textView);
        NavHeaderUserImage=(ImageView)Header.findViewById(R.id.navHeaderImage);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final Bundle chattingListBundle = new Bundle();

        chatIDs = new ArrayList<>();





        ViewChatting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!chatIDs.isEmpty()) {
                    Log.e("ProfileActivity", "check if user is the target or not :" + user.getUser_Name());
                    Intent intent =new Intent(ProfileActivity.this,PreviousChatting.class);
                    intent.putExtras(chattingListBundle);
                    startActivity(intent);
                    }

            }
        });

        ValueEventListener profileUser = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                user = dataSnapshot.getValue(Users.class);
                UserInfo.sUserName = user.getUser_Name();
                UserInfo.sImageUrl=user.getUrl();
                UserInfo.USER_ID=dataSnapshot.getKey();
                UserInfo.sCommitteeName = user.getUser_Committee();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals("ChattingList")) {
                        Log.e("ProfileActivity", "user children data : " + snapshot.getValue().toString());
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            //Log.e("ProfileActivity","Chatting List data : "+snapshot1.getValue().toString());
                            chatIDs.add(snapshot1.getValue().toString());
                        }
                        //chatIDs = snapshot.getValue(ArrayList.class);
                        chattingListBundle.putStringArrayList("ChattingIDs", chatIDs);
                    }
                }
                for (String ID : chatIDs) {
                    Log.e("ProfileActivity", "Chatting List data : " + ID);
                }

                if (user != null) {
                    UpdateUI();

                } else
                    Toast.makeText(ProfileActivity.this, "user is null ", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };


        privateChat.setText("Private Chatting");
        privateChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                intent.putExtra("ProfileUID", Profile_Uid);
                intent.putExtra("chatType", 0);
                intent.putExtra("CommitteeName", "");
                intent.putExtra("ProfileUserName", user.getUser_Name());

                startActivity(intent);
            }
        });


        committeeChat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                intent.putExtra("ProfileUID", "");
                intent.putExtra("chatType", 1);
                intent.putExtra("CommitteeName", user.getUser_Committee());
                intent.putExtra("ProfileUserName", "");
                startActivity(intent);
            }
        });
        committeeChat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                intent.putExtra("ProfileUID", "");
                intent.putExtra("chatType", 1);
                intent.putExtra("CommitteeName", CommitteeName2.getText().toString());
                intent.putExtra("ProfileUserName", "");
                startActivity(intent);
            }
        });

        clubChat.setText("Club Chatting");
        clubChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                intent.putExtra("ProfileUID", "");
                intent.putExtra("chatType", 2);
                intent.putExtra("CommitteeName", "");
                intent.putExtra("ProfileUserName", "");
                startActivity(intent);
            }
        });

        CommitteeName1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, CommitteActivity.class).putExtra("CommitteeName", user.getUser_Committee()));
            }
        });
        CommitteeName2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, CommitteActivity.class).putExtra("CommitteeName", CommitteeName2.getText().toString()));
            }
        });


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            privateChat.setVisibility(View.VISIBLE);
            Profile_Uid = bundle.getString("UserID");
            databaseReference.child(Profile_Uid).addValueEventListener(profileUser);
//            ChattingList.setVisibility(View.GONE);
            committeeChat1.setVisibility(View.GONE);
            committeeChat2.setVisibility(View.GONE);
            clubChat.setVisibility(View.GONE);
            ViewChatting.setVisibility(View.GONE);
            if(user.getUser_Position().equals("member")) {
                navigationView.getMenu().removeItem(R.id.draw_creation_menu);
            }
            //Line.setVisibility(View.GONE);
        } else {
            Log.e("ProfileActivity", "auth user");
            privateChat.setVisibility(View.GONE);
            Profile_Uid = firebaseAuth.getCurrentUser().getUid();
            databaseReference.child(Profile_Uid).addValueEventListener(profileUser);


        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nav_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.draw_committees_menu:
                Intent intent = new Intent(ProfileActivity.this, CommitteesListActivity.class);
                startActivity(intent);
                break;
            case R.id.draw_Logout_menu:
                firebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                break;
        }

        return true;

    }

    private void UpdateUI() {
        progressBar.setVisibility(View.GONE);
        if (user.getUser_Position().equals("Chairman") || user.getUser_Position().equals("Chairman Vice")) {
            CommitteeName2.setVisibility(View.GONE);
            committeeChat2.setVisibility(View.GONE);
        } else {
            CommitteeName2.setVisibility(View.VISIBLE);
            committeeChat2.setVisibility(View.VISIBLE);
        }

        Name.setVisibility(View.VISIBLE);
        CommitteeName1.setVisibility(View.VISIBLE);
        ProfileImage.setVisibility(View.VISIBLE);
        PositionName.setVisibility(View.VISIBLE);
        Line.setVisibility(View.VISIBLE);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString("UserID").equals(firebaseAuth.getCurrentUser().getUid())) {
                privateChat.setVisibility(View.GONE);
                committeeChat1.setVisibility(View.VISIBLE);
                committeeChat2.setVisibility(View.VISIBLE);
                clubChat.setVisibility(View.VISIBLE);
                ChattingList.setVisibility(View.VISIBLE);
                Line.setVisibility(View.VISIBLE);
                ViewChatting.setVisibility(View.GONE);


            } else {
                committeeChat1.setVisibility(View.GONE);
                committeeChat2.setVisibility(View.GONE);
                clubChat.setVisibility(View.GONE);
                //Line.setVisibility(View.GONE);
                ViewChatting.setVisibility(View.GONE);

            }
        } else {
            committeeChat1.setVisibility(View.VISIBLE);
            committeeChat2.setVisibility(View.VISIBLE);
            clubChat.setVisibility(View.VISIBLE);
            Line.setVisibility(View.VISIBLE);
            ViewChatting.setVisibility(View.VISIBLE);

        }


        if (user.getUser_Position().equals("Head") || user.getUser_Position().equals("Vice") ||
                (user.getUser_Position().equals("Technical Manager") || user.getUser_Position().equals("Technical Vice"))
                || (user.getUser_Position().equals("Chairman") || user.getUser_Position().equals("Chairman Vice"))) {

            Log.e("ProfileActivity", user.getUser_Committee());
            CommitteeName1.setText(user.getUser_Committee());
            committeeChat1.setText(user.getUser_Committee() + " Chatting");
            FirebaseMessaging.getInstance().subscribeToTopic("HighBoard" + "_Chatting")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = "subscribe successful";
                            if (!task.isSuccessful()) {
                                msg = "subscribe failed";
                            }
                            Log.d(TAG, msg);
                        }
                    });
            FirebaseMessaging.getInstance().subscribeToTopic("HighBoard")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = "subscribe successful";
                            if (!task.isSuccessful()) {
                                msg = "subscribe failed";
                            }
                            Log.d(TAG, msg);
                        }
                    });

            CommitteeName2.setText("High Board");
            committeeChat2.setText("High Board" + " Chatting");
        } else if (user.getUser_Position().equals("Member")) {
            Log.e("ProfileActivity", "user is member inside the member condition");
            CommitteeName1.setText(user.getUser_Committee());
            committeeChat1.setText(user.getUser_Committee() + " Chatting");
            CommitteeName2.setVisibility(View.GONE);
            committeeChat2.setVisibility(View.GONE);
        }
        FirebaseMessaging.getInstance().subscribeToTopic(user.getUser_Committee().replaceAll("\\s", "") + "_Chatting")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "subscribe successful";
                        if (!task.isSuccessful()) {
                            msg = "subscribe failed";
                        }
                        Log.d(TAG, msg);
                    }
                });

        FirebaseMessaging.getInstance().subscribeToTopic(user.getUser_Committee().replaceAll("\\s", ""))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "subscribe successful";
                        if (!task.isSuccessful()) {
                            msg = "subscribe failed";
                        }
                        Log.d(TAG, msg);
                    }
                });

        FirebaseMessaging.getInstance().subscribeToTopic("IEEECUSB")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "subscribe successful";
                        if (!task.isSuccessful()) {
                            msg = "subscribe failed";
                        }
                        Log.d(TAG, msg);
                    }
                });


        Name.setText(user.getUser_Name());
        PositionName.setText(user.getUser_Position());

        if (user.getUrl() == null)
            ProfileImage.setImageResource(R.drawable.defaultprofile);
        else {
            Picasso.get().load(user.getUrl()).
                    transform(new CircleTransform()).into(ProfileImage);
            Picasso.get().load(user.getUrl()).
                    transform(new CircleTransform()).into(NavHeaderUserImage);
        }
        NavHeaderUserName.setText(user.getUser_Name());
    }


    private void sendRegistrationToServer(String token) {
        Log.d(TAG, "sendRegistrationToServer: sending token to server: " + token);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Messaging_Token")
                .setValue(token);
    }


    private void initFCM() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "initFCM: token: " + token);
        sendRegistrationToServer(token);

    }

    @Override
    protected void onStart() {
        super.onStart();
        initFCM();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if(user.getUser_Position().equals("member")) {
            navigationView.getMenu().findItem(R.id.draw_creation_menu).setEnabled(false);
            switch (menuItem.getItemId()) {
                case R.id.draw_notifications_menu://display
                    Intent intent1 = new Intent(ProfileActivity.this, AnnouncementActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.draw_creation_menu:
                    Toast.makeText(getBaseContext(),"you can't use this feature",Toast.LENGTH_LONG).show();
                    break;
                case R.id.draw_committees_menu:
                    Intent intent = new Intent(ProfileActivity.this, CommitteesListActivity.class);
                    startActivity(intent);
                    break;
                case R.id.draw_Logout_menu:
                    firebaseAuth.getInstance().signOut();
                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                    break;
            }
        }
        else
        {
            switch (menuItem.getItemId()) {
                case R.id.draw_notifications_menu://display
                    Intent intent = new Intent(ProfileActivity.this, AnnouncementActivity.class);
                    startActivity(intent);
                    break;
                case R.id.draw_creation_menu:
                    startActivity(new Intent(ProfileActivity.this,Create.class));
                    break;
                case R.id.draw_committees_menu:
                    Intent intent1 = new Intent(ProfileActivity.this, CommitteesListActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.draw_Logout_menu:
                    firebaseAuth.getInstance().signOut();
                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                    break;
            }
        }
        return true;
    }

}