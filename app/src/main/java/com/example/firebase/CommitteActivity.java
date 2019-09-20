package com.example.firebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.firebase.models.Committee;
import com.example.firebase.models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CommitteActivity extends AppCompatActivity {
    private TextView mCommittee_NameTextView;
    private ImageView mCommitteeImageView;
    private Committee mCommittee;
    private String Committee_Name;
    private FirebaseDatabase mFireBaseDatabase;
    private DatabaseReference mFireBaseDatabaseReference;
    private ArrayList<String> Head_IDs;
    private ArrayList<String>mMembers_IDs;
    private String Vice_ID;
    private FirebaseAuth mFirebaseAuth;
    private Toolbar toolbar;
    private FrameLayout heads;
    private FrameLayout vice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.committee_activity);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle bundle = getIntent().getExtras();
        Committee_Name = bundle.get("CommitteeName").toString();
        Log.e("CommitteeActivity", "Committee Name is : " + Committee_Name);
        mFireBaseDatabase = FirebaseDatabase.getInstance();
        Head_IDs = new ArrayList<>();
        mMembers_IDs = new ArrayList<>();
        mCommittee = new Committee();
        mFireBaseDatabaseReference = mFireBaseDatabase.getReference();
        mCommitteeImageView = (ImageView) findViewById(R.id.committe_ImageView);
        mCommittee_NameTextView = (TextView) findViewById(R.id.CommitteeName_TextView);
        mCommittee.setCommittee_Name(Committee_Name);
        mFirebaseAuth = FirebaseAuth.getInstance();
        heads = (FrameLayout) findViewById(R.id.HeadNVice_FrameLayout);
        vice = (FrameLayout) findViewById(R.id.HeadNVice2_FrameLayout);

        mCommitteeImageView.setImageResource(R.drawable.committee);
        mFireBaseDatabaseReference.child("Committees").child(Committee_Name).child("Committee_Name").setValue(Committee_Name);
        mCommittee_NameTextView.setText(mCommittee.getCommittee_Name());


        mFireBaseDatabaseReference.child("Committees").child(Committee_Name).child("Committee_Heads_IDs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.e("CommitteeActivity", "heads number from dataSnapshot  " + dataSnapshot.getChildrenCount());
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Head_IDs.add(snapshot.getValue().toString());
                        Log.e("CommitteeActivity", snapshot.getValue().toString());
                        Log.e("CommitteeActivity", Head_IDs.get(0));
                    }
                    mCommittee.setCommittee_Heads_IDs(Head_IDs);
                    Fragment CommitteeInfo = new CommitteeInfoFragment();
                    Bundle CommitteeData = new Bundle();
                    if (!Head_IDs.isEmpty()) {
                        Log.e("CommitteeActivity", "successful extraction heads IDs");
                        CommitteeData.putStringArrayList("Heads_IDS", Head_IDs);
                        CommitteeData.putString("CommitteeName", Committee_Name);
                        CommitteeInfo.setArguments(CommitteeData);
                        getSupportFragmentManager().beginTransaction().add(R.id.HeadNVice_FrameLayout, CommitteeInfo, "CommitteeInfoFragment").commit();
                    }
                } else {
                    heads.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mFireBaseDatabaseReference.child("Committees").child(Committee_Name).child("Committee_Vice_ID").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.e("CommitteeActivity", "Vice ID " + dataSnapshot.getValue().toString());
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                        Vice_ID = snapshot.getValue().toString();
                    Log.e("CommitteeActivity", "Vice ID " + Vice_ID);
                    mCommittee.setCommittee_Vice_ID(Vice_ID);
                    Fragment CommitteeViceInfo = new ViceFragment();
                    Bundle CommitteeData = new Bundle();
                    CommitteeData.putString("ViceID", Vice_ID);
                    CommitteeData.putString("CommitteeName", Committee_Name);
                    CommitteeViceInfo.setArguments(CommitteeData);
                    getSupportFragmentManager().beginTransaction().add(R.id.HeadNVice2_FrameLayout, CommitteeViceInfo, "CommitteeViceInfoFragment").commit();
                } else {
                    vice.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mFireBaseDatabaseReference.child("Committees").child(Committee_Name).child("members_IDs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Log.e("CommitteeActivity", "Members IDs " + snapshot.getValue().toString());
                        mMembers_IDs.add(snapshot.getValue().toString());
                    }
                    mCommittee.setMembers_IDs(mMembers_IDs);
                    Fragment CommitteeMembersInfo = new MembersFragment();
                    Bundle CommitteeData = new Bundle();
                    CommitteeData.putStringArrayList("MembersIDs", mMembers_IDs);
                    CommitteeData.putString("CommitteeName", Committee_Name);
                    CommitteeMembersInfo.setArguments(CommitteeData);
                    getSupportFragmentManager().beginTransaction().add(R.id.Members_FrameLayout, CommitteeMembersInfo, "CommitteeMembersInfoFragment").commit();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R. menu.nav_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.draw_committees_menu:
                Intent intent = new Intent(CommitteActivity.this,CommitteesListActivity.class);
                startActivity(intent);
                break;
            case R.id.draw_Logout_menu:
                mFirebaseAuth.signOut();
                startActivity(new Intent(CommitteActivity.this,LoginActivity.class));
                break;
        }

        return true;

    }


}
