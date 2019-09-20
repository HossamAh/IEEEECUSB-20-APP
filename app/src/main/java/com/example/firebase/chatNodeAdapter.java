package com.example.firebase;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.firebase.CircleTransform;
import com.example.firebase.R;
import com.example.firebase.models.ChatNode;
import com.example.firebase.models.FreindlyMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class chatNodeAdapter extends ArrayAdapter<ChatNode> {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mFirebaseReference;

    public chatNodeAdapter(Context context, int resource, List<ChatNode> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.chat_item, parent, false);
        }

        ImageView photoImageView = (ImageView) convertView.findViewById(R.id.Receiver_imageView);
        final TextView messageTextView = (TextView) convertView.findViewById(R.id.Message_textView);
        TextView ReceiverTextView = (TextView) convertView.findViewById(R.id.ReciverName_textView);

        final ChatNode chatNode = getItem(position);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseReference = mFirebaseDatabase.getReference();
        ChildEventListener getLastMessageListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FreindlyMessage LastMessage = dataSnapshot.getValue(FreindlyMessage.class);
                if(chatNode.getLastMessage().getUser_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                {
                    messageTextView.setText("You: "+LastMessage.getText());
                }
                else
                {
                    messageTextView.setText(LastMessage.getName()+": "+LastMessage.getText());
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
        };
        String ID = chatNode.getChatID();
        if (ID.indexOf("_Chat") != -1)//privateChat
        {
            mFirebaseReference.child("Messages").child(ID).addChildEventListener(getLastMessageListener);
        }
        else {
            mFirebaseReference.child("Committees chatting").child("Messages").child(ID).addChildEventListener(getLastMessageListener);
        }
        ReceiverTextView.setText(chatNode.getReceiverName());


        boolean isPhoto = chatNode.getReceiverImageUrl() != null;
        if (isPhoto) {
            Picasso.get().load(chatNode.getReceiverImageUrl()).transform(new CircleTransform())
                    .into(photoImageView);
        } else {
            photoImageView.setImageResource(R.drawable.defaultprofile);
        }
        return convertView;
    }
}
