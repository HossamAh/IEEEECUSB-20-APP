package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.firebase.models.ChatNode;
import com.example.firebase.models.FreindlyMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PreviousChatting extends AppCompatActivity {
    private static final String TAG = "PreviousChatting";
    private TextView PreviousChattingTitleTextView;
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
    private ValueEventListener getChattingListIDs;
    private ValueEventListener GetUserDataListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_chatting);
        PreviousChattingTitleTextView = (TextView)findViewById(R.id.previousChattingTitle);
        PreviousChattingTitleTextView.setText("Your Previous Messages");
        Log.e(TAG, "onCreate: in it " );
        chatNodes = new ArrayList<>();


        chattingIDsList = new ArrayList<>();
        chattings = (ListView)findViewById(R.id.ChattingNodesList);
        mChattingAdapter = new chatNodeAdapter(PreviousChatting.this,R.layout.chat_item,chatNodes);
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
                        ProfileName = chatNodes.get(position).getName2();
                        ProfileUid = chattingIDsList.get(position).substring(authIDLength);
                        Log.e("ChattingListFragment","authIDStartIndex = 0"+chattingIDsList.get(position).substring(authIDLength));

                    }
                    else {
                        ProfileName = chatNodes.get(position).getName1();
                        ProfileUid = chattingIDsList.get(position).substring(0,authIDStartIndex);
                        Log.e("ChattingListFragment","authIDStartIndex > 0"+chattingIDsList.get(position).substring(0,5));
                    }

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

                Intent intent = new Intent(PreviousChatting.this, HomeActivity.class);
                intent.putExtra("ProfileUID", ProfileUid);
                intent.putExtra("chatType", chatType);
                intent.putExtra("CommitteeName",CommitteeName);
                intent.putExtra("ProfileUserName", ProfileName);
                startActivity(intent);
            }
        });
        //mChattingAdapter.clear();
        //attachDatabaseReadListner();

    }


    private void detachDatabaseReadListener(){
        if(getChattingListIDs != null || GetUserDataListener != null) {
            mFireBaseDatabaseReference.removeEventListener(getChattingListIDs);
            mFireBaseDatabaseReference.removeEventListener(GetUserDataListener);
        }

        getChattingListIDs=null;
    }
    private void attachDatabaseReadListner() {

        mFireBaseDatabase = FirebaseDatabase.getInstance();
        mFireBaseDatabaseReference = mFireBaseDatabase.getReference();

        if(getChattingListIDs == null )
        {
            Log.e("PreviousChatting","getchattingListIds is null inside attach on value listener");
            GetUserDataListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 //   Log.e("ChattingListFragment",dataSnapshot.getValue().toString());
                    ChatNode chatNode = new ChatNode();
                    for(DataSnapshot snapshot:dataSnapshot.getChildren())
                    {
                        if(snapshot.getKey().equals("Last Message"))
                        {
                            chatNode.setLastMessage(snapshot.getValue(FreindlyMessage.class));
                        }
                        else if(snapshot.getKey().equals("Name1"))
                        {
                            chatNode.setName1(snapshot.getValue().toString());
                        }
                        else if(snapshot.getKey().equals("Name2"))
                        {
                            chatNode.setName2(snapshot.getValue().toString());
                        }
                        else if(snapshot.getKey().equals("ImageUrl1"))
                        {
                            chatNode.setImageUrl1(snapshot.getValue().toString());
                        }
                        else if(snapshot.getKey().equals("ImageUrl2"))
                        {
                            chatNode.setImageUrl2(snapshot.getValue().toString());
                        }
                        else if (snapshot.getKey().equals("ChatID"))
                        {
                            chatNode.setChatID(snapshot.getValue().toString());
                        }
                    }
                    mChattingAdapter.add(chatNode);
                    mChattingAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            getChattingListIDs = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
                        Log.e("PreviousChatting", snapshot.getValue().toString());
                        chattingIDsList.add(snapshot.getValue().toString());
                        String ID =snapshot.getValue().toString();
                        if (ID.indexOf("_Chat") != -1)//committee chat
                        {
                            mFireBaseDatabaseReference.child("Committees chatting").child(ID).child("ChatNode").addValueEventListener(GetUserDataListener);
                        } else //private
                        {
                            mFireBaseDatabaseReference.child("PrivateChatting").child(ID).child("ChatNode").addValueEventListener(GetUserDataListener);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
        }
        mFireBaseDatabaseReference.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ChattingList").addValueEventListener(getChattingListIDs);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        detachDatabaseReadListener();
        mChattingAdapter.clear();

    }


    @Override
    protected void onStart() {
        super.onStart();
        mChattingAdapter.clear();
        attachDatabaseReadListner();

    }
}
