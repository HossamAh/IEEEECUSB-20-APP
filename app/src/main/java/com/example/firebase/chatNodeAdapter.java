package com.example.firebase;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.firebase.models.ChatNode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class chatNodeAdapter extends ArrayAdapter<ChatNode> {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mFirebaseReference;
    private boolean check1;
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
        int authIDStartIndex = chatNode.getChatID().indexOf(FirebaseAuth.getInstance().getCurrentUser().getUid());
        if(authIDStartIndex == 0)//auth first.
        {
            check1 = true;
        }
        else {
            check1=false;
        }


        if(check1) {
            ReceiverTextView.setText(chatNode.getName2());
        }
        else
            {
                ReceiverTextView.setText(chatNode.getName1());
            }
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(chatNode.getLastMessage().getUser_id()))
        {
            messageTextView.setText("You: "+chatNode.getLastMessage().getText());
        }
        else
            {
                messageTextView.setText(chatNode.getLastMessage().getName()+": "+chatNode.getLastMessage().getText());
            }

        boolean isPhoto;
        if(check1)
        {
            isPhoto=chatNode.getImageUrl2() != null;
            if (isPhoto) {
                Picasso.get().load(chatNode.getImageUrl2()).transform(new CircleTransform())
                        .into(photoImageView);
            } else {
                photoImageView.setImageResource(R.drawable.defaultprofile);
            }
        }
        else
        {
            isPhoto=chatNode.getImageUrl1() != null;
            if (isPhoto) {
                Picasso.get().load(chatNode.getImageUrl1()).transform(new CircleTransform())
                        .into(photoImageView);
            } else {
                photoImageView.setImageResource(R.drawable.defaultprofile);
            }
        }


        return convertView;
    }
}
