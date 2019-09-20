package com.example.firebase;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.firebase.models.ChatNode;
import com.example.firebase.models.FreindlyMessage;
import com.example.firebase.models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class chattingList extends Fragment {
    private ArrayList<String> chattingIDsList;
    private ListView chattings;
    private ArrayList<ChatNode> chatNodes;
    private chatNodeAdapter mChattingAdapter;
    private FirebaseDatabase mFireBaseDatabase;
    private DatabaseReference mFireBaseDatabaseReference;
    private ChatNode chatNode;
    private String currentID;
    public chattingList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_chatting_list, container, false);
        Log.e("ChattingListFragment","in ChattingListFragment ");
        chatNodes = new ArrayList<>();
        chattingIDsList = getArguments().getStringArrayList("ChattingIDs");
        for(String ID : chattingIDsList)
        {
            Log.e("ChattingListFragment","IDs is : "+ID);
        }


        chattings = (ListView)view.findViewById(R.id.ChattingNodesList);
        mFireBaseDatabase = FirebaseDatabase.getInstance();
        mFireBaseDatabaseReference = mFireBaseDatabase.getReference();
        mChattingAdapter = new chatNodeAdapter(getContext(),R.layout.chat_item,chatNodes);




        ValueEventListener GetUserDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("ChattingListFragment",dataSnapshot.getValue().toString());
                chatNode.setReceiverName(dataSnapshot.getValue(Users.class).getUser_Name());
                chatNode.setReceiverImageUrl(dataSnapshot.getValue(Users.class).getUrl());
                mChattingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };


if(chattingIDsList !=null) {
    for (String ID : chattingIDsList) {
        chatNode = new ChatNode();
        chatNode.setChatID(ID);
        if (ID.indexOf("_Chat") != -1)//privateChat
        {
            Log.e("ChattingListFragment",ID);
            int AuthIDIndex = ID.indexOf(FirebaseAuth.getInstance().getCurrentUser().getUid());
            int AuthIDLenght = ID.length();
            if (AuthIDIndex > 0) {
                String ReceiverID = ID.substring(0, AuthIDIndex);
                mFireBaseDatabaseReference.child("users").child(ReceiverID).addValueEventListener(GetUserDataListener);
            }
            else if (AuthIDIndex == 0)
            {
                String ReceiverID = ID.substring(AuthIDLenght);
                mFireBaseDatabaseReference.child("users").child(ReceiverID).addValueEventListener(GetUserDataListener);
            }
        }
        else
         {
            int committeeNameEndIndex = ID.indexOf("_Chat");
            chatNode.setReceiverName(ID.substring(0, committeeNameEndIndex));

        }
        chatNodes.add(chatNode);
        chattings.setAdapter(mChattingAdapter);
    }

}


        return view;
    }


}
