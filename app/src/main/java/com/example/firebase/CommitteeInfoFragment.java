package com.example.firebase;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.firebase.models.Committee;
import com.example.firebase.models.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class CommitteeInfoFragment extends Fragment {
    private TextView mCommitteeHead1_NameTextView,
            mCommitteeHead2_NameTextView,HeadTitle_TextView;
    private ImageView mCommitteeImageView;
    private ListView mMembersListView;
    private Committee mCommittee;
    private String Committee_Name;
    private FirebaseDatabase mFireBaseDatabase;
    private DatabaseReference mFireBaseDatabaseReference;
    private MembersAdapter mMembersAdapter;
    private List<String> Head_IDs;
    private String Vice_ID;
    private List<String> mMembers_IDs;
    private List<Users> mMembers;
    private Users Head1,Head2,Vice;
    private int headsCount;

    public CommitteeInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_committee_info, container, false);

        mCommitteeHead1_NameTextView = (TextView)view.findViewById(R.id.HeadName_textView);
        mCommitteeHead2_NameTextView = (TextView)view.findViewById(R.id.headName_textView);
        mMembersListView = (ListView)view.findViewById(R.id.Members_ListView);
        HeadTitle_TextView = (TextView)view.findViewById(R.id.Head_textView);

        Committee_Name = getArguments().getString("CommitteeName");
        mMembers_IDs = new ArrayList<>();
        mCommittee =new Committee();
        mFireBaseDatabase = FirebaseDatabase.getInstance();

        mFireBaseDatabaseReference = mFireBaseDatabase.getReference();

        mCommitteeHead1_NameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ProfileActivity.class).putExtra("UserID", Head1.getUid()));
            }
        });
        mCommitteeHead2_NameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ProfileActivity.class).putExtra("UserID", Head2.getUid()));
            }
        });

        Bundle CommitteeData = getArguments();
        Head_IDs = CommitteeData.getStringArrayList("Heads_IDS");
        if(Committee_Name.equals("High Board"))
        {
            HeadTitle_TextView.setText("Chairman:");
        }
        else if(Committee_Name.equals("Technical Committee"))
        {
            HeadTitle_TextView.setText("Technical Manager:");
        }
        else
            {
                HeadTitle_TextView.setText("Head:");
            }
        if(!Head_IDs.isEmpty()) {
            mFireBaseDatabaseReference.child("users").child(Head_IDs.get(0)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Head1 = dataSnapshot.getValue(Users.class);
                    Log.e("CommitteeActivity", dataSnapshot.getValue().toString());
                    Log.e("CommitteeActivity", "heads num: " + Head_IDs.size());
                    Head1.setUid(dataSnapshot.getKey());
                    mCommitteeHead1_NameTextView.setText(Head1.getUser_Name());
                    //mCommitteeHead2_NameTextView.setText(user.getUser_Name());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            ValueEventListener Head2Listener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Head2 = dataSnapshot.getValue(Users.class);
                    Log.e("CommitteeActivity", "head2" + dataSnapshot.getValue().toString());
                    mCommitteeHead2_NameTextView.setVisibility(View.VISIBLE);
                    mCommitteeHead2_NameTextView.setText(Head2.getUser_Name());
                    Head2.setUid(dataSnapshot.getKey());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            if (Head_IDs.size() == 2) {
                mFireBaseDatabaseReference.child("users").child(Head_IDs.get(1)).addValueEventListener(Head2Listener);
            } else
                mCommitteeHead2_NameTextView.setVisibility(View.GONE);
        }




        return view;
    }


}
