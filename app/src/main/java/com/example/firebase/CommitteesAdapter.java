package com.example.firebase;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.firebase.models.Committee;
import com.example.firebase.models.Users;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CommitteesAdapter extends ArrayAdapter<Committee> {
    public CommitteesAdapter(Context context, int resource, ArrayList<Committee> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.committees_list_item,parent,false);
        }

        TextView CommitteeName = (TextView)convertView.findViewById(R.id.CommitteeName_textView);

        Committee committee = getItem(position);

        CommitteeName.setText(committee.getCommittee_Name());

        return convertView;
    }
}
