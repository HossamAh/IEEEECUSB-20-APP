package com.example.firebase;

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RecyclerViewAdapterAnnouncements extends RecyclerView.Adapter<RecyclerViewAdapterAnnouncements.ViewHolder> {
    @NonNull
    private ArrayList<Announcement> announcements = new ArrayList<>();
    private Context mContext;
    private OnNotListener onNotListener;

    public RecyclerViewAdapterAnnouncements(@NonNull ArrayList<Announcement> announcements, Context mContext , OnNotListener onNotListener) {
        this.announcements = announcements;
        this.mContext = mContext;
        this.onNotListener = onNotListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.announcement_item,viewGroup,false);
        ViewHolder holder = new ViewHolder(view, onNotListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        //viewHolder.thumbnail.setImageURI(announcements.get(i).getImgUri());
        viewHolder.description.setText(announcements.get(i).getTopic());
        viewHolder.id.setText(announcements.get(i).getId());
        viewHolder.header.setText(announcements.get(i).getTarget());

    }
    Intent intent;

    @Override
    public int getItemCount() {
        return announcements.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView header;
        OnNotListener onNotListener;
        TextView description;
        TextView id;
        public ViewHolder(@NonNull final View itemView , OnNotListener onNotListener) {
            super(itemView);
            header = (TextView) itemView.findViewById(R.id.AnnouncementItemTarget);
            description =(TextView) itemView.findViewById(R.id.AnnouncementItemTopic);
            id = (TextView) itemView.findViewById(R.id.id);
            this.onNotListener = onNotListener;
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {
            onNotListener.OnNotClick(getAdapterPosition());
        }

    }
    public interface OnNotListener{
        void OnNotClick(int position);


    }
}
