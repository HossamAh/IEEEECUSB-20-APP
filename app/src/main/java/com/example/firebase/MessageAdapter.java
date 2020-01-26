package com.example.firebase;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.firebase.models.FreindlyMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<FreindlyMessage> {
    public MessageAdapter(Context context, int resource, List<FreindlyMessage> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_message, parent, false);
        }

        ImageView photoImageView = (ImageView) convertView.findViewById(R.id.photoImageView);
        TextView messageTextView = (TextView) convertView.findViewById(R.id.messageTextView);
        TextView authorTextView = (TextView) convertView.findViewById(R.id.nameTextView);
        TextView TimeStamp = (TextView)convertView.findViewById(R.id.Time_textView);
        ImageView senderImage = (ImageView) convertView.findViewById(R.id.Sender_imageView);

        FreindlyMessage message = getItem(position);

        boolean isPhoto = message.getPhotoUrl() != null;
        if (isPhoto) {
            //Log.e("MessageAdapter","download image uri "+message.getPhotoUrl());
            messageTextView.setVisibility(View.GONE);
            photoImageView.setVisibility(View.VISIBLE);
            Picasso.get().load(message.getPhotoUrl())
                    .into(photoImageView);
        } else {

            messageTextView.setVisibility(View.VISIBLE);
            photoImageView.setVisibility(View.GONE);
            messageTextView.setText(message.getText());
        }
        if(message.getSender_imageUrl() !=null)
        {
            Picasso.get().load(message.getSender_imageUrl()).
                    transform(new CircleTransform()).into(senderImage);
        }
        else
        {
            senderImage.setImageResource(R.drawable.defaultprofile);
        }
        authorTextView.setText(message.getName());
        TimeStamp.setText(message.getTimestamp());
        if(message.getUser_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            if(message.getText()!=null)
            {
                messageTextView.setBackgroundResource(R.drawable.auth_background);
            }
            else
                {
                    photoImageView.setBackgroundResource(R.drawable.auth_background);
                }
        }
        else
            {
                if(message.getText()!=null)
                {
                    messageTextView.setBackgroundResource(R.drawable.others_background);
                }
                else
                {
                    photoImageView.setBackgroundResource(R.drawable.others_background);
                }
            }

        return convertView;
    }
}

