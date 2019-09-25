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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.firebase.models.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.MeasureSpec.makeMeasureSpec;


public class MembersFragment extends Fragment {
    private List<String> mMembers_IDs;
    private FirebaseDatabase mFireBaseDatabase;
    private DatabaseReference mFireBaseDatabaseReference;
    private ListView mMembersListView;
    private ArrayList<Users> mMembers;
    private MembersAdapter mMembersAdapter;
    private String Committee_Name;
    private TextView MembersTitle;
    private int width;
    public MembersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_members, container, false);
        mMembers = new ArrayList<>();
        mMembersListView = (ListView)view.findViewById(R.id.Members_ListView);
        mFireBaseDatabase = FirebaseDatabase.getInstance();
        mFireBaseDatabaseReference = mFireBaseDatabase.getReference();
        MembersTitle = (TextView)view.findViewById(R.id.membersTitle_textView2);
        mMembersAdapter = new MembersAdapter(getContext(),R.layout.member_item,mMembers);
        mMembersListView.setAdapter(mMembersAdapter);

        mMembers_IDs = getArguments().getStringArrayList("MembersIDs");
        Committee_Name = getArguments().getString("CommitteeName");

        if(!mMembers_IDs.isEmpty()) {
            MembersTitle.setText(Committee_Name+" Members");

            mMembersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.e("MembersFragment", "member item is clicked ");
                    Log.e("MembersFragment", "user name clicked is : " + mMembers.get(position).getUser_Name());
                    String uid = mMembers.get(position).getUid();
                    startActivity(new Intent(getContext(), ProfileActivity.class).putExtra("UserID", uid));
                }
            });


            for(int membersCounter = 0;membersCounter<mMembers_IDs.size();membersCounter++){
            mFireBaseDatabaseReference.child("users").child(mMembers_IDs.get(membersCounter)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.e("MembersFragment",dataSnapshot.getValue().toString());
                    Users user = dataSnapshot.getValue(Users.class);
                    user.setUid(dataSnapshot.getKey());
                    mMembers.add(user);
                    Log.e("CommitteeActivity", user.getUser_Name());
                    mMembersAdapter.notifyDataSetChanged();
                    Log.e("CommitteeActivity", "members number :"+mMembersListView.getCount());
                   // setListViewHeightBasedOnChildren(mMembersListView);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            }
        }
        else
            {
                mMembersListView.setVisibility(View.GONE);
                MembersTitle.setText("No Members Till Now");
            }
        return view;
    }
    public static void setListViewHeightBasedOnChildren
            (ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) return;
        int desiredWidth = makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0) view.setLayoutParams(new
                    ViewGroup.LayoutParams(desiredWidth,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
            }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight + (listView.getDividerHeight() *
                (listAdapter.getCount()-1));

        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
