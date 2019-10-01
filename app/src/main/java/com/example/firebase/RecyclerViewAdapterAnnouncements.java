package com.example.firebase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.firebase.models.Announcement;

import java.util.ArrayList;

public class RecyclerViewAdapterAnnouncements extends RecyclerView.Adapter<RecyclerViewAdapterAnnouncements.ViewHolder> {
    @NonNull
    private ArrayList<Announcement> announcements = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapterAnnouncements(@NonNull ArrayList<Announcement> announcements, Context mContext) {
        this.announcements = announcements;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.announcement_item,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        //viewHolder.thumbnail.setImageURI(announcements.get(i).getImgUri());
        viewHolder.description.setText(announcements.get(i).getTopic());
        viewHolder.header.setText(announcements.get(i).getTarget());

    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView header;
        TextView description;
        LinearLayout parent;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            header = (TextView) itemView.findViewById(R.id.AnnouncementItemTarget);
            description =(TextView) itemView.findViewById(R.id.AnnouncementItemTopic);
            ( (CardView)itemView.findViewById(R.id.parent)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //send ID to intent like noty
                }
            });
        }
    }
}
