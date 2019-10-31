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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
private RelativeLayout datalayout;
private LinearLayout chattingLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        chattingLayout =  findViewById(R.id.chattingsLayout);
        datalayout = findViewById(R.id.dataLayout);

        chattingLayout.setVisibility(View.INVISIBLE);
        datalayout.setVisibility(View.INVISIBLE);

  //      Line = findViewById(R.id.view5);
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
                else
                    {
                        Toast.makeText(getBaseContext(),"No Previous Chats",Toast.LENGTH_LONG).show();
                    }

            }
        });

        ValueEventListener profileUser = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                user = dataSnapshot.getValue(Users.class);
                if(dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    UserInfo.sUserName = user.getUser_Name();
                    UserInfo.sImageUrl = user.getUrl();
                    UserInfo.USER_ID = dataSnapshot.getKey();
                    UserInfo.sCommitteeName = user.getUser_Committee();
                    UserInfo.sPosition = user.getUser_Position();

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
                }

                if (user != null) {
                    UpdateUI();
                }
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
                String Committee2 = "";
                if(CommitteeName1.getText().toString().split(" ")[1].equals("Section"))
                {
                    Committee2 = CommitteeName1.getText().toString().split(" ")[0]+" Committee";
                }
                else
                {
                    Committee2=CommitteeName1.getText().toString();
                }
                intent.putExtra("CommitteeName", Committee2);
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
                String Committee2 = "";
                if(CommitteeName2.getText().toString().split(" ")[1].equals("Section"))
                {
                    Committee2 = CommitteeName2.getText().toString().split(" ")[0]+" Committee";
                }
                else
                {
                    Committee2=CommitteeName2.getText().toString();
                }
                intent.putExtra("CommitteeName",Committee2 );
                intent.putExtra("ProfileUserName", "");
                startActivity(intent);
            }
        });

        clubChat.setText("Volunteers Chatting");
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
                String Committee2 = "";
                if(CommitteeName1.getText().toString().split(" ")[1].equals("Section"))
                {
                    Committee2 = CommitteeName1.getText().toString().split(" ")[0]+" Committee";
                }
                else
                {
                    Committee2=CommitteeName1.getText().toString();
                }
                startActivity(new Intent(ProfileActivity.this, CommitteActivity.class).putExtra("CommitteeName", Committee2));
            }
        });
        CommitteeName2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Committee2 = "";
                if(CommitteeName2.getText().toString().split(" ")[1].equals("Section"))
                {
                    Committee2 = CommitteeName2.getText().toString().split(" ")[0]+" Committee";
                }
                else
                    {
                        Committee2=CommitteeName2.getText().toString();
                    }

                startActivity(new Intent(ProfileActivity.this, CommitteActivity.class).putExtra("CommitteeName", Committee2));
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
            //Line.setVisibility(View.GONE);
        } else {
            Log.e("ProfileActivity", "auth user");
            privateChat.setVisibility(View.GONE);
            Profile_Uid = firebaseAuth.getCurrentUser().getUid();
            databaseReference.child(Profile_Uid).addValueEventListener(profileUser);


        }


    }


    private void UpdateUI() {
        progressBar.setVisibility(View.GONE);
        chattingLayout.setVisibility(View.VISIBLE);
        datalayout.setVisibility(View.VISIBLE);

        if (user.getUser_Position().equals("Chairman") || user.getUser_Position().equals("Chairman Vice") || user.getUser_Position().equals("Member")) {
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
    //    Line.setVisibility(View.VISIBLE);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {//not auth user
            if (bundle.getString("UserID").equals(firebaseAuth.getCurrentUser().getUid())) {
                privateChat.setVisibility(View.GONE);
                committeeChat1.setVisibility(View.VISIBLE);
                committeeChat2.setVisibility(View.VISIBLE);
                clubChat.setVisibility(View.VISIBLE);
            //    ChattingList.setVisibility(View.VISIBLE);
                //Line.setVisibility(View.VISIBLE);
                ViewChatting.setVisibility(View.GONE);


            }
            else {
                committeeChat1.setVisibility(View.GONE);
                committeeChat2.setVisibility(View.GONE);
                clubChat.setVisibility(View.GONE);
                privateChat.setVisibility(View.VISIBLE);
                //Line.setVisibility(View.GONE);
                ViewChatting.setVisibility(View.GONE);

            }
        }
        else {//auth user
            committeeChat1.setVisibility(View.VISIBLE);
            committeeChat2.setVisibility(View.VISIBLE);
            clubChat.setVisibility(View.VISIBLE);
         //   Line.setVisibility(View.VISIBLE);
            ViewChatting.setVisibility(View.VISIBLE);
            privateChat.setVisibility(View.GONE);
        }


        if (user.getUser_Position().equals("Head") || user.getUser_Position().equals("Vice") ||
                (user.getUser_Position().equals("Technical Manager") || user.getUser_Position().equals("Technical Vice"))
                || (user.getUser_Position().equals("Chairman") || user.getUser_Position().equals("Chairman Vice"))||user.getUser_Position().equals("Secretary")
                ||user.getUser_Position().equals("Branding Manager")||user.getUser_Position().equals("Relation Manger")) {

            Log.e("ProfileActivity", user.getUser_Committee());

            if(user.getUser_Committee().equals("Relation Committee") ||user.getUser_Committee().equals("Technical Committee")
                    || user.getUser_Committee().equals("Branding Committee")
                    ||user.getUser_Committee().equals("IT Committee")||user.getUser_Committee().equals("HR Committee")||user.getUser_Position().equals("Secretary") )
            {

                if (user.getUser_Committee().equals("IT Committee") || user.getUser_Committee().equals("HR Committee")) {
                    CommitteeName1.setText(user.getUser_Committee());
                    committeeChat1.setText(user.getUser_Committee() + " Chatting");

                }else
                if(user.getUser_Position().equals("Secretary"))
                {
                    CommitteeName1.setText(user.getUser_Committee());
                    committeeChat1.setText(user.getUser_Committee()+" Chatting");
                }
                else {
                    CommitteeName1.setText(user.getUser_Committee().split(" ")[0] + " Section");
                    committeeChat1.setText(user.getUser_Committee().split(" ")[0] + " Section Chatting");
                }
                if (bundle == null) {
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
                }
                CommitteeName2.setText("High Board");
                committeeChat2.setText("High Board" + " Chatting");

            }
            else if(user.getUser_Position().equals("Chairman")||user.getUser_Position().equals("Chairman Vice"))
            {
                CommitteeName1.setText(user.getUser_Committee());
                committeeChat1.setText(user.getUser_Committee()+"Chatting");
                CommitteeName2.setVisibility(View.GONE);
                committeeChat2.setVisibility(View.GONE);
            }

            else //head or vice of committee 3adya
                {
                    String SectionWithNoSpaces="";
                    if(user.getUser_Committee().equals("Embedded Committee")||user.getUser_Committee().equals("Power Committee")
                            || user.getUser_Committee().equals("Electronics Committee")||user.getUser_Committee().equals("Biomedical Committee")||user.getUser_Committee().equals("Computer Committee"))
                    {
                        CommitteeName2.setText("Technical Section");
                        committeeChat2.setText("Technical Section Chatting");
                        SectionWithNoSpaces = "TechnicalCommittee";
                    }
                    if(user.getUser_Committee().equals("Multimedia Committee")||user.getUser_Committee().equals("Magazine Committee")
                            || user.getUser_Committee().equals("Marketing Committee"))
                    {
                        CommitteeName2.setText("Branding Section");
                        committeeChat2.setText("Branding Section Chatting");
                        SectionWithNoSpaces = "BrandingCommittee";
                    }
                    if(user.getUser_Committee().equals("OC Committee")||user.getUser_Committee().equals("PR Committee")
                            || user.getUser_Committee().equals("FR Committee"))
                    {
                        CommitteeName2.setText("Relations Section");
                        committeeChat2.setText("Relations Section Chatting");
                        SectionWithNoSpaces = "RelationCommittee";
                    }
                    CommitteeName1.setText(user.getUser_Committee());
                    committeeChat1.setText(user.getUser_Committee() +" Chatting");
                    if (bundle == null) {
                        FirebaseMessaging.getInstance().subscribeToTopic(SectionWithNoSpaces + "_Chatting")
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

                        FirebaseMessaging.getInstance().subscribeToTopic(SectionWithNoSpaces)
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
                    }
                }

        }
        else if (user.getUser_Position().equals("Member")) {
            Log.e("ProfileActivity", "user is member inside the member condition");
            CommitteeName1.setText(user.getUser_Committee());
            committeeChat1.setText(user.getUser_Committee() + " Chatting");
            CommitteeName2.setVisibility(View.GONE);
            committeeChat2.setVisibility(View.GONE);
        }
        if (bundle == null) {
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

        }
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


    private  void sendRegistrationToServer(String token) {
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
        if(UserInfo.sPosition.equals("Member")) {
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