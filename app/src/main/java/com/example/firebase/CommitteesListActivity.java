package com.example.firebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.firebase.models.Committee;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CommitteesListActivity extends AppCompatActivity {
    private ListView CommitteesListView;
    private TextView CommitteesListTitle;
    private ArrayList<Committee> committeeArrayList;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private CommitteesAdapter committeesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_committees_list);
        CommitteesListView = (ListView)findViewById(R.id.CommitteesList_ListView);
        CommitteesListTitle = (TextView)findViewById(R.id.CommittessListTitile_textView);
        committeeArrayList = new ArrayList<>();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        committeesAdapter = new CommitteesAdapter(getBaseContext(),R.layout.committees_list_item,committeeArrayList);
        CommitteesListView.setAdapter(committeesAdapter);

        CommitteesListTitle.setText("Committees List");

        CommitteesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(CommitteesListActivity.this,CommitteActivity.class).putExtra("CommitteeName",committeesAdapter.getItem(position).getCommittee_Name()));
            }
        });

        mDatabaseReference.child("Committees").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Committee committee = new Committee();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    if(snapshot.getKey().equals("Committee_Name"))
                    {
                        committee.setCommittee_Name(snapshot.getValue().toString());
                        committeeArrayList.add(committee);
                    }
                    committeesAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
}
