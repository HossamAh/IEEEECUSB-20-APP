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
import android.widget.TextView;

import com.example.firebase.models.Committee;
import com.example.firebase.models.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ViceFragment extends Fragment {
    private String Vice_ID;
    private FirebaseDatabase mFireBaseDatabase;
    private DatabaseReference mFireBaseDatabaseReference;
    private TextView mCommitteeVice_NameTextView,ViceTitle_TextView;
    private String Committee_Name;
    private Users Vice;
    public ViceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_vice, container, false);
        mFireBaseDatabase = FirebaseDatabase.getInstance();
        mFireBaseDatabaseReference = mFireBaseDatabase.getReference();
        mCommitteeVice_NameTextView = (TextView)view.findViewById(R.id.viceName_textView);
        ViceTitle_TextView = (TextView)view.findViewById(R.id.vice_textView);
        Committee_Name = getArguments().getString("CommitteeName");
        if(Committee_Name.equals("High Board"))
        {
            ViceTitle_TextView.setText("Chairman Vice:");
        }
        else if(Committee_Name.equals("Technical Committee"))
        {
            ViceTitle_TextView.setText("Technical Vice:");
        }
        else
        {
            ViceTitle_TextView.setText("Vice:");
        }

        mCommitteeVice_NameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ProfileActivity.class).putExtra("UserID", Vice.getUid()));
            }
        });

        Vice_ID = getArguments().getString("ViceID");
        //Log.e("ViceFragment",Vice_ID);
if(Vice_ID !=null) {
    mFireBaseDatabaseReference.child("users").child(Vice_ID).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Log.e("ViceFragment", dataSnapshot.getValue().toString());

            Vice= dataSnapshot.getValue(Users.class);
            //user.setUid(snapshot.getKey());
            mCommitteeVice_NameTextView.setText(Vice.getUser_Name());
            Vice.setUid(dataSnapshot.getKey());


        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
}
else
    {
        ViceTitle_TextView.setVisibility(View.GONE);
        mCommitteeVice_NameTextView.setVisibility(View.GONE);
    }


        return view;
    }
}
