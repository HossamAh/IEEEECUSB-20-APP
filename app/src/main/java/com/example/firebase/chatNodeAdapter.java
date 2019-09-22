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
import com.google.firebase.database.ValueEventListener;
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


        ReceiverTextView.setText(chatNode.getReceiverName());
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(chatNode.getLastMessage().getUser_id()))
        {
            messageTextView.setText("You: "+chatNode.getLastMessage().getText());
        }
        else
            {
                messageTextView.setText(chatNode.getLastMessage().getName()+": "+chatNode.getLastMessage().getText());
            }

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
