package com.example.firebase;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.firebase.models.Committee;
import com.example.firebase.models.FreindlyMessage;
import com.example.firebase.models.Users;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MembersAdapter extends ArrayAdapter<Users> {

    public MembersAdapter(Context context, int resource, List<Users> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.member_item, parent, false);
        }

        TextView memberName = (TextView)convertView.findViewById(R.id.MemberName_textView);
        ImageView MemberImage = (ImageView)convertView.findViewById(R.id.Member_imageView);

        Users user = getItem(position);

        memberName.setText(user.getUser_Name());

        if(user.getUrl() !=null)
        {
            Picasso.get().load(user.getUrl()).
                    transform(new CircleTransform()).into(MemberImage);
        }
        else
        {
            MemberImage.setImageResource(R.drawable.defaultprofile);
        }

        return convertView;
    }

}
