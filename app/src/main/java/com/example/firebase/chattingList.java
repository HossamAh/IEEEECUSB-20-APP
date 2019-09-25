package com.example.firebase;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    private int chatType;
    private String ProfileUid; //receiver id
    private String CommitteeName;
    private String ProfileName;//receiver name
    public chattingList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(mChattingAdapter!=null)
        mChattingAdapter.clear();

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
        chattings.setAdapter(mChattingAdapter);

        chattings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.e("ChattingListFragment",chattingIDsList.get(position));
                if(chattingIDsList.get(position).indexOf("_Chat") < 0 )//private chat
                    {

                        chatType = 0;
                        int authIDLength = FirebaseAuth.getInstance().getCurrentUser().getUid().length();
                        int authIDStartIndex = chattingIDsList.get(position).indexOf(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        if(authIDStartIndex == 0)
                        {
                            ProfileUid = chattingIDsList.get(position).substring(authIDLength);
                            Log.e("ChattingListFragment","authIDStartIndex = 0"+chattingIDsList.get(position).substring(authIDLength));

                        }
                        else {
                            ProfileUid = chattingIDsList.get(position).substring(0,authIDStartIndex);
                            Log.e("ChattingListFragment","authIDStartIndex > 0"+chattingIDsList.get(position).substring(0,5));
                        }
                        ProfileName = chatNodes.get(position).getReceiverName();
                        CommitteeName="";
                    }
                else if(chattingIDsList.get(position).equals("IEEECUSB_Chat"))
                    {
                        ProfileName="";
                        ProfileUid="";
                        chatType=2;
                        CommitteeName = "";
                    }
                else
                    {
                        chatType=1;
                        ProfileUid="";
                        ProfileName="";
                      CommitteeName = chattingIDsList.get(position).substring(0,chattingIDsList.get(position).indexOf("_Chat"));
                    }

                Intent intent = new Intent(getContext(), HomeActivity.class);
                intent.putExtra("ProfileUID", ProfileUid);
                intent.putExtra("chatType", chatType);
                intent.putExtra("CommitteeName",CommitteeName);
                intent.putExtra("ProfileUserName", ProfileName);
                startActivity(intent);

            }

        });

        ValueEventListener GetUserDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("ChattingListFragment",dataSnapshot.getValue().toString());
                ChatNode chatNode = new ChatNode();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    if(snapshot.getKey().equals("Last Message"))
                    {
                        chatNode.setLastMessage(snapshot.getValue(FreindlyMessage.class));
                    }
                    else if(snapshot.getKey().equals("ReceiverName"))
                    {
                        chatNode.setReceiverName(snapshot.getValue().toString());
                    }
                    else
                        {
                            chatNode.setReceiverImageUrl(snapshot.getValue().toString());
                        }
                }
                chatNodes.add(chatNode);
                for(ChatNode chatNode1:chatNodes)
                {
                    Log.e("ChattingListFragment",chatNode1.getLastMessage().getText());
                }
                mChattingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        if(chattingIDsList !=null) {
            chatNodes.clear();
    for (String ID : chattingIDsList) {

        if(ID.indexOf("_Chat")!= -1)//committee chat
        {
            mFireBaseDatabaseReference.child("Committees chatting").child(ID).child("ChatNode").addValueEventListener(GetUserDataListener);
        }
        else //private
            {
                mFireBaseDatabaseReference.child("PrivateChatting").child(ID).child("ChatNode").addValueEventListener(GetUserDataListener);
            }
    }
}
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mChattingAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        mChattingAdapter.clear();
    }
}
